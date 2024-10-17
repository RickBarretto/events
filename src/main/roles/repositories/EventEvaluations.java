package main.roles.repositories;

import java.util.Optional;

import main.domain.models.evaluations.Evaluation;
import main.domain.models.events.EventId;

public interface EventEvaluations {
    void evaluate(Evaluation evaluation);

    Optional<Evaluation> ofEvent(EventId event);
}
