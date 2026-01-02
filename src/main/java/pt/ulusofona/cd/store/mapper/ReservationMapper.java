package pt.ulusofona.cd.store.mapper;

import pt.ulusofona.cd.store.dto.ReservationCreateRequest;
import pt.ulusofona.cd.store.dto.ReservationResponse;
import pt.ulusofona.cd.store.model.Reservation;
import pt.ulusofona.cd.store.model.ReservationStatus;

public class ReservationMapper {

    public static Reservation toEntity(ReservationCreateRequest dto) {
        Reservation r = new Reservation();

        r.setRestaurantId(dto.getRestaurantId());
        r.setAvailabilitySlotId(dto.getAvailabilitySlotId());

        r.setCustomerName(dto.getCustomerName().trim());
        r.setCustomerEmail(dto.getCustomerEmail().trim());

        r.setPartySize(dto.getPartySize());
        r.setScheduledAt(dto.getScheduledAt());

        //EU JURO QUE SE VOLTAS A ENGANR-TE NO ESTADO TE FAÇO PAGAR-ME UMA CERVEJA (JÉSSICA)
        //ESTÁ TUDO NO ENUNCIADO... PENDING ANDRÉE MIGUEL PENDING
        r.setStatus(ReservationStatus.PENDING);

        return r;
    }

    public static ReservationResponse toResponse(Reservation entity) {
        ReservationResponse resp = new ReservationResponse();

        resp.setId(entity.getId());
        resp.setRestaurantId(entity.getRestaurantId());
        resp.setAvailabilitySlotId(entity.getAvailabilitySlotId());

        resp.setCustomerName(entity.getCustomerName());
        resp.setCustomerEmail(entity.getCustomerEmail());

        resp.setPartySize(entity.getPartySize());
        resp.setScheduledAt(entity.getScheduledAt());

        resp.setStatus(entity.getStatus());
        resp.setCreatedAt(entity.getCreatedAt());

        return resp;
    }
}
