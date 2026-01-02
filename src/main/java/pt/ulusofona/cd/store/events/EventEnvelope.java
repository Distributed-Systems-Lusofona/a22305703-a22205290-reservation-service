package pt.ulusofona.cd.store.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventEnvelope<T> {
    private String eventType;
    private String status;
    private Instant occurredAt;
    private String traceId;
    private T payload;
}
