package main.core.contexts.users;

import main.core.contexts.users.subcontexts.AccountAvailability;
import main.core.models.users.Customer;
import main.core.models.users.types.Account;
import main.core.models.users.types.CustomerID;
import main.core.roles.UserRepository;

public class UserRegistering 
{
    private AccountAvailability<Customer, CustomerID> availabilityContext;
    private UserRepository<Customer, CustomerID> repository;
    private Account account;
    private boolean registered;

    public UserRegistering(UserRepository<Customer, CustomerID> repository, Account account)
    {
        this.availabilityContext = new AccountAvailability<>(repository, account);
        this.repository = repository;
        this.account = account;
        this.registered = false;
    }

    public void register()
    throws UserAlreadyRegistered
    {
        if (!availabilityContext.isAvailable())
            throw new UserAlreadyRegistered(availabilityContext.reason().get());
        repository.register(new Customer(new CustomerID(), account));
        registered = true;
    }

    public boolean hasRegistered()
    {
        return registered;
    }
}
