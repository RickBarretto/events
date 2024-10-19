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

public class TicketRefund {
    private Events events;
    private Users users;

    private UserId customer;
    private Ticket ticket;
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

    public TicketRefund to(UserId customer) {
        this.customer = customer;
        return this;
    }

    public TicketRefund with(Ticket ticket) {
        this.ticket = ticket;
        return this;
    }

    public TicketRefund via(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void refund() {
        Objects.requireNonNull(customer);
        Objects.requireNonNull(ticket);
        returnTicket();
        getTicketBack();
        sendEmail();
    }

    private void sendEmail() {
        Objects.requireNonNull(ticket);
        Objects.requireNonNull(paymentMethod);

        var mailing = new MailingBuyer().by(targetUser()).owns(ticket)
                .of(targetEvent()).via(paymentMethod).purchaseMail();

        service.send(mailing);
    }

    private void getTicketBack() {
        var event = targetEvent();
        event.boxOffice().refund(ticket);
        this.events.update(event);
    }

    private Event targetEvent() {
        return this.events.byId(ticket.event()).get();
    }

    private void returnTicket() {
        var targetUser = targetUser();
        targetUser.returnTicket(ticket);
        this.users.update(targetUser, targetUser);
    }

    private User targetUser() { return this.users.byId(customer).get(); }

}
