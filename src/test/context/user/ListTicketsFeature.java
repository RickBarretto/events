package test.context.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.domain.contexts.user.ListTickets;
import main.domain.models.events.EventId;
import main.domain.models.events.Ticket;
import main.domain.models.users.User;

// Test supporters
import test.resources.bdd.*;
import test.resources.entities.ConcreteUsers;

// @formatter:off
public class ListTicketsFeature {
    List<Ticket> tickets = List.of(
        new Ticket(new EventId()),
        new Ticket(new EventId())
    );

    User customer;

    @BeforeEach
    void initCustomer() {
        customer = ConcreteUsers.JohnDoe().withTickets(tickets);
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
