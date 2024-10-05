package main.domain.models.users;

import java.util.Objects;

import main.roles.Entity;

public class User extends Entity<UserId> {
    private UserId id;
    private Login login;
    private Person person;
    private boolean admin;

    // Constructors
    public User(Login login, Person person) {
        this(new UserId(), login, person, false);
    }

    public User(UserId id, Login login, Person person) {
        this(id, login, person, false);
    }

    private User(UserId id, Login login, Person person, boolean admin) {
        super(id);
        this.login = login;
        this.person = person;
        this.admin = false;
    }

    // Getters and Setters
    public Login login() { return login; }

    public Person person() { return person; }

    public boolean isAdmin() { return admin; }

    // Factory Methods
    public User asAdmin() { return new User(id, login, person, true); }

    public User with(Login login) { return new User(login, this.person); }

    public User with(Person person) { return new User(this.login, person); }

    // Overriden from Object
    @Override
    public int hashCode() { return Objects.hash(login, person); }

    public boolean equals(User other) {
        return Objects.equals(login, other.login)
                && Objects.equals(person, other.person);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User user)
            return this.equals(user);
        return false;
    }
}
