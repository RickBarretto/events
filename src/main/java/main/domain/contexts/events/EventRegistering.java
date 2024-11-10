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

/**
 * Context for registering an event.
 */
public class EventRegistering implements Context {
    private Events repository;
    private Poster poster;
    private User author;
    private LocalDate currentDay;

    /**
     * Constructor with the specified events repository.
     *
     * @param events the repository of events used for registering
     */
    public EventRegistering(Events repository) {
        this.repository = repository;
    }

    /**
     * Sets the poster for the event.
     *
     * @param poster the event poster
     * @return the updated EventRegistering object
     */
    public EventRegistering poster(Poster poster) {
        this.poster = poster;
        return this;
    }

    /**
     * Sets the author of the event.
     *
     * @param author the author of the event
     * @return the updated EventRegistering object
     * @throws PermissionDenied if the author is not an admin
     */
    public EventRegistering by(User author) throws PermissionDenied {
        if (!author.isAdmin()) {
            throw new PermissionDenied("Author must be an Admin");
        }
        this.author = author;
        return this;
    }

    /**
     * Sets the current day for the context.
     *
     * @param currentDay the current day
     * @return the updated EventRegistering object
     */
    public EventRegistering on(LocalDate currentDay) {
        this.currentDay = currentDay;
        return this;
    }

    /**
     * Registers the event.
     *
     * @throws EventAlreadyRegistered if the event is already registered
     * @throws CantRegisterPastEvent  if the event is scheduled for a past date
     */
    public void register()
            throws EventAlreadyRegistered, CantRegisterPastEvent {
        Objects.requireNonNull(repository, "Repository cannot be null");
        Objects.requireNonNull(poster, "Poster cannot be null");
        Objects.requireNonNull(author, "Author cannot be null");

        var event = new Event(poster);
        shouldBeFuture();
        shouldBeUnregistered();
        repository.register(event);
    }

    /**
     * Checks if the event is scheduled for a future date.
     *
     * @throws CantRegisterPastEvent if the event is scheduled for a past or
     *                                   current date
     */
    private void shouldBeFuture() throws CantRegisterPastEvent {
        if (currentDay.isAfter(poster.date())
                || currentDay.isEqual(poster.date())) {
            throw new CantRegisterPastEvent();
        }
    }

    /**
     * Checks if the event is already registered.
     *
     * @throws EventAlreadyRegistered if the event is already registered
     */
    private void shouldBeUnregistered() throws EventAlreadyRegistered {
        if (repository.has(poster.title(), poster.date())) {
            throw new EventAlreadyRegistered();
        }
    }
}
