package main.core.contexts.users.subcontexts;

import java.util.Optional;

import main.core.contexts.Context;
import main.core.models.users.types.Account;
import main.core.roles.UserRepository;

public final class AccountAvailability<User, UserID>
extends Context<Account> 
{
    private UserRepository<User, UserID> repository;
    private Account account;
    private boolean unavailableEmail;
    private boolean unavailableUsername;

    public AccountAvailability(UserRepository<User, UserID> repository, Account account)
    {
        this.repository = repository;
        this.account = account;
    }

    private void updateAvailability()
    {
        unavailableEmail = repository.exists(account.email());
        unavailableUsername = repository.exists(account.username());
    }

    public boolean isAvailable()
    {
        updateAvailability();
        return !(unavailableEmail || unavailableUsername);
    }

    public Optional<String> reason()
    {
        updateAvailability();

        String reason;
        if (unavailableEmail && unavailableUsername)
            reason = "Email and Username are";
        else if (unavailableEmail)
            reason = "Email is";
        else if (unavailableUsername)
            reason = "Username is";
        else
            return Optional.empty();

        return Optional.of(reason + " already registered");
    }
}
