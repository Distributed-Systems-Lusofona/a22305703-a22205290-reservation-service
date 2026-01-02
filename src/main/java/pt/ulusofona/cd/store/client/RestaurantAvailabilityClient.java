package pt.ulusofona.cd.store.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import pt.ulusofona.cd.store.dto.AvailabilitySlotDto;

import java.util.List;

@FeignClient(name = "restaurant-service", url = "${restaurant.service.url}")
public interface RestaurantAvailabilityClient {

    @GetMapping("/api/v1/restaurants/{id}/availability")
    List<AvailabilitySlotDto> getAvailability(
            @PathVariable("id") String id,
            @RequestParam("date") String date,
            @RequestParam("partySize") int partySize
    );
}
