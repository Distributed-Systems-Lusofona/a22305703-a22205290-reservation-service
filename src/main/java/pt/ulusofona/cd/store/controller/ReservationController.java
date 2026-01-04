package pt.ulusofona.cd.store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ulusofona.cd.store.dto.ReservationCreateRequest;
import pt.ulusofona.cd.store.dto.ReservationResponse;
import pt.ulusofona.cd.store.mapper.ReservationMapper;
import pt.ulusofona.cd.store.model.Reservation;
import pt.ulusofona.cd.store.service.ReservationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    private String traceIdOrNew(String header) {
        return (header != null && !header.isBlank()) ? header : UUID.randomUUID().toString();
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestHeader(value = "X-Trace-Id", required = false) String traceIdHeader,
            @Valid @RequestBody ReservationCreateRequest request
    ) {
        String traceId = traceIdOrNew(traceIdHeader);
        Reservation created = reservationService.createReservation(request, traceId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ReservationMapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(ReservationMapper.toResponse(reservation));
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        List<ReservationResponse> responseList = reservations.stream()
                .map(ReservationMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<ReservationResponse> confirmReservation(
            @RequestHeader(value = "X-Trace-Id", required = false) String traceIdHeader,
            @PathVariable Long id
    ) {
        String traceId = traceIdOrNew(traceIdHeader);
        Reservation confirmed = reservationService.confirmReservation(id, traceId);
        return ResponseEntity.ok(ReservationMapper.toResponse(confirmed));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ReservationResponse> cancelReservation(
            @RequestHeader(value = "X-Trace-Id", required = false) String traceIdHeader,
            @PathVariable Long id
    ) {
        String traceId = traceIdOrNew(traceIdHeader);
        Reservation cancelled = reservationService.cancelReservation(id, traceId);
        return ResponseEntity.ok(ReservationMapper.toResponse(cancelled));
    }
}
