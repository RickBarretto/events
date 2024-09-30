package main.core.contexts.customers;

import java.util.Optional;

import main.core.models.users.Customer;
import main.core.models.users.exceptions.EmailAlreadyExists;
import main.core.models.users.types.CustomerID;
import main.core.models.users.types.Email;
import main.core.models.users.types.Username;

public interface CustomerRepository 
{
    void register(Customer customer) throws EmailAlreadyExists;
    boolean exists(Email email);
    boolean exists(Username username);
    boolean exists(CustomerID id);
    Optional<Customer> by(Email email);
    Optional<Customer> by(Username username);
    Optional<Customer> by(CustomerID id);
}
