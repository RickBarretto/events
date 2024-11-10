package main.domain.contexts.user;

import main.domain.exceptions.EmailAlreadyExists;
import main.domain.models.users.Login;
import main.domain.models.users.Person;
import main.domain.models.users.User;
import main.roles.Context;
import main.roles.repositories.Users;

/**
 * Context for registering an admin user.
 */
public class AdminRegistering extends UserRegistering implements Context {
    private AccountInformation account = new AccountInformation();
    private Users repository;

    /**
     * Constructor that initializes the repository.
     *
     * @param repository the users repository
     */
    public AdminRegistering(Users repository) {
        super(repository);
        this.repository = repository;
    }

    /**
     * Sets the login information for the admin user.
     *
     * @param login the login information
     * @return the updated AdminRegistering object
     */
    public AdminRegistering login(Login login) {
        account.login = login;
        return this;
    }

    /**
     * Sets the personal information for the admin user.
     *
     * @param person the personal information
     * @return the updated AdminRegistering object
     */
    public AdminRegistering person(Person person) {
        account.person = person;
        return this;
    }

    /**
     * Registers the admin user.
     *
     * @throws EmailAlreadyExists if the email is already registered
     */
    @Override
    public void register() throws EmailAlreadyExists {
        account.shouldBeInitialized();
        this.emailShouldBeUnregistered();
        
        var user = new User(account.login, account.person).asAdmin();
        this.repository.register(user);
    }

    /**
     * Checks if the email is already registered.
     *
     * @throws EmailAlreadyExists if the email is already registered
     */
    private void emailShouldBeUnregistered() throws EmailAlreadyExists {
        if (this.repository.has(account.login.email())) {
            throw new EmailAlreadyExists();
        }
    }
}
