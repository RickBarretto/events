package main.core.contexts.users;

import java.util.Optional;

import main.core.contexts.Context;
import main.core.models.LocalSession;
import main.core.models.users.Customer;
import main.core.models.users.types.CustomerID;
import main.core.models.users.types.Email;
import main.core.roles.UserRepository;

public class UserSession
extends Context<Optional<Customer>> 
{
    private LocalSession<Customer, CustomerID> session;
    private UserRepository<Customer, CustomerID> repository;

    public UserSession(LocalSession<Customer, CustomerID> session, UserRepository<Customer, CustomerID> repository)
    {
        this.session = session;
        this.repository = repository;
    }
    
    public boolean isLoggedIn()
    {
        return session.isLogged();
    }
    
    public Optional<Customer> current()
    {
        return session.loggedUser();
    }

    public void loginAs(Email email, String password)
    {
        repository.by(email).ifPresent(customer -> {
            if (customer.account().hasLogin(email, password))
                session.loginAs(customer);
        });
    }

    public void logOut()
    {
        session.logOut();
    }
}
