package main.roles.repositories;

import java.util.Optional;

import main.domain.models.evaluations.Evaluation;
import main.domain.models.events.EventId;
import main.domain.models.users.UserId;

public interface Evaluations {
    void write(EventId event, UserId author, String comment);

    Optional<Evaluation> wroteBy(UserId author);
}
