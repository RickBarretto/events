package main.core.models.users;

import main.core.models.users.types.Account;

public sealed interface User<ID>
permits Customer, Administrator
{
    public ID id();
    public Account account();
}
