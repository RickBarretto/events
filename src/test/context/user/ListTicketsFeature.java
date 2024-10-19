package test.context.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.domain.contexts.user.ListTickets;
import main.domain.models.events.EventId;
import main.domain.models.events.Ticket;
import main.domain.models.users.Login;
import main.domain.models.users.Person;
import main.domain.models.users.User;
import test.resources.bdd.Assume;
import test.resources.bdd.Then;
import test.resources.bdd.When;

// @formatter:off
public class ListTicketsFeature {
    List<Ticket> tickets = List.of(
        new Ticket(new EventId()),
        new Ticket(new EventId())
    );

    User customer;

    @BeforeEach
    void initCustomer() {
        customer = new User(
            new Login("john.doe@example.com", "123456"),
            new Person("John Doe", "000.000.000-00")
        ).withTickets(tickets);
    }

    @Assume("A customer with two Tickets")
    void userShouldHaveTwoTickets() {
        assumeTrue(2 == customer.tickets().size());
    }

    @When("Listing their tickets")
    List<Ticket> listTickets() {
        return ListTickets.ownedBy(customer).list();
    }
    
    @Test
    @Then("Should return the same tickets")
    void shouldReturnTheSameTickets() {
        assertEquals(tickets, listTickets());
    }   
}
