package main.infra;

import java.util.Optional;

import main.domain.models.users.User;

public class Session {
    private Optional<User> loggedUser = Optional.empty();

    public void logInAs(User user) { this.loggedUser = Optional.of(user); }

    public void logOut() { this.loggedUser = Optional.empty(); }

    public boolean isActive() { return loggedUser.isPresent(); }

    public Optional<User> loggedUser() { return loggedUser; }

}
