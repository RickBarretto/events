package main.roles.repositories;

import java.util.Optional;

import main.domain.models.users.User;
import main.domain.models.users.UserId;

public interface Users {
    void register(User user);

    void update(User target, User newUser);

    Optional<User> ownerOf(String email, String password);

    boolean has(UserId id);

    boolean has(String email);
}
