package main.domain.contexts.user;

import java.util.Objects;

import main.domain.exceptions.EmailAlreadyExists;
import main.domain.models.users.Login;
import main.domain.models.users.Person;
import main.domain.models.users.User;
import main.roles.Context;
import main.roles.repositories.Users;

class AccountInformation {
    public Login login;
    public Person person;

    public void shouldBeInitialized() {
        Objects.requireNonNull(login);
        Objects.requireNonNull(person);
    }
}

/**
 * Allows registering an User into a Repository
 */
public class UserRegistering implements Context {
    private AccountInformation account = new AccountInformation();
    private Users repository;

    public UserRegistering(Users repository) {
        this.repository = repository;
    }

    public UserRegistering login(Login login) {
        account.login = login;
        return this;
    }

    public UserRegistering person(Person person) {
        account.person = person;
        return this;
    }

    public void register() throws EmailAlreadyExists {;
        account.shouldBeInitialized();
        emailShouldBeUnregistered();
        this.repository.register(new User(account.login, account.person));
    }

    private void emailShouldBeUnregistered() throws EmailAlreadyExists {
        if (this.repository.has(account.login.email()))
            throw new EmailAlreadyExists();
    }
}