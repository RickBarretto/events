package main.core.roles;

import java.util.Optional;

import main.core.models.users.types.Email;

public interface UserRepository<T extends AccountOwner<ID>, ID extends EntityId>
{
    void register(T user);
    boolean exists(ID id);
    boolean exists(Email email);  
    Optional<T> by(ID id);
    Optional<T> by(Email email);
}
