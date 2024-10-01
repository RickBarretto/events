package main.core.roles;

import main.core.models.users.Administrator;
import main.core.models.users.types.AdministratorID;

public interface AdministratorsRepository 
extends UserRepository<Administrator, AdministratorID>
{
}
