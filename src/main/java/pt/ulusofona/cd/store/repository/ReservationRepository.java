package pt.ulusofona.cd.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ulusofona.cd.store.model.Reservation;
import pt.ulusofona.cd.store.model.ReservationStatus;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByRestaurantId(Long restaurantId);

    List<Reservation> findByRestaurantIdAndStatus(Long restaurantId, ReservationStatus status);

    List<Reservation> findByStatus(ReservationStatus status);
}
