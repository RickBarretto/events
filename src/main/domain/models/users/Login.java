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
     * Password's getter
     * 
     * @return Person's Password
     */
    public String password() { return password; }

    /**
     * Creates a copy with a new email
     * 
     * @param email New Person's email
     * @return a new Person
     */
    public Person withEmail(String email) {
        return new Person(email, this.password);
    }

    /**
     * Creates a copy with a new Password
     * 
     * @param password New Person's Password
     * @return a new Person
     */
    public Person withPassword(String password) {
        return new Person(this.email, password);
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
