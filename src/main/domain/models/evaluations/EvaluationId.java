package main.domain.models.evaluations;

import java.util.UUID;

import main.roles.EntityId;

public class EvaluationId extends EntityId {
    public EvaluationId(UUID id) { super(id); }

    public EvaluationId() { super(); }
}
