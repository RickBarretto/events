package test.context.purchase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import main.domain.contexts.purchases.internal.MailingBuyer;
import main.domain.models.email.EmailDocument;
import main.domain.models.events.Event;
import main.domain.models.events.Poster;
import main.domain.models.events.Ticket;
import main.domain.models.purchases.PaymentMethod;
import main.domain.models.users.Login;
import main.domain.models.users.Person;
import main.domain.models.users.User;

public class MailingBuyerFeature {
    final PaymentMethod method = new PaymentMethod("PIX",
            "Chave aleatória: d4f8e20b-48fa-4c91-9363-e067c074fa75");
    final User buyer = new User(new Login("john.doe@example.com", "123456"),
            new Person("John Doe", "000.000.000-00"));
    final Event event = new Event(
            new Poster("From Zero", "A LP show", LocalDate.of(2024, 11, 15)),
            850.00);
    final Ticket ticket = new Ticket(event.id(), 850.00).packedFor(3);

    MailingBuyer service() {
        return new MailingBuyer().by(buyer).owns(ticket).of(event).via(method);
    }

    @Test
    void testPurchase() {
        final EmailDocument email = service().purchaseMail();
        final String content = email.body();

        assertTrue(content.contains("A 3-person ticket"));
        assertTrue(content.contains(" was purchased for From Zero"));
        assertFalse(content.contains("Refunding in"));
    }

    @Test
    void testRefundMessage() {
        final EmailDocument email = service().refundMail();
        final String content = email.body();

        assertTrue(content.contains("Refunding in R$2550.00"));
        assertTrue(content.contains(" for From Zero purchase."));
        assertFalse(content.contains("was purchased"));
    }

}
