package main.roles;

import java.util.Optional;

import main.domain.models.events.Event;
import main.domain.models.events.EventId;

public interface EventRepository {
    void register(Event event);

    Optional<Event> eventByTitle(String title);

    Optional<Event> eventById(EventId id);

    boolean has(EventId id);
    boolean has(String title);
    
}
