package main.core.models.users;

import main.core.models.users.types.Account;
import main.core.models.users.types.UserID;

public abstract class User 
{
    private UserID id;
    private Account account;

    public UserID id() 
    {
        return id;
    }

    public Account account()
    {
        return account;
    }
}
