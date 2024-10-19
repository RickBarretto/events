package main.domain.contexts.purchases;

import java.util.Objects;

import main.domain.contexts.purchases.internal.MailingBuyer;
import main.domain.models.events.Event;
import main.domain.models.events.Ticket;
import main.domain.models.purchases.PaymentMethod;
import main.domain.models.users.User;
import main.domain.models.users.UserId;
import main.infra.DisabledEmailService;
import main.roles.EmailService;
import main.roles.repositories.Events;
import main.roles.repositories.Users;

class Purchase {
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

public class TicketRefund {
    private Purchase purchase = new Purchase();
    private Events events;
    private Users users;

    private PaymentMethod paymentMethod;
    private EmailService service;

    public TicketRefund(Events events, Users users) {
        this(new DisabledEmailService(), events, users);
    }

    public TicketRefund(EmailService service, Events events, Users users) {
        this.events = events;
        this.users = users;
        this.service = service;
    }

    public TicketRefund to(UserId customerId) {
        purchase.buyer = users.byId(customerId).get();
        return this;
    }

    public TicketRefund with(Ticket ticket) {
        purchase.event = this.events.byId(ticket.event()).get();
        purchase.ticket = ticket;
        return this;
    }

    public TicketRefund via(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void refund() {
        purchase.shouldBeInitialized();
        purchase.refund();
        saveRefunding();
        sendEmail();
    }

    private void sendEmail() {
        Objects.requireNonNull(paymentMethod);

        var mailing = new MailingBuyer().by(purchase.buyer)
                .owns(purchase.ticket).of(purchase.event).via(paymentMethod)
                .purchaseMail();

        service.send(mailing);
    }

    private void saveRefunding() {
        events.update(purchase.event);
        users.update(purchase.buyer, purchase.buyer);
    }
}
