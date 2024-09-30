package main.core.models;

public interface Entity 
{
    <ID extends EntityId> ID id();
}
