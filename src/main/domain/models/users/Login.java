package main.domain.models.users;

import java.util.Objects;


public class Login {
    private String email;
    private String password;

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Email's getter
     * 
     * @return Person's email
     */
    public String email() { return email; }

    /**
     * Creates a copy with a new email
     * 
     * @param email New Person's email
     * @return a new Person
     */
    public Login withEmail(String email) {
        return new Login(email, this.password);
    }

    /**
     * Creates a copy with a new Password
     * 
     * @param password New Person's Password
     * @return a new Person
     */
    public Login withPassword(String password) {
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
