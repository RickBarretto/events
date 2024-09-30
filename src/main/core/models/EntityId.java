package main.core.models;

import java.util.UUID;

public interface EntityId 
{
    UUID value();
    int hashCode();
    boolean equals(Object obj);
}
