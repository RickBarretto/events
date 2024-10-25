package main.domain.contexts.user;

import main.domain.exceptions.PermissionDenied;
import main.domain.models.users.Login;
import main.domain.models.users.values.EmailAddress;
import main.domain.models.users.values.Password;
import main.infra.Session;
import main.roles.Context;
import main.roles.repositories.Users;

/**
 * Context for managing user login operations.
 */
public class UserLogin implements Context {
    private Session session;
    private Users users;

    /**
     * Constructs a UserLogin context with the specified user repository.
     *
     * @param users the repository of users
     */
    public UserLogin(Users users) { this.users = users; }

    /**
     * Associates the current login context with a session.
     *
     * @param session the session to be associated
     * @return the current UserLogin context
     */
    public UserLogin withSession(Session session) {
        this.session = session;
        return this;
    }

    /**
     * Logs in the user with the specified email and password.
     *
     * @param email    the email of the user
     * @param password the password of the user
     * @throws PermissionDenied if the login credentials are incorrect
     */
    public void logAs(EmailAddress email, 
            Password password) throws PermissionDenied {
        final var login = new Login(email, password);
        session.logInAs(users.ownerOf(login).orElseThrow(
                () -> new PermissionDenied("Wrong login credentials")));
    }

    /**
     * Logs out the current session.
     */
    public void logOut() { session.logOut(); }
}
