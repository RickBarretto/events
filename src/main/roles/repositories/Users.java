package main.roles.repositories;

import java.util.List;
import java.util.Optional;

import main.domain.models.users.User;

public interface Users {
    void register(User user);

    void update(User target, User newUser);

    Optional<User> ownerOf(String email, String password);

    boolean has(String email);

    List<User> list();
}
