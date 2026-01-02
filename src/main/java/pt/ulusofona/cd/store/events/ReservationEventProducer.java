package pt.ulusofona.cd.store.events;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pt.ulusofona.cd.store.model.Reservation;

import java.time.Instant;
import java.util.UUID;

@Service
public class ReservationEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.reservation-created}")
    private String reservationCreatedTopic;

    @Value("${kafka.topics.reservation-confirmed}")
    private String reservationConfirmedTopic;

    @Value("${kafka.topics.reservation-cancelled}")
    private String reservationCancelledTopic;

    public ReservationEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishReservationCreated(Reservation r, String traceId) {
        publish(reservationCreatedTopic, "reservation.created", r, traceId);
    }

    public void publishReservationConfirmed(Reservation r, String traceId) {
        publish(reservationConfirmedTopic, "reservation.confirmed", r, traceId);
    }

    public void publishReservationCancelled(Reservation r, String traceId) {
        publish(reservationCancelledTopic, "reservation.cancelled", r, traceId);
    }

    private void publish(String topic, String eventType, Reservation r, String traceId) {

        String tid = (traceId != null && !traceId.isBlank()) ? traceId : UUID.randomUUID().toString();

        ReservationEventPayload payload = new ReservationEventPayload(
                r.getId(),
                r.getRestaurantId(),
                r.getCustomerName(),
                r.getCustomerEmail(),
                r.getPartySize(),
                r.getScheduledAt()
        );

        EventEnvelope<ReservationEventPayload> envelope = new EventEnvelope<>(
                eventType,
                r.getStatus().name(),
                Instant.now(),
                tid,
                payload
        );

        kafkaTemplate.send(topic, String.valueOf(r.getId()), envelope);
    }
}
