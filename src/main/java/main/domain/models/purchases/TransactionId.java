package main.domain.models.purchases;

import java.util.UUID;

import main.roles.EntityId;

public class TransactionId extends EntityId {
    public TransactionId(UUID id) { super(id); }

    public TransactionId() { super(); }
}
