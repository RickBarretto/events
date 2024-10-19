package test.context.purchase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.List;

import org.junit.jupiter.api.Test;

import main.domain.contexts.purchases.internal.Purchase;
import main.domain.models.events.Ticket;
import test.resources.entities.ConcreteEvents;
import test.resources.entities.ConcreteUsers;

public class PurchaseTest {
    @Test
    void testShouldOwnTicket() {
        var purchase = new Purchase();

        purchase.event = ConcreteEvents.FromZeroTour();
        purchase.ticket = new Ticket(purchase.event.id());
        purchase.buyer = ConcreteUsers.JohnDoe()
                .withTickets(List.of(purchase.ticket));

        assertDoesNotThrow(() -> purchase.shouldOwnTicket());
    }

    @Test
    void testShouldOwnTicketFails() {
        var purchase = new Purchase();

        purchase.event = ConcreteEvents.FromZeroTour();
        purchase.ticket = new Ticket(purchase.event.id());
        purchase.buyer = ConcreteUsers.JohnDoe().withTickets(
                List.of(new Ticket(ConcreteEvents.ExtraFromZeroTour().id())));

        assertThrowsExactly(AssertionError.class, () -> purchase.shouldOwnTicket());
    }
}
