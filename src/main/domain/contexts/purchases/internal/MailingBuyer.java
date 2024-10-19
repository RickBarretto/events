package main.domain.contexts.purchases.internal;

import main.domain.models.email.EmailDocument;
import main.domain.models.purchases.Participant;
import main.domain.models.purchases.PaymentDetails;
import main.domain.models.purchases.PaymentMethod;
import main.domain.models.purchases.Transaction;

public class MailingBuyer {
    private final Participant self = Participant.self();

    private Purchase purchase;
    private PaymentMethod paymentMethod;

    public MailingBuyer of(Purchase purchase) {
        this.purchase = purchase;
        return this;
    }

    public MailingBuyer via(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public EmailDocument purchaseMail() {
        return mail(new Participant(purchase.buyer), self, purchaseMessage());
    }

    public EmailDocument refundMail() {
        return mail(self, new Participant(purchase.buyer), refundMessage());
    }

    private EmailDocument mail(Participant payer, Participant recipient,
            String description) {
        purchase.shouldBeSold();
        return new Transaction(payer, recipient, this.paymentDetails(),
                description).toEmail();
    }

    // @formatter:off
    private String ticketDescription() {
        // E.g.: "2-person ticket"
        return new StringBuilder("")
            .append(purchase.ticket.availableFor())
            .append("-person ticket")
            .toString();
    }

    private String purchaseMessage() {
        var ticketDescription = ticketDescription();
        var eventDescription = purchase.event.poster().title();

        return new StringBuilder("A ")
            .append(ticketDescription)
            .append(" was purchased for ")
            .append(eventDescription)
            .toString();
    }
    
    private String refundMessage() {
        return new StringBuilder("Refunding in ")
            .append(String.format("R$%.2f", purchase.ticket.price()))
            .append(" for ")
            .append(purchase.event.poster().title())
            .append(" purchase.")
            .toString();
    }

    private PaymentDetails paymentDetails() {
        return new PaymentDetails(paymentMethod, purchase.ticket.price());
    }

}
