package main.core.models.users.types;

import java.util.Objects;
import java.util.UUID;

import main.core.models.EntityId;

public class CustomerID 
implements EntityId
{
    private UUID value;

    public CustomerID()
    {
        this.value = UUID.randomUUID();
    }

    @Override
    public UUID value()
    {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof CustomerID))
            return false;
        CustomerID other = (CustomerID) obj;
        return Objects.equals(value, other.value);
    }
}
