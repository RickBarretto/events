package main.core.contexts.users;

import java.util.Optional;

import main.core.contexts.Context;
import main.core.models.users.Customer;
import main.core.models.users.types.CustomerID;
import main.core.models.users.types.Email;
import main.core.roles.UserRepository;

public class CustomerSession 
extends Context<Optional<Customer>> 
{
    private UserRepository<Customer, CustomerID> repository;
    private Optional<Customer> customer;
    
    public boolean isLoggedIn()
    {
        return customer.isPresent();
    }
    
    public Optional<Customer> current()
    {
        return customer;
    }

    public void loginAs(Email email, String password)
    {
        var customer = repository.by(email);
        if (customer.isEmpty())
            return;

        if (customer.get().account().hasLogin(email.toString(), password))
            this.customer = customer;
    }

    public void logOut()
    {
        this.customer = Optional.empty();
    }
}
