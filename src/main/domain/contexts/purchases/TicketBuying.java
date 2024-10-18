package main.domain.contexts.purchases;

import java.util.Objects;

import main.domain.exceptions.SoldOut;
import main.domain.models.events.EventId;
import main.domain.models.events.Ticket;
import main.domain.models.users.UserId;
import main.roles.repositories.Events;
import main.roles.repositories.Users;

public class TicketBuying {
    private Events events;
    private Users users;

    private EventId event;
    private UserId customer;
    private Integer amount;

    public TicketBuying(Events events, Users users) {
        this.events = events;
        this.users = users;
    }

    public TicketBuying of(EventId event) {
        this.event = event;
        return this;
    }

    public TicketBuying by(UserId customer) {
        this.customer = customer;
        return this;
    }

    public TicketBuying amountOf(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void buy() throws SoldOut {
        Objects.requireNonNull(event);
        Objects.requireNonNull(customer);
        buyTicket(sellTicket());
    }

    private void buyTicket(Ticket ticket) {
        var targetUser = this.users.byId(customer).get();
        targetUser.buyTicket(ticket);
        this.users.update(targetUser, targetUser);
    }

    private Ticket sellTicket() throws SoldOut {
        var targetEvent = this.events.byId(event).get();
        var boxOffice = targetEvent.boxOffice();

        var ticket = boxOffice.ticket(amount);
        boxOffice.sell(ticket);
        this.events.update(targetEvent);

        return ticket;
    }
}
