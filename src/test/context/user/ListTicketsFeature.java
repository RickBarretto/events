package test.context.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.domain.contexts.user.ListTickets;
import main.domain.models.events.EventId;
import main.domain.models.events.Ticket;
import main.domain.models.users.User;
import test.resources.entities.ConcreteUsers;
import test.resources.bdd.*;

@Feature("Listing User Tickets")
public class ListTicketsFeature {

    private List<Ticket> tickets;
    private User customer;

    @BeforeEach
    void initCustomer() {
        tickets = List.of(new Ticket(new EventId()), new Ticket(new EventId()));
        customer = ConcreteUsers.JohnDoe().withTickets(tickets);
    }

    @Given("A customer with two tickets")
    @Assume("The customer has two tickets")
    @When("Listing their tickets")
    @Then("Should return the same tickets")
    @Test
    void shouldReturnTheSameTickets() {
        assumeTrue(2 == customer.tickets().size());
        assertEquals(tickets, ListTickets.ownedBy(customer).list());
    }
}
