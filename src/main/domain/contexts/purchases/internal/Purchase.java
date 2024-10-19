package main.domain.contexts.purchases.internal;

import java.util.Objects;

import main.domain.models.events.Event;
import main.domain.models.events.Ticket;
import main.domain.models.users.User;

public class Purchase {
    public User buyer;
    public Ticket ticket;
    public Event event;

    public void refund() {
        this.shouldBeInitialized();
        this.shouldOwnTicket();

        event.boxOffice().refund(ticket);
        buyer.returnTicket(ticket);
    }

    public void shouldBeInitialized() {
        Objects.requireNonNull(buyer);
        Objects.requireNonNull(ticket);
        Objects.requireNonNull(event);
    }

    public void shouldOwnTicket() { assert buyer.tickets().contains(ticket); }
}
