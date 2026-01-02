package pt.ulusofona.cd.store.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    @NotNull
    @Column(name = "availability_slot_id", nullable = false)
    private Long availabilitySlotId;

    @NotBlank
    @Column(name = "customer_name", nullable = false, length = 120)
    private String customerName;

    @NotBlank
    @Email
    @Column(name = "customer_email", nullable = false, length = 255)
    private String customerEmail;

    @Min(1)
    @Column(name = "party_size", nullable = false)
    private int partySize;

    @NotNull
    @Column(name = "scheduled_at", nullable = false)
    private Instant scheduledAt;
    //perigoso tenho que avisar a Jéssica depois

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReservationStatus status = ReservationStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        if (status == null) status = ReservationStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
