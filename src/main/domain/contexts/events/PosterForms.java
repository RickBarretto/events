package main.domain.contexts.events;

import java.time.LocalDate;
import java.util.Objects;

import main.domain.models.events.Poster;

public class PosterForms {
    private String title;
    private String description;
    private LocalDate schedule;

    public PosterForms title(String title) {
        this.title = title;
        return this;
    }

    public PosterForms description(String description) {
        this.description = description;
        return this;
    }

    public PosterForms scheduledFor(LocalDate schedule) {
        this.schedule = schedule;
        return this;
    }

    public Poster submit() throws NullPointerException {
        Objects.requireNonNull(title);
        Objects.requireNonNull(description);
        Objects.requireNonNull(schedule);

        return new Poster(title, description, schedule);
    }

}
