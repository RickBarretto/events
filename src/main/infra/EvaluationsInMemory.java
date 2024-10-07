package main.infra;

import java.util.HashMap;
import java.util.Optional;

import main.domain.models.evaluations.Evaluation;
import main.domain.models.evaluations.EvaluationId;
import main.domain.models.events.EventId;
import main.domain.models.users.UserId;
import main.roles.repositories.Evaluations;

public class EvaluationsInMemory implements Evaluations {
    private HashMap<EvaluationId, Evaluation> evaluations;
    private HashMap<UserId, EvaluationId> evaluationsByUser;

    public EvaluationsInMemory() {
        this.evaluations = new HashMap<>();
        this.evaluationsByUser = new HashMap<>();
    }

    @Override
    public void write(EventId event, UserId author, String comment) {
        var evaluation = new Evaluation(event, author, comment);
        this.evaluations.put(evaluation.id(), evaluation);
        this.evaluationsByUser.put(author, evaluation.id());
    }

    @Override
    public Optional<Evaluation> wroteBy(UserId author) {
        if (evaluationsByUser.get(author) instanceof EvaluationId id) {
            return Optional.ofNullable(evaluations.get(id));
        }
        return Optional.empty();
    }

}
