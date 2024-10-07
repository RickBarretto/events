package main.domain.models.evaluations;

import main.domain.models.events.EventId;
import main.domain.models.users.UserId;
import main.roles.Entity;

public class Evaluation implements Entity<EvaluationId> {
    private final EvaluationId id;
    private final EventId event;
    private final UserId author;
    private String comment;

    public Evaluation(EventId event, UserId author, String comment) {
        this(new EvaluationId(), event, author, comment);
    }

    public Evaluation(EvaluationId id, EventId event, UserId author,
            String comment) {
        this.id = id;
        this.author = author;
        this.event = event;
        this.comment = comment;
    }

    public EvaluationId id() { return id; }

    public UserId author() { return author; }

    public EventId event() { return event; }

    public String comment() { return comment; }

}
