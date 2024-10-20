package test.context.purchase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import java.util.List;
import org.junit.jupiter.api.Test;
import main.domain.contexts.purchases.internal.Purchase;
import main.domain.models.events.Ticket;
import test.resources.entities.ConcreteEvents;
import test.resources.entities.ConcreteUsers;
import test.resources.bdd.*;

@Feature("Purchase Validation")
public class PurchaseTest {

    @Scenario("Valid Purchase")
    @Given("A user has a ticket for an event")
    @When("The user checks ticket ownership")
    @Then("The check should pass if the ticket is owned by the user")
    @Test
    void testShouldOwnTicket() {
        var purchase = new Purchase();
        purchase.event = ConcreteEvents.FromZeroTour();
        purchase.ticket = new Ticket(purchase.event.id());
        purchase.buyer = ConcreteUsers.JohnDoe()
                .withTickets(List.of(purchase.ticket));

        assertDoesNotThrow(() -> purchase.shouldOwnTicket());
    }

    @Scenario("Invalid Purchase")
    @Given("A user has a different ticket for a different event")
    @When("The user checks ticket ownership")
    @Then("The check should fail if the ticket is not owned by the user")
    @Test
    void testShouldOwnTicketFails() {
        var purchase = new Purchase();
        purchase.event = ConcreteEvents.FromZeroTour();
        purchase.ticket = new Ticket(purchase.event.id());
        purchase.buyer = ConcreteUsers.JohnDoe().withTickets(
                List.of(new Ticket(ConcreteEvents.ExtraFromZeroTour().id())));

        assertThrowsExactly(AssertionError.class,
                () -> purchase.shouldOwnTicket(),
                () -> purchase.buyer.tickets().toString());
    }
}
