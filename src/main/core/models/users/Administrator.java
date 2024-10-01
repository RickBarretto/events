

package main.core.models.users;

import main.core.models.users.types.Account;
import main.core.models.users.types.AdministratorID;

/**
 * Administrator
 */
public class Administrator 
{
    private AdministratorID id;
    private Account account;

    public Administrator(AdministratorID id, Account account)
    {
        this.id = id;
        this.account = account;
    }

    public AdministratorID id() 
    {
        return id;
    }

    public Account account()
    {
        return account;
    }
}