package main.roles;

import java.time.LocalDate;
import java.util.Optional;

import main.domain.models.events.Event;
import main.domain.models.events.EventId;

public interface EventRepository {
    void register(Event event);

    Optional<Event> event(String title, LocalDate date);

    boolean has(EventId id);

    boolean has(String title, LocalDate inDate);

}
