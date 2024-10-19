package test.context.purchase;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNoException;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.domain.contexts.purchases.TicketBuying;
import main.domain.contexts.purchases.TicketRefund;
import main.domain.exceptions.SoldOut;
import main.domain.models.events.Event;
import main.domain.models.events.EventId;
import main.domain.models.events.Poster;
import main.domain.models.purchases.PaymentMethod;
import main.domain.models.users.Login;
import main.domain.models.users.Person;
import main.domain.models.users.User;
import main.domain.models.users.UserId;
import main.infra.virtual.EventsInMemory;
import main.infra.virtual.UsersInMemory;
import main.roles.repositories.Events;
import main.roles.repositories.Users;
import test.resources.bdd.Assume;
import test.resources.bdd.Then;
import test.resources.bdd.When;

// @formatter:off
public class TicketRefundFeature {
    Events events;
    Users users;

    void sellTickets(Integer amount) throws SoldOut {
        new TicketBuying(events, users)
            .of(targetEvent()).by(targetUser())
            .via(new PaymentMethod("...", "..."))
            .buy(amount);
    }

    @BeforeEach
    void loadRepositories() {
        var event = new Event(new Poster(
            "From Zero", 
                    "A LP show",
            LocalDate.of(2024, 10, 15))
        );

        event.addCapacity(2);

        events = new EventsInMemory(List.of(event));
        users = new UsersInMemory(List.of(
            new User(
                new Login("john.doe@example.com", "123456"),
                new Person("John Doe", "000.000.000-00")
            )
        ));
    }

    EventId targetEvent() { return events.list().get(0).id(); }
    UserId targetUser() { return users.list().get(0).id(); }


    @Assume("One couple Tickets sold")
    void sellCoupleTickets() {
        try {
            sellTickets(2);
        } catch (Exception e) {
            assumeNoException(e);
        }
    }
    
    @Assume("Two individual tickets have been sold")
    void sellTwoIndividualTickets() {
        try {
            sellTickets(1);
            sellTickets(1);
        } catch (Exception e) {
            assumeNoException(e);
        }
    }

    void refundFirstTicket() {
        new TicketRefund(events, users)
            .to(targetUser())
            .via(new PaymentMethod("...", "..."))
            .with(users.byId(targetUser()).get().tickets().get(0))
            .refund();
    }

    @Test
    @When("Refunding one Ticket of two individual ones")
    @Then("Event should have 0 sales and user 0 tickets")
    void shouldBeEmptyForIndividual() {
        // Given
        sellTwoIndividualTickets();
        
        // When
        refundFirstTicket();
        
        // Then
        assertTrue(1 == events.byId(targetEvent()).get().boxOffice().available());
        assertTrue(1 == users.byId(targetUser()).get().tickets().size());
    }

    @Test
    @When("Refunding one Ticket for couples")
    @Then("Event should have 0 sales and user 0 tickets")
    void shouldBeEmptyForCouple() {
        // Given
        sellCoupleTickets();

        // When
        refundFirstTicket();

        // Then
        assertTrue(2 == events.byId(targetEvent()).get().boxOffice().available());
        assertTrue(users.byId(targetUser()).get().tickets().isEmpty());
    }
}
