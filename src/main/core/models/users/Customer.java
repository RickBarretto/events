package main.core.models.users;

import main.core.models.users.types.Account;
import main.core.models.users.types.CustomerID;
import main.core.roles.AccountOwner;

public final class Customer
implements AccountOwner<CustomerID>
{
    private CustomerID id;
    private Account account;

    public Customer(CustomerID id, Account account)
    {
        this.id = id;
        this.account = account;
    }
    
    public Customer(Account account)
    {
        this(new CustomerID(), account);
    }

    @Override
    public CustomerID id() 
    {
        return id;
    }

    @Override
    public Account account()
    {
        return account;
    }
}
