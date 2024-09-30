package main.core.models.users.types;

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
}
