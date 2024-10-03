package main.core.contexts.users.subcontexts;

import java.util.Optional;

import main.core.contexts.Context;
import main.core.models.users.types.Account;
import main.core.roles.AccountOwner;
import main.core.roles.EntityId;
import main.core.roles.UserRepository;

public final class AccountAvailability<T extends AccountOwner<ID>, ID extends EntityId>
extends Context<Account> 
{
    private UserRepository<T, ID> repository;
    private Account account;
    private boolean unavailableEmail;

    public AccountAvailability(UserRepository<T, ID> repository, Account account)
    {
        this.repository = repository;
        this.account = account;
    }

    private void updateAvailability()
    {
        unavailableEmail = repository.exists(account.email());
    }

    public boolean isAvailable()
    {
        updateAvailability();
        return !(unavailableEmail);
    }

    public Optional<String> reason()
    {
        updateAvailability();
        if (this.isAvailable()) return Optional.empty();
        return Optional.of("Email is already registered");
    }
}
