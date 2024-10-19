package main.domain.contexts.user;

import java.util.List;

import main.domain.models.events.Ticket;
import main.domain.models.users.User;
import main.roles.Context;

public class ListTickets implements Context {
    private User customer;

    private ListTickets(User customer) {
        this.customer = customer;
    }

    public static ListTickets ownedBy(User customer) {
        return new ListTickets(customer);
    }

    public List<Ticket> list() {
        return customer.tickets();
    }
}
