package main.infra.virtualdb;

import java.util.Optional;
import java.util.HashMap;

import main.core.contexts.customers.CustomerRepository;
import main.core.models.users.Customer;
import main.core.models.users.types.CustomerID;
import main.core.models.users.types.Email;
import main.core.models.users.types.Username;

public class VirtualCustomerRepository 
implements CustomerRepository
{
    HashMap<CustomerID, Customer> customers;

    public VirtualCustomerRepository()
    {
        customers = new HashMap<>(); 
    }

    @Override
    public void register(Customer customer) 
    {
        assert !exists(customer.id()) : 
            "Must verify the existence beforehand";

        customers.put(customer.id(), customer);
    }

    @Override
    public Optional<Customer> by(Email email) 
    {
        return customers.values()
            .stream()
            .filter((customer) -> customer.account().email().equals(email))
            .findAny();
    }

    @Override
    public Optional<Customer> by(Username username) 
    {
        return customers.values()
            .stream()
            .filter((customer) -> customer.account().username().equals(username))
            .findAny();
    }

    @Override
    public Optional<Customer> by(CustomerID id) 
    {
        return Optional.ofNullable(customers.get(id));
    }

    @Override
    public boolean exists(Email email) 
    {
        return customers.values().stream().anyMatch(
            (customer) -> customer.account().email().equals(email)
        );
    }

    @Override
    public boolean exists(Username username) 
    {
        return customers.values().stream().anyMatch(
            (customer) -> customer.account().username().equals(username)
        );
    }

    @Override
    public boolean exists(CustomerID id) 
    {
        return customers.containsKey(id);
    }
}
