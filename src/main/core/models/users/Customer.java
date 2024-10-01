package main.core.models.users;

import main.core.models.users.types.Account;
import main.core.models.users.types.CustomerID;

public class Customer
{
    private CustomerID id;
    private Account account;

    public Customer(CustomerID id, Account account)
    {
        this.id = id;
        this.account = account;
    }

    public CustomerID id() 
    {
        return id;
    }

    public Account account()
    {
        return account;
    }
}
