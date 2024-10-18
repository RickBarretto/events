package main.domain.models.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import main.domain.models.events.Ticket;
import main.roles.Entity;

public class User implements Entity<UserId> {
    private final UserId id;
    private final Login login;
    private final Person person;
    private final boolean admin;
    private ArrayList<Ticket> boughtTickets;

    // Constructors
    public User(Login login, Person person) {
        this(new UserId(), login, person);
    }

    public User(UserId id, Login login, Person person) {
        this(id, login, person, false, new ArrayList<>());
    }

    private User(UserId id, Login login, Person person, boolean admin,
            ArrayList<Ticket> tickets) {
        this.id = id;
        this.login = login;
        this.person = person;
        this.admin = admin;
        this.boughtTickets = tickets;
    }

    public User withTickets(List<Ticket> tickets) {
        return new User(id, login, person, admin, new ArrayList<>(tickets));
    }

    public void buyTicket(Ticket ticket) { this.boughtTickets.add(ticket); }

    public void returnTicket(Ticket ticket) {
        this.boughtTickets.remove(ticket);
    }

    // Getters and Setters
    public UserId id() { return id; }

    public Login login() { return login; }

    public Person person() { return person; }

    public boolean isAdmin() { return admin; }

    public List<Ticket> tickets() { return List.copyOf(boughtTickets); }

    // Factory Methods
    public User asAdmin() {
        return new User(id, login, person, true, boughtTickets);
    }

    public User with(Login login) {
        return new User(id, login, person, admin, boughtTickets);
    }

    public User with(Person person) {
        return new User(id, login, person, admin, boughtTickets);
    }

    // Overriden from Object

    public User copy() {
        return new User(id, login, person, admin, boughtTickets);
    }

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
