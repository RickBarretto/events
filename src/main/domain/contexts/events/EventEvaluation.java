package main.domain.contexts.events;

import java.util.Objects;

import main.domain.models.events.Event;
import main.domain.models.users.User;
import main.roles.Context;
import main.roles.repositories.Evaluations;

public class EventEvaluation implements Context {
    private Evaluations evaluations;
    private Event event;
    private User author;

    public EventEvaluation from(Evaluations evaluations) {
        this.evaluations = evaluations;
        return this;
    }

    public EventEvaluation of(Event event) {
        this.event = event;
        return this;
    }

    public EventEvaluation by(User author) {
        this.author = author;
        return this;
    }

    public void evaluateWith(String comment) {
        Objects.requireNonNull(evaluations);
        Objects.requireNonNull(event);
        Objects.requireNonNull(author);

        evaluations.write(event.id(), author.id(), comment);
    }

}
