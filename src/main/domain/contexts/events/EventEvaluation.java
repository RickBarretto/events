package main.domain.contexts.events;

import java.util.Objects;
import main.domain.models.evaluations.Evaluation;
import main.domain.models.events.EventId;
import main.domain.models.users.UserId;
import main.roles.Context;
import main.roles.repositories.Events;

/**
 * Context for evaluating an event.
 */
public class EventEvaluation implements Context {
    private Events events;
    private EventId eventId;
    private UserId authorId;

    /**
     * Sets the events repository for the context.
     *
     * @param events the events repository
     * @return the updated EventEvaluation object
     */
    public EventEvaluation from(Events events) {
        this.events = events;
        return this;
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
    public void evaluateWith(String comment) {
        Objects.requireNonNull(events, "Events repository cannot be null");
        Objects.requireNonNull(eventId, "Event ID cannot be null");
        Objects.requireNonNull(authorId, "Author ID cannot be null");

        var event = events.byId(eventId).get();
        event.receiveEvaluation(new Evaluation(eventId, authorId, comment));
        events.update(event);
    }
}
