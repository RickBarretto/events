package main.domain.contexts.events;

import java.time.LocalDate;
import java.util.Objects;

import main.domain.exceptions.PermissionDenied;
import main.domain.exceptions.TryingToEvaluateActiveEvent;
import main.domain.models.evaluations.Evaluation;
import main.domain.models.events.EventId;
import main.domain.models.users.UserId;
import main.roles.Context;
import main.roles.repositories.Events;

/**
 * Context for evaluating an event.
 */
public class EventEvaluation implements Context {
    private final LocalDate currentDay;
    private Events events;
    private EventId eventId;
    private UserId authorId;

    /**
     * Constructor with the specified events repository.
     *
     * @param events the repository of events used for evaluation
     */
    public EventEvaluation(Events events, LocalDate currentDay) {
        this.events = events;
        this.currentDay = currentDay;
    }

    /**
     * Sets the event ID for the evaluation.
     *
     * @param event the event ID
     * @return the updated EventEvaluation object
     */
    public EventEvaluation of(EventId event) {
        this.eventId = event;
        return this;
    }

    /**
     * Sets the author ID for the evaluation.
     *
     * @param author the author ID
     * @return the updated EventEvaluation object
     */
    public EventEvaluation by(UserId author) {
        this.authorId = author;
        return this;
    }

    /**
     * Submits the evaluation with the given comment.
     *
     * @param comment the evaluation comment
     * @throws NullPointerException if any of the required fields (events,
     *                                  eventId, authorId) are null
     */
    public void evaluateWith(String comment)
            throws TryingToEvaluateActiveEvent {
        Objects.requireNonNull(events, "Events repository cannot be null");
        Objects.requireNonNull(eventId, "Event ID cannot be null");
        Objects.requireNonNull(authorId, "Author ID cannot be null");

        var event = events.byId(eventId).get();

        if (event.isAvailableFor(currentDay))
            throw new TryingToEvaluateActiveEvent();

        event.receiveEvaluation(new Evaluation(eventId, authorId, comment));
        events.update(event);
    }
}
