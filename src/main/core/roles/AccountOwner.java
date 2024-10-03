package main.core.roles;

import main.core.models.users.types.Account;

public interface AccountOwner<ID extends EntityId>
extends Entity<ID>
{
    Account account();
}
