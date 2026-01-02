package pt.ulusofona.cd.store.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEventPayload {
    private Long reservationId;
    private Long restaurantId;
    private String customerName;
    private String customerEmail;
    private int partySize;
    private Instant scheduledAt;
}
