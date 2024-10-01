package main.core.models;

import java.util.Objects;
import java.util.UUID;

public abstract class EntityId 
{
    private UUID value;

    public EntityId()
    {
        this.value = UUID.randomUUID();
    }

    public UUID value()
    {
        return value;
    }

    public int hashCode() {
        return Objects.hash(value);
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof EntityId))
            return false;
        var other = (EntityId) obj;
        return Objects.equals(value, other.value);
    }
}
