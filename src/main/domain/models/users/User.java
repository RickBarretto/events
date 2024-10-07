package main.domain.models.users;

import java.util.Objects;

import main.roles.Entity;

public class User implements Entity<UserId> {
    private final UserId id;
    private Login login;
    private Person person;
    private boolean admin;

    // Constructors
    public User(Login login, Person person) {
        this(new UserId(), login, person);
    }

    public User(UserId id, Login login, Person person) {
        this(id, login, person, false);
    }

    private User(UserId id, Login login, Person person, boolean admin) {
        this.id = id;
        this.login = login;
        this.person = person;
        this.admin = admin;
    }

    // Getters and Setters
    public UserId id() { return id; }

    public Login login() { return login; }

    public Person person() { return person; }

    public boolean isAdmin() { return admin; }

    // Factory Methods
    public User asAdmin() { return new User(id, login, person, true); }

    public User with(Login login) { return new User(id, login, person, admin); }

    public User with(Person person) {
        return new User(id, login, person, admin);
    }

    // Overriden from Object

    public User copy() { return new User(id, login, person, admin); }

    @Override
    public int hashCode() { return Objects.hash(id, login, person, admin); }

    public boolean equals(User other) {
        return Objects.equals(login, other.login)
                && Objects.equals(person, other.person)
                && Objects.equals(admin, other.admin)
                && Objects.equals(id, other.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User user)
            return this.equals(user);
        return false;
    }
}
