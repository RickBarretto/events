package main.domain.contexts.purchases.internal;

import java.util.Objects;

import main.domain.models.email.EmailDocument;
import main.domain.models.events.Event;
import main.domain.models.events.Ticket;
import main.domain.models.purchases.Participant;
import main.domain.models.purchases.PaymentDetails;
import main.domain.models.purchases.PaymentMethod;
import main.domain.models.purchases.Transaction;
import main.domain.models.users.User;

public class MailingBuyer {
    private final Participant self = Participant.self();

    private PaymentMethod paymentMethod;
    private Participant buyer;
    private Event event;
    private Ticket ticket;

    public MailingBuyer owns(Ticket ticket) {
        this.ticket = ticket;
        return this;
    }

    public MailingBuyer by(User buyer) {
        this.buyer = new Participant(buyer);
        return this;
    }

    public MailingBuyer of(Event event) {
        this.event = event;
        return this;
    }

    public MailingBuyer via(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public EmailDocument purchaseMail() {
        return mail(buyer, self, purchaseMessage());
    }

    public EmailDocument refundMail() {
        return mail(self, buyer, refundMessage());
    }

    private EmailDocument mail(Participant payer, Participant recipient,
            String description) {
        Objects.requireNonNull(this.self);
        Objects.requireNonNull(this.ticket);
        Objects.requireNonNull(this.buyer);
        Objects.requireNonNull(this.event);

        return new Transaction(buyer, self, this.paymentDetails(), description)
                .toEmail();
    }

    // @formatter:off
    private String ticketDescription() {
        // E.g.: "2-person ticket"
        return new StringBuilder("")
            .append(ticket.availableFor())
            .append("-person ticket")
            .toString();
    }

    private String purchaseMessage() {
        var ticketDescription = ticketDescription();
        var eventDescription = event.poster().title();

        return new StringBuilder("A ")
            .append(ticketDescription)
            .append(" was purchased for ")
            .append(eventDescription)
            .toString();
    }
    
    private String refundMessage() {
        return new StringBuilder("Refunding in ")
            .append(String.format("R$%.2f", ticket.price()))
            .append(" for ")
            .append(event.poster().title())
            .append(" purchase.")
            .toString();
    }

    private PaymentDetails paymentDetails() {
        return new PaymentDetails(paymentMethod, ticket.price());
    }

}
