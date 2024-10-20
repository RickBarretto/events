package main.roles.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import main.domain.models.events.Event;
import main.domain.models.events.EventId;

public interface Events {
    void register(Event event);

    void update(Event event);

    Optional<Event> byId(EventId id);

    Optional<Event> event(String title, LocalDate date);

    List<Event> list();

    List<Event> availableOn(LocalDate date);

    boolean has(EventId id);

    boolean has(String title, LocalDate inDate);

}
