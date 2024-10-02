package main.core.roles;

import java.util.Optional;

import main.core.models.users.types.Email;
import main.core.models.users.types.Username;

public interface UserRepository<User, UserID>
{
    void register(User user);
    boolean exists(UserID id);
    boolean exists(Email email);
    boolean exists(Username username);   
    Optional<User> by(UserID id);
    Optional<User> by(Email email);
    Optional<User> by(Username username); 
}
