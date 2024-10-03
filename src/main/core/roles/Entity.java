package main.core.roles;

public interface Entity<ID extends EntityId>
{
    ID id();
}
