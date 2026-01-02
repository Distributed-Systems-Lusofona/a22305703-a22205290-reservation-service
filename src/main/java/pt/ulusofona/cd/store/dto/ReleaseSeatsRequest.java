package pt.ulusofona.cd.store.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReleaseSeatsRequest {
    private Long availabilitySlotId;
    private int partySize;
}
