

package main.core.models.users;

import main.core.models.users.types.Account;
import main.core.models.users.types.AdministratorID;
import main.core.roles.AccountOwner;

/**
 * Administrator
 */
public final class Administrator 
implements AccountOwner<AdministratorID>
{
    private AdministratorID id;
    private Account account;

    public Administrator(AdministratorID id, Account account)
    {
        this.id = id;
        this.account = account;
    }

    @Override
    public AdministratorID id() 
    {
        return id;
    }

    @Override
    public Account account()
    {
        return account;
    }
}