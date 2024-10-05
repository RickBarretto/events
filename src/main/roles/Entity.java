package main.roles;

public interface Entity<Id extends EntityId> {
    public Id id();
}
