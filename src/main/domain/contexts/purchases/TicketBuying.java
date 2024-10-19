package main.domain.contexts.purchases;

import java.util.Objects;

import main.domain.contexts.purchases.internal.MailingBuyer;
import main.domain.contexts.purchases.internal.Purchase;
import main.domain.exceptions.SoldOut;
import main.domain.models.events.EventId;
import main.domain.models.purchases.PaymentMethod;
import main.domain.models.users.UserId;
import main.infra.DisabledEmailService;
import main.roles.EmailService;
import main.roles.repositories.Events;
import main.roles.repositories.Users;

public class TicketBuying {
    private Purchase purchase = new Purchase();
    private PaymentMethod paymentMethod;

    private EmailService service;
    private Events events;
    private Users users;

    public TicketBuying(Events events, Users users) {
        this(new DisabledEmailService(), events, users);
    }

    public TicketBuying(EmailService service, Events events, Users users) {
        this.events = events;
        this.users = users;
        this.service = service;
    }

    public TicketBuying of(EventId event) {
        purchase.event = this.events.byId(event).get();
        return this;
    }

    public TicketBuying by(UserId customer) {
        purchase.buyer = this.users.byId(customer).get();
        ;
        return this;
    }

    public TicketBuying via(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void buy(Integer amount) throws SoldOut {
        purchase.makeOf(amount);
        savePurchase();
        sendEmail();
    }

    private void savePurchase() {
        events.update(purchase.event);
        users.update(purchase.buyer, purchase.buyer);
    }

    private void sendEmail() {
        Objects.requireNonNull(paymentMethod);

        var emailDoc = new MailingBuyer().of(purchase).via(paymentMethod)
                .purchaseMail();

        service.send(emailDoc);
    }
}
