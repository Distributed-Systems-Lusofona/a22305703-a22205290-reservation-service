package pt.ulusofona.cd.store.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private String traceId(HttpServletRequest req) {
        String header = req.getHeader("X-Trace-Id");
        return (header != null && !header.isBlank()) ? header : UUID.randomUUID().toString();
    }

    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String errorCode, String message, String traceId) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", errorCode);
        body.put("message", message);
        body.put("traceId", traceId);
        return ResponseEntity.status(status).body(body);
    }

    // 404
    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleReservationNotFound(ReservationNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), traceId(req));
    }

    // 422: inválido (scheduledAt no passado, partySize <= 0, etc.)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "VALIDATION_ERROR", ex.getMessage(), traceId(req));
    }

    // 409: conflitos (slot indisponível / overlap / estado inválido, etc.)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "CONFLICT", ex.getMessage(), traceId(req));
    }

    // 400: erros de request genéricos (ex.: JSON malformado)
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleBadJson(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "Invalid request body", traceId(req));
    }

    // 500
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", ex.getMessage(), traceId(req));
    }
}
