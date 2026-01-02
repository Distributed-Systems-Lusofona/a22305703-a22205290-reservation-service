package pt.ulusofona.cd.store.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import pt.ulusofona.cd.store.dto.ReleaseSeatsRequest;

@FeignClient(name = "restaurant-service", url = "${restaurant.service.url}")
public interface RestaurantCapacityClient {

    @PostMapping("/api/v1/restaurants/{id}/availability/release")
    void releaseSeats(
            @PathVariable("id") String id,
            @RequestBody ReleaseSeatsRequest req
    );
}
