package main.domain.contexts.user;

import java.util.Objects;

import main.domain.models.users.Login;
import main.domain.models.users.Person;
import main.domain.models.users.User;
import main.roles.repositories.Users;

public class AdminRegistering extends UserRegistering {
    private Users repository;
    private Login login;
    private Person person;

    @Override
    public void register() {
        Objects.requireNonNull(this.repository);
        Objects.requireNonNull(this.login);
        Objects.requireNonNull(this.person);

        var user = new User(login, person).asAdmin();

        this.repository.register(user);
    }
}
