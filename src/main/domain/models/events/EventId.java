package main.domain.models.events;

import java.util.UUID;

import main.roles.EntityId;

public class EventId extends EntityId {
    public EventId(UUID id) { super(id); }

    public EventId() { super(); }
}