package main.core.roles;

import java.util.Optional;

import main.core.models.users.types.Email;
import main.core.models.users.types.Username;

public interface UserRepository<USER_CLASS, USER_ID>
{
    void register(USER_CLASS user);
    boolean exists(USER_ID id);
    boolean exists(Email email);
    boolean exists(Username username);   
    Optional<USER_CLASS> by(USER_ID id);
    Optional<USER_CLASS> by(Email email);
    Optional<USER_CLASS> by(Username username); 
}
