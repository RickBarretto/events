package main.domain.models.purchases;

import javax.swing.text.html.HTML;

import main.roles.Entity;

public class Transaction implements Entity<TransactionId> {
    private final TransactionId id;
    private final String desciption;
    private final Participant payer;
    private final Participant recipient;
    private final PaymentDetails payment;

    public Transaction(TransactionId id, Participant payer,
            Participant recipient, PaymentDetails payment, String description) {

        this.id = id;
        this.payer = payer;
        this.recipient = recipient;
        this.payment = payment;
        this.desciption = description;
    }

    public Transaction(Participant payer, Participant recipient,
            PaymentDetails payment, String description) {
        this(new TransactionId(), payer, recipient, payment, description);
    }

    public TransactionId id() { return id; }

    public Email toEmail() {
        return new Email(
                new EmailMetadata(payer.email(), recipient.email(), desciption),
                this.html());
    }

    // @formatter:off

    public Html html() {
        var content = new StringBuilder("")
            .append(Html.node("h1").content(desciption))
            .append(transactionSect())
            .append(Html.br())
            .append(payment.html())
            .toString();
        return Html.node("body").content(content);
    }

    private String transactionSect() {
        return new StringBuilder("Transaction by ")
            .append(payer)
            .append(" to ")
            .append(recipient)
            .append(" of ")
            .append(payment.amount())
            .append(".")
            .toString();
    }
}
