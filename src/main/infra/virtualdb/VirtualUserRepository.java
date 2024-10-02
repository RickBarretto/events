package main.infra.virtualdb;

import java.util.Optional;
import java.util.HashMap;

import main.core.models.users.types.Email;
import main.core.models.users.types.Username;
import main.core.roles.UserRepository;

public final class VirtualUserRepository<User extends main.core.models.users.User<UserID>, UserID>
implements UserRepository<User, UserID>
{
    HashMap<UserID, User> customers;

    public VirtualUserRepository()
    {
        customers = new HashMap<>(); 
    }

    @Override
    public void register(User customer) 
    {
        assert !exists(customer.id()) : 
            "Must verify the existence beforehand";

        customers.put(customer.id(), customer);
    }

    @Override
    public Optional<User> by(Email email) 
    {
        return customers.values()
            .stream()
            .filter((customer) -> customer.account().email().equals(email))
            .findAny();
    }

    @Override
    public Optional<User> by(Username username) 
    {
        return customers.values()
            .stream()
            .filter((customer) -> customer.account().username().equals(username))
            .findAny();
    }

    @Override
    public Optional<User> by(UserID id) 
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
    public boolean exists(UserID id) 
    {
        return customers.containsKey(id);
    }
}
