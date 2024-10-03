package main.infra.virtualdb;

import java.util.Optional;
import java.util.HashMap;

import main.core.models.users.types.Email;
import main.core.roles.AccountOwner;
import main.core.roles.EntityId;
import main.core.roles.UserRepository;

public final class VirtualUserRepository<T extends AccountOwner<ID>, ID extends EntityId>
implements UserRepository<T, ID>
{
    HashMap<ID, T> customers;

    public VirtualUserRepository()
    {
        customers = new HashMap<>(); 
    }

    @Override
    public void register(T customer) 
    {
        assert !exists(customer.id()) : "Must verify the existence beforehand";
        customers.put(customer.id(), customer);
    }

    @Override
    public Optional<T> by(Email email) 
    {
        return customers.values()
            .stream()
            .filter((customer) -> customer.account().email().equals(email))
            .findAny();
    }

    @Override
    public Optional<T> by(ID id) 
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
    public boolean exists(ID id) 
    {
        return customers.containsKey(id);
    }
}
