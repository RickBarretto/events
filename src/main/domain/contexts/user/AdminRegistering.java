package main.domain.contexts.user;

import java.util.Objects;

import main.domain.exceptions.EmailAlreadyExists;
import main.domain.models.users.Login;
import main.domain.models.users.Person;
import main.domain.models.users.User;
import main.roles.Context;
import main.roles.repositories.Users;

public class AdminRegistering extends UserRegistering implements Context {
    private AccountInformation account = new AccountInformation();
    private Users repository;

    public AdminRegistering(Users repository) {
        super(repository);
        this.repository = repository;
    }

    public AdminRegistering login(Login login) {
        account.login = login;
        return this;
    }

    public AdminRegistering person(Person person) {
        account.person = person;
        return this;
    }

    @Override
    public void register() throws EmailAlreadyExists {
        account.shouldBeInitialized();
        this.emailShouldBeUnregistered();

        var user = new User(account.login, account.person).asAdmin();
        this.repository.register(user);
    }

    protected void emailShouldBeUnregistered() throws EmailAlreadyExists {
        if (this.repository.has(account.login.email()))
            throw new EmailAlreadyExists();
    }

}
