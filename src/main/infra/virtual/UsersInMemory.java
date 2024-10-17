package main.infra.virtual;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import main.domain.models.users.User;
import main.domain.models.users.UserId;
import main.roles.repositories.Users;

public class UsersInMemory implements Users {
    private HashMap<UserId, User> users = new HashMap<>();
    private HashMap<String, UserId> emailIndex = new HashMap<>();

    public UsersInMemory() { this(List.of()); }

    public UsersInMemory(List<User> usersList) {
        usersList.forEach((user) -> register(user));
    }

    public List<User> list() { return List.copyOf(users.values()); }

    @Override
    public void register(User user) {
        assert !users.containsKey(user.id()) : "User must not exist";
        assert !emailIndex.containsKey(user.login().email())
                : "Email must not be registered";

        this.users.put(user.id(), user);
        this.emailIndex.put(user.login().email(), user.id());
    }

    @Override
    public void update(User target, User newUser) {
        assert target.id() == newUser.id() : "ID should be the same";
        this.replaceEmail(target.login().email(), newUser.login().email());
        this.replaceUser(newUser);
    }

    @Override
    public Optional<User> byId(UserId id) {
        return Optional.ofNullable(users.get(id)); 
    }

    @Override
    public Optional<User> ownerOf(String email, String password) {
        var id = emailIndex.get(email);
        return this.byId(id);
    }

    @Override
    public boolean has(String email) { return emailIndex.containsKey(email); }

    private void replaceEmail(String oldEmail, String newEmail) {
        assert emailIndex.containsKey(oldEmail);
        if (!oldEmail.equals(newEmail)) {
            assert !emailIndex.containsKey(newEmail);
        }

        emailIndex.put(newEmail, emailIndex.remove(oldEmail));
    }

    private void replaceUser(User newUser) {
        this.users.replace(newUser.id(), newUser);
    }

}
