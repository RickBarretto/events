package main.domain.contexts.purchases;

import java.util.Objects;

import main.domain.contexts.purchases.internal.MailingBuyer;
import main.domain.exceptions.SoldOut;
import main.domain.models.events.Event;
import main.domain.models.events.EventId;
import main.domain.models.events.Ticket;
import main.domain.models.purchases.PaymentMethod;
import main.domain.models.users.User;
import main.domain.models.users.UserId;
import main.infra.DisabledEmailService;
import main.roles.EmailService;
import main.roles.repositories.Events;
import main.roles.repositories.Users;

public class TicketBuying {
    private Events events;
    private Users users;

    private EventId event;
    private UserId customer;
    private Integer amount;
    private PaymentMethod paymentMethod;
    private EmailService service;

    private Ticket ticket;

    public TicketBuying(Events events, Users users) {
        this(new DisabledEmailService(), events, users);
    }

    public TicketBuying(EmailService service, Events events, Users users) {
        this.events = events;
        this.users = users;
        this.service = service;
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

    public TicketBuying via(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void buy() throws SoldOut {
        Objects.requireNonNull(event);
        Objects.requireNonNull(customer);
        ticket = sellTicket();
        buyTicket(ticket);
        sendEmail();
    }

    private void sendEmail() {
        Objects.requireNonNull(ticket);
        Objects.requireNonNull(paymentMethod);

        var mailing = new MailingBuyer().by(targetUser()).owns(ticket)
                .of(targetEvent()).via(paymentMethod).purchaseMail();

        service.send(mailing);
    }

    private void buyTicket(Ticket ticket) {
        var targetUser = targetUser();
        targetUser.buyTicket(ticket);
        this.users.update(targetUser, targetUser);
    }

    private Ticket sellTicket() throws SoldOut {
        var targetEvent = targetEvent();
        var boxOffice = targetEvent.boxOffice();

        var ticket = boxOffice.ticket(amount);
        boxOffice.sell(ticket);
        this.events.update(targetEvent);

        return ticket;
    }

    private User targetUser() { return this.users.byId(customer).get(); }

    private Event targetEvent() { return this.events.byId(event).get(); }
}
