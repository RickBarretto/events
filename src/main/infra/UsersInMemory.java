package main.infra;

import java.util.HashMap;
import java.util.Optional;

import main.domain.models.users.User;
import main.domain.models.users.UserId;
import main.roles.UserRepository;

public class UsersInMemory implements UserRepository {
    private HashMap<UserId, User> users;
    private HashMap<String, UserId> emailIndex;

    public UsersInMemory() {
        this.users = new HashMap<>();
        this.emailIndex = new HashMap<>();
    }

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
    public Optional<User> ownerOf(String email, String password) {
        var id = emailIndex.get(email);
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public boolean has(UserId id) { return users.containsKey(id); }

    @Override
    public boolean has(String email) { return emailIndex.containsKey(email); }

    private void replaceEmail(String oldEmail, String newEmail) {
        assert emailIndex.containsKey(oldEmail);
        assert !emailIndex.containsKey(newEmail);

        emailIndex.put(newEmail, emailIndex.remove(oldEmail));
    }

    private void replaceUser(User newUser) {
        this.users.replace(newUser.id(), newUser);
    }

}
