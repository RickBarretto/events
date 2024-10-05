package main.infra;

import java.util.HashMap;
import java.util.Optional;

import main.domain.models.users.User;
import main.domain.models.users.UserId;
import main.roles.UserRepository;

public class VirtualUserRepository implements UserRepository {
    private HashMap<UserId, User> users;
    private HashMap<String, UserId> emailIndex;

    public VirtualUserRepository() {
        this.users = new HashMap<>();
        this.emailIndex = new HashMap<>();
    }

    @Override
    public void register(User user) {
        this.users.put(user.id(), user);
        this.emailIndex.put(user.login().email(), user.id());
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

}
