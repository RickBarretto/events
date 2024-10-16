package main.domain.contexts.user;

import java.util.Objects;

import main.domain.exceptions.EmailAlreadyExists;
import main.domain.models.users.Login;
import main.domain.models.users.Person;
import main.domain.models.users.User;
import main.roles.Context;
import main.roles.repositories.Users;

/**
 * Allows registering an User into a Repository
 */
public class UserRegistering implements Context {
    private Users repository;
    private Login login;
    private Person person;

    public UserRegistering into(Users repository) {
        this.repository = repository;
        return this;
    }

    public UserRegistering login(Login login) {
        this.login = login;
        return this;
    }

    public UserRegistering person(Person person) {
        this.person = person;
        return this;
    }

    public void register() throws EmailAlreadyExists {
        Objects.requireNonNull(this.repository);
        Objects.requireNonNull(this.login);
        Objects.requireNonNull(this.person);

        var user = new User(login, person);

        if (this.repository.has(login.email()))
            throw new EmailAlreadyExists();
        this.repository.register(user);
    }
}