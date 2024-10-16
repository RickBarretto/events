package main.domain.contexts.events;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import main.domain.models.events.Event;
import main.roles.Context;
import main.roles.repositories.Events;

public class AvailableEventsListing implements Context {
    private LocalDate today;
    private Events events;

    public AvailableEventsListing beingToday(LocalDate today) {
        this.today = today;
        return this;
    }

    public AvailableEventsListing from(Events events) {
        this.events = events;
        return this;
    }

    public List<Event> availables() {
        Objects.requireNonNull(today);
        Objects.requireNonNull(events);

        return events.availableOn(today);
    }

}
