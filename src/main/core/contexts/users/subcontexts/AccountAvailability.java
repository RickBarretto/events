package main.core.contexts.users.subcontexts;

import java.util.Optional;

import main.core.contexts.Context;
import main.core.models.users.Customer;
import main.core.models.users.types.Account;
import main.core.models.users.types.CustomerID;
import main.core.roles.UserRepository;

public class AccountAvailability
extends Context<Account> 
{
    private UserRepository<Customer, CustomerID> repository;
    private Account account;
    private boolean unavailableEmail;
    private boolean unavailableUsername;

    public AccountAvailability(UserRepository<Customer, CustomerID> repository, Account account)
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
