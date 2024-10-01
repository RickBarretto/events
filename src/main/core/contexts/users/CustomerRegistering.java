package main.core.contexts.users;

import main.core.contexts.Context;
import main.core.models.users.Customer;
import main.core.models.users.types.Account;
import main.core.models.users.types.CustomerID;
import main.core.roles.CustomersRepository;

public class CustomerRegistering
extends Context<Account>
{
    private CustomersRepository repository;
    private Account account;
    private boolean registered;

    public CustomerRegistering(CustomersRepository repository, Account account)
    {
        this.repository = repository;
        this.account = account;
        this.registered = false;
    }

    public void register() 
    throws UserAlreadyRegistered
    {
        if (this.repository.exists(account.email()))
            this.notifyExistenceFor("Email");
        if (this.repository.exists(account.username()))
            this.notifyExistenceFor("Username");

        var customer = new Customer(new CustomerID(), this.account);
        this.repository.register(customer);
        this.registered = true;
    }

    private void notifyExistenceFor(String paramName)
    throws UserAlreadyRegistered
    {
        throw new UserAlreadyRegistered(paramName + " is already registered");
    }

    public boolean hasRegistered()
    {
        return registered;
    }
}
