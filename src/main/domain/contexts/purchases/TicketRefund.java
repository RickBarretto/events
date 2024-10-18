package main.domain.contexts.purchases;

import java.util.Objects;

import main.domain.models.events.Ticket;
import main.domain.models.users.UserId;
import main.roles.repositories.Events;
import main.roles.repositories.Users;

public class TicketRefund {
    private Events events;
    private Users users;

    private UserId customer;
    private Ticket ticket;

    public TicketRefund(Events events, Users users) {
        this.events = events;
        this.users = users;
    }

    public TicketRefund to(UserId customer) {
        this.customer = customer;
        return this;
    }

    public TicketRefund with(Ticket ticket) {
        this.ticket = ticket;
        return this;
    }

    public void refund() {
        Objects.requireNonNull(customer);
        Objects.requireNonNull(ticket);
        returnTicket();
        getTicketBack();
    }

    private void getTicketBack() {
        var event = this.events.byId(ticket.event()).get();
        event.boxOffice().refund(ticket);
        this.events.update(event);
    }

    private void returnTicket() {
        var targetUser = this.users.byId(customer).get();
        targetUser.returnTicket(ticket);
        this.users.update(targetUser, targetUser);
    }

}
