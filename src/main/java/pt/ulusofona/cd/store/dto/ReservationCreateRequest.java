package pt.ulusofona.cd.store.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ReservationCreateRequest {

    @NotNull(message = "restaurantId is required")
    private Long restaurantId;

    @NotNull(message = "availabilitySlotId is required")
    private Long availabilitySlotId;

    @NotBlank(message = "customerName is required")
    @Size(min = 2, max = 120, message = "customerName must be between 2 and 120 characters")
    private String customerName;

    @NotBlank(message = "customerEmail is required")
    @Email(message = "customerEmail must be a valid email")
    @Size(max = 255, message = "customerEmail must be at most 255 characters long")
    private String customerEmail;

    @Min(value = 1, message = "partySize must be greater than zero")
    private int partySize;
//também se conseguissemos meter menos que 0 era obra xD

    @NotNull(message = "scheduledAt is required")
    private Instant scheduledAt;
}
//AQUI DAVA MESMO JEITO QUE ISTO FUNCIONASSE, JÁ CHEGA DE PERDER TEMPO POR CAUSA DISTO (A JÉSSICA PASSOU POR AQUI)