package main.core.models;

import java.util.Optional;

import main.core.roles.AccountOwner;
import main.core.roles.EntityId;

public class LocalSession<T extends AccountOwner<ID>, ID extends EntityId>
{
    private Optional<T> loggedUser;
    
    public LocalSession()
    {
        this.loggedUser = Optional.empty();
    }

    public void loginAs(T user)
    {
        this.loggedUser = Optional.of(user);
    }

    public void logOut()
    {
        this.loggedUser = Optional.empty();
    }

    public boolean isLogged()
    {
        return this.loggedUser.isPresent();
    }

    public Optional<T> loggedUser()
    {
        return loggedUser;
    }
    
}
