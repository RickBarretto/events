package main.infra.virtual;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import main.domain.models.users.Login;
import main.domain.models.users.User;
import main.domain.models.users.UserId;
import main.roles.repositories.Users;

/**
 * In-memory implementation of the Users repository.
 */
public class UsersInMemory implements Users {
    private HashMap<UserId, User> users = new HashMap<>();
    private HashMap<String, UserId> emailIndex = new HashMap<>();

    /**
     * Constructs a new UsersInMemory with an empty list of users.
     */
    public UsersInMemory() { this(List.of()); }

    /**
     * Constructs a new UsersInMemory with a list of users.
     *
     * @param usersList the list of users
     */
    public UsersInMemory(List<User> usersList) {
        usersList.forEach((user) -> register(user));
    }

    /**
     * Returns a list of all users.
     *
     * @return a list of users
     */
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
        var user = this.byId(id);
        if (!user.isPresent())
            return user;
        if (user.get().isOwnerOf(new Login(email, password))) {
            return user;
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public boolean has(String email) { return emailIndex.containsKey(email); }

    /**
     * Replaces an existing email with a new email.
     *
     * @param oldEmail the old email address
     * @param newEmail the new email address
     */
    private void replaceEmail(String oldEmail, String newEmail) {
        assert emailIndex.containsKey(oldEmail);
        if (!oldEmail.equals(newEmail)) {
            assert !emailIndex.containsKey(newEmail);
        }
        emailIndex.put(newEmail, emailIndex.remove(oldEmail));
    }

    /**
     * Replaces an existing user with a new user.
     *
     * @param newUser the new user details
     */
    private void replaceUser(User newUser) {
        this.users.replace(newUser.id(), newUser);
    }
}
