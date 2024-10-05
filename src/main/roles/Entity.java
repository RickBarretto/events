package main.roles;

import java.util.Objects;

public abstract class Entity<Id extends EntityId> {
    Id id;

    public Entity(Id id) { this.id = id; }

    public Id id() { return id; }

    @Override
    public int hashCode() { return Objects.hash(id); }

    public boolean equals(Entity<Id> other) { return Objects.equals(id, other.id); }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Entity entity)
            return this.equals(entity);
        return false;
    }
}
