package pt.ulusofona.cd.store.dto;

import lombok.Getter;
import lombok.Setter;
import pt.ulusofona.cd.store.model.ReservationStatus;

import java.time.Instant;

@Getter
@Setter
public class ReservationResponse {

    private Long id;

    private Long restaurantId;
    private Long availabilitySlotId;

    private String customerName;
    private String customerEmail;

    private int partySize;
    private Instant scheduledAt;

    private ReservationStatus status;

    private Instant createdAt;
}
