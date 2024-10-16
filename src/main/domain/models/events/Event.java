package main.domain.models.events;

import java.time.LocalDate;

import main.roles.Entity;

public class Event implements Entity<EventId> {
    private final EventId id;
    private Poster poster;

    public Event(Poster poster) {
        this.id = new EventId();
        this.poster = poster;
    }

    public boolean isAvailableFor(LocalDate date) {
        var schedule = poster.date();
        return !schedule.isBefore(date);
    }

    public EventId id() { return id; }

    public Poster poster() { return poster; }

}
