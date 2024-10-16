package main.domain.contexts.events;

import java.time.LocalDate;
import java.util.Objects;

import main.domain.exceptions.CantRegisterPastEvent;
import main.domain.exceptions.EventAlreadyRegistered;
import main.domain.exceptions.PermissionDenied;
import main.domain.models.events.Event;
import main.domain.models.events.Poster;
import main.domain.models.users.User;
import main.roles.Context;
import main.roles.repositories.Events;

public class EventRegistering implements Context {
    private Events repository;
    private Poster poster;
    private User author;
    private LocalDate currentDay;

    public EventRegistering into(Events repository) {
        this.repository = repository;
        return this;
    }

    public EventRegistering poster(Poster poster) {
        this.poster = poster;
        return this;
    }

    public EventRegistering by(User author) throws PermissionDenied {
        if (!author.isAdmin())
            throw new PermissionDenied("Author must be an Admin");
        this.author = author;
        return this;
    }

    public EventRegistering on(LocalDate currentDay) {
        this.currentDay = currentDay;
        return this;
    }

    public void register()
            throws EventAlreadyRegistered, CantRegisterPastEvent {
        Objects.requireNonNull(repository);
        Objects.requireNonNull(poster);
        Objects.requireNonNull(author);

        var event = new Event(poster);

        shouldBeFuture();
        shouldBeUnregistered();

        repository.register(event);
    }

    private void shouldBeFuture() throws CantRegisterPastEvent {
        if (currentDay.isAfter(poster.date())
                || currentDay.isEqual(poster.date()))
            throw new CantRegisterPastEvent();
    }

    private void shouldBeUnregistered() throws EventAlreadyRegistered {
        if (repository.has(poster.title(), poster.date()))
            throw new EventAlreadyRegistered();
    }

}
