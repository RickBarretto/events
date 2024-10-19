package main.domain.contexts.purchases;

import java.util.Objects;
import main.domain.contexts.purchases.internal.PurchaseMail;
import main.domain.contexts.purchases.internal.Purchase;
import main.domain.exceptions.SoldOut;
import main.domain.models.events.EventId;
import main.domain.models.purchases.PaymentMethod;
import main.domain.models.users.UserId;
import main.infra.DisabledEmailService;
import main.roles.Context;
import main.roles.EmailService;
import main.roles.repositories.Events;
import main.roles.repositories.Users;

/**
 * Context for buying tickets for an event.
 */
public class TicketBuying implements Context {
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
    public TicketBuying(Events events, Users users) {
        this(new DisabledEmailService(), events, users);
    }

    /**
     * Constructor with specified email service.
     *
     * @param service the email service
     * @param events  the events repository
     * @param users   the users repository
     */
    public TicketBuying(EmailService service, Events events, Users users) {
        this.events = events;
        this.users = users;
        this.service = service;
    }

    /**
     * Sets the event for which tickets are being bought.
     *
     * @param event the event ID
     * @return the updated TicketBuying object
     */
    public TicketBuying of(EventId event) {
        purchase.event = this.events.byId(event).get();
        return this;
    }

    /**
     * Sets the customer who is buying the tickets.
     *
     * @param customer the user ID of the customer
     * @return the updated TicketBuying object
     */
    public TicketBuying by(UserId customer) {
        purchase.buyer = this.users.byId(customer).get();
        return this;
    }

    /**
     * Sets the payment method for the purchase.
     *
     * @param paymentMethod the payment method
     * @return the updated TicketBuying object
     */
    public TicketBuying via(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    /**
     * Executes the ticket purchase.
     *
     * @param amount the amount of tickets to buy
     * @throws SoldOut if the event is sold out
     */
    public void buy(Integer amount) throws SoldOut {
        purchase.makeOf(amount);
        savePurchase();
        sendEmail();
    }

    /**
     * Saves the purchase to the repositories.
     */
    private void savePurchase() {
        events.update(purchase.event);
        users.update(purchase.buyer, purchase.buyer);
    }

    /**
     * Sends a confirmation email for the purchase.
     */
    private void sendEmail() {
        Objects.requireNonNull(paymentMethod);
        var emailDoc = new PurchaseMail().of(purchase).via(paymentMethod)
                .purchaseMail();
        service.send(emailDoc);
    }
}
