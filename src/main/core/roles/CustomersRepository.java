package main.core.roles;

import main.core.models.users.Customer;
import main.core.models.users.types.CustomerID;

public interface CustomersRepository
extends UserRepository<Customer, CustomerID>
{    
}
