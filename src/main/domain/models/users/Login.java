package main.domain.models.users;

import java.util.Objects;

import main.domain.models.users.values.EmailAddress;
import main.domain.models.users.values.Password;

/**
 * Represents the login information for a user, including email and password.
 */
public class Login {
    private final EmailAddress email;
    private final Password password;

    /**
     * Constructs a new Login with the specified email and password.
     *
     * @param email    the email address of the user
     * @param password the password of the user
     */
    public Login(EmailAddress email, Password password) {
        this.email = email;
        this.password = password;
    }

    public Login(String email, String password) {
        this(new EmailAddress(email), new Password(password));
    }

    /**
     * Returns the email address of the user.
     *
     * @return the email address
     */
    public EmailAddress email() { return email; }

    public Login with(EmailAddress email) { return new Login(email, password); }

    public Login with(Password password) { return new Login(email, password); }

    /**
     * Creates a new Login with the specified email, keeping the current
     * password.
     *
     * @param email the new email address
     * @return a new Login object with the updated email
     */
    public Login withEmail(EmailAddress email) {
        return new Login(email, this.password);
    }

    /**
     * Creates a new Login with the specified password, keeping the current
     * email.
     *
     * @param password the new password
     * @return a new Login object with the updated password
     */
    public Login withPassword(Password password) {
        return new Login(this.email, password);
    }

    public boolean equals(Login other) {
        return Objects.equals(email, other.email)
                && Objects.equals(password, other.password);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Login login)
            return this.equals(login);
        return false;
    }

    @Override
    public int hashCode() { return Objects.hash(email, password); }
}
