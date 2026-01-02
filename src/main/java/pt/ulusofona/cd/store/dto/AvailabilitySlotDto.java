package pt.ulusofona.cd.store.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AvailabilitySlotDto {
    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private String endTime;
    private int capacity;
    private int seatsAvailable;
}
