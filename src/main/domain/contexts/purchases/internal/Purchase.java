package main.domain.contexts.purchases.internal;

import java.util.Objects;

import main.domain.exceptions.SoldOut;
import main.domain.models.events.Event;
import main.domain.models.events.Ticket;
import main.domain.models.users.User;

public class Purchase {
    public User buyer;
    public Ticket ticket;
    public Event event;

    public void makeOf(Integer amount) throws SoldOut {
        this.shouldBeSellable();
        ticket = new Ticket(event.id()).packedFor(amount);

        event.boxOffice().sell(ticket);
        buyer.buyTicket(ticket);
        this.shouldBeSold();
    }

    public void refund() {
        this.shouldBeSold();
        this.shouldOwnTicket();

        event.boxOffice().refund(ticket);
        buyer.returnTicket(ticket);
    }

    public void shouldBeSold() {
        this.shouldBeSellable();
        Objects.requireNonNull(ticket);
    }

    public void shouldBeSellable() {
        Objects.requireNonNull(buyer);
        Objects.requireNonNull(event);

    }

    public void shouldOwnTicket() { assert buyer.tickets().contains(ticket); }
}
