package pt.ulusofona.cd.store.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pt.ulusofona.cd.store.dto.ReleaseSeatsRequest;

@FeignClient(
        name = "restaurant-service",
        contextId = "restaurantCapacityClient",
        url = "${restaurant.service.url}"
)
public interface RestaurantCapacityClient {

    @PostMapping("/api/restaurants/{id}/availability/release")
    void releaseSeats(
            @PathVariable("id") String restaurantId,
            @RequestBody ReleaseSeatsRequest req
    );
}
