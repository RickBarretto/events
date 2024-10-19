package main.domain.contexts.events.forms;

import java.time.LocalDate;
import java.util.Objects;

import main.domain.models.events.Poster;

public class EventInformation {
    private String title;
    private String description;
    private LocalDate schedule;

    public EventInformation title(String title) {
        this.title = title;
        return this;
    }

    public EventInformation description(String description) {
        this.description = description;
        return this;
    }

    public EventInformation scheduledFor(LocalDate schedule) {
        this.schedule = schedule;
        return this;
    }

    public Poster submit() {
        Objects.requireNonNull(title);
        Objects.requireNonNull(description);
        Objects.requireNonNull(schedule);

        return new Poster(title, description, schedule);
    }

}
