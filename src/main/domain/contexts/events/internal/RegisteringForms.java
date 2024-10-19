package main.domain.contexts.events.internal;

import java.time.LocalDate;
import java.util.Objects;

import main.domain.models.events.Poster;

public class RegisteringForms {
    private String title;
    private String description;
    private LocalDate schedule;

    public RegisteringForms title(String title) {
        this.title = title;
        return this;
    }

    public RegisteringForms description(String description) {
        this.description = description;
        return this;
    }

    public RegisteringForms scheduledFor(LocalDate schedule) {
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
