package main.domain.contexts.purchases;

import java.util.Objects;
import main.domain.contexts.purchases.internal.PurchaseMail;
import main.domain.contexts.purchases.internal.Purchase;
import main.domain.models.events.Ticket;
import main.domain.models.purchases.PaymentMethod;
import main.domain.models.users.UserId;
import main.infra.DisabledEmailService;
import main.roles.Context;
import main.roles.EmailService;
import main.roles.repositories.Events;
import main.roles.repositories.Users;

/**
 * Context for processing ticket refunds.
 */
public class TicketRefund implements Context {
    private Purchase purchase = new Purchase();
    private PaymentMethod paymentMethod;
    private EmailService service;
    private Events events;
    private Users users;

    /**
     * Constructor with default email service.
     *
     * @param events the events repository
     * @param users  the users repository
     */
    public TicketRefund(Events events, Users users) {
        this(new DisabledEmailService(), events, users);
    }

    /**
     * Constructor with specified email service.
     *
     * @param service the email service
     * @param events  the events repository
     * @param users   the users repository
     */
    public TicketRefund(EmailService service, Events events, Users users) {
        this.events = events;
        this.users = users;
        this.service = service;
    }

    /**
     * Sets the customer who will receive the refund.
     *
     * @param customerId the user ID of the customer
     * @return the updated TicketRefund object
     */
    public TicketRefund to(UserId customerId) {
        purchase.buyer = users.byId(customerId).get();
        return this;
    }

    /**
     * Sets the ticket to be refunded.
     *
     * @param ticket the ticket to be refunded
     * @return the updated TicketRefund object
     */
    public TicketRefund owning(Ticket ticket) {
        purchase.event = this.events.byId(ticket.event()).get();
        purchase.ticket = ticket;
        return this;
    }

    /**
     * Sets the payment method for the refund.
     *
     * @param paymentMethod the payment method
     * @return the updated TicketRefund object
     */
    public TicketRefund via(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    /**
     * Executes the refund process.
     */
    public void refund() {
        purchase.refund();
        saveRefunding();
        sendEmail();
    }

    /**
     * Sends a refund confirmation email.
     */
    private void sendEmail() {
        Objects.requireNonNull(paymentMethod);
        var emailDoc = new PurchaseMail().of(purchase).via(paymentMethod)
                .refundMail();
        service.send(emailDoc);
    }

    /**
     * Saves the refunding process to the repositories.
     */
    private void saveRefunding() {
        events.update(purchase.event);
        users.update(purchase.buyer, purchase.buyer);
    }
}
