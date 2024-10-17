package main.domain.contexts.events;

import java.util.Objects;

import main.domain.models.evaluations.Evaluation;
import main.domain.models.events.EventId;
import main.domain.models.users.UserId;
import main.roles.Context;
import main.roles.repositories.Events;

public class EventEvaluation implements Context {
    private Events events;
    private EventId eventId;
    private UserId authorId;

    public EventEvaluation from(Events events) {
        this.events = events;
        return this;
    }

    public EventEvaluation of(EventId event) {
        this.eventId = event;
        return this;
    }

    public EventEvaluation by(UserId author) {
        this.authorId = author;
        return this;
    }

    public void evaluateWith(String comment) {
        Objects.requireNonNull(events);
        Objects.requireNonNull(eventId);
        Objects.requireNonNull(authorId);

        var event = events.byId(eventId).get();
        event.receiveEvaluation(new Evaluation(eventId, authorId, comment));
        events.update(event);
    }

}
