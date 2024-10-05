package main.domain.models.users;

import java.util.Objects;

import main.roles.Entity;

public class User implements Entity<UserId> {
    private UserId id;
    private Login login;
    private Person person;
    private boolean admin;

    // Constructors
    public User(Login login, Person person) {
        this(new UserId(), login, person);
    }

    public User(UserId id, Login login, Person person) {
        this.id = id;
        this.login = login;
        this.person = person;
        this.admin = false;
    }

    // Getters and Setters
    public UserId id() { return id; }

    public Login login() { return login; }

    public Person person() { return person; }

    public boolean isAdmin() { return admin; }

    // Factory Methods
    public User asAdmin() {
        var newUser = new User(id, login, person);
        newUser.admin = true;
        return newUser;
    }

    public User with(Login login) { return new User(login, this.person); }

    public User with(Person person) { return new User(this.login, person); }

    // Overriden from Object
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
