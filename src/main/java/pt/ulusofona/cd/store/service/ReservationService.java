package pt.ulusofona.cd.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ulusofona.cd.store.client.RestaurantAvailabilityClient;
import pt.ulusofona.cd.store.client.RestaurantCapacityClient;
import pt.ulusofona.cd.store.dto.AvailabilitySlotDto;
import pt.ulusofona.cd.store.dto.ReleaseSeatsRequest;
import pt.ulusofona.cd.store.dto.ReservationCreateRequest;
import pt.ulusofona.cd.store.events.ReservationEventProducer;
import pt.ulusofona.cd.store.exception.ReservationNotFoundException;
import pt.ulusofona.cd.store.mapper.ReservationMapper;
import pt.ulusofona.cd.store.model.Reservation;
import pt.ulusofona.cd.store.model.ReservationStatus;
import pt.ulusofona.cd.store.repository.ReservationRepository;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final RestaurantAvailabilityClient restaurantAvailabilityClient;
    private final RestaurantCapacityClient restaurantCapacityClient;

    private final ReservationEventProducer reservationEventProducer;

    @Transactional
    public Reservation createReservation(ReservationCreateRequest request, String traceId) {

        if (request.getScheduledAt() == null || !request.getScheduledAt().isAfter(Instant.now())) {
            throw new IllegalArgumentException("scheduledAt não pode ser agora mesmo");
        }


        if (request.getPartySize() <= 0) {
            throw new IllegalArgumentException("partySize sempre maior que 0");
        }

        String date = request.getScheduledAt().atZone(ZoneOffset.UTC).toLocalDate().toString();

        List<AvailabilitySlotDto> slots = restaurantAvailabilityClient.getAvailability(
                request.getRestaurantId().toString(),
                date,
                request.getPartySize()
        );

        boolean slotOk = slots.stream().anyMatch(s ->
                s.getId() != null && s.getId().equals(request.getAvailabilitySlotId())
        );

        if (!slotOk) {

            throw new IllegalStateException("Requested availability slot is not available for the given party size/date");
        }

        Reservation reservation = ReservationMapper.toEntity(request);
        reservation.setStatus(ReservationStatus.PENDING);

        Reservation saved = reservationRepository.save(reservation);

        reservationEventProducer.publishReservationCreated(saved, traceId);

        return saved;
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reserva não encontrada com o id: " + id));
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation confirmReservation(Long id, String traceId) {
        Reservation reservation = getReservationById(id);

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalArgumentException("confirm only allowed from PENDING");
        }

        String date = reservation.getScheduledAt().atZone(ZoneOffset.UTC).toLocalDate().toString();

        List<AvailabilitySlotDto> slots = restaurantAvailabilityClient.getAvailability(
                reservation.getRestaurantId().toString(),
                date,
                reservation.getPartySize()
        );

        boolean slotOk = slots.stream().anyMatch(s ->
                s.getId() != null && s.getId().equals(reservation.getAvailabilitySlotId())
        );

        if (!slotOk) {
            throw new IllegalStateException("Vaga ficou indisponível antes de poder confirmar.");
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        Reservation saved = reservationRepository.save(reservation);
        reservationEventProducer.publishReservationConfirmed(saved, traceId);

        return saved;
    }

    @Transactional
    public Reservation cancelReservation(Long id, String traceId) {
        Reservation reservation = getReservationById(id);

        if (reservation.getStatus() != ReservationStatus.PENDING &&
                reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalArgumentException("cancel allowed only from PENDING or CONFIRMED");
        }

        // Temos que nos lembrarrr disto: se cancelar a partir de CONFIRMED temos que chamar o Restaurant Service para libertar os lugares antes de mudar o estado
        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            ReleaseSeatsRequest req = new ReleaseSeatsRequest();
            req.setAvailabilitySlotId(reservation.getAvailabilitySlotId());
            req.setPartySize(reservation.getPartySize());

            restaurantCapacityClient.releaseSeats(reservation.getRestaurantId().toString(), req);
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        Reservation saved = reservationRepository.save(reservation);

        reservationEventProducer.publishReservationCancelled(saved, traceId);

        return saved;
    }
}
