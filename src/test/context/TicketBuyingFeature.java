package test.context;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import main.domain.contexts.user.TicketBuying;
import main.domain.exceptions.SoldOut;
import main.domain.models.events.Event;
import main.domain.models.events.EventId;
import main.domain.models.events.Poster;
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
public class TicketBuyingFeature {
    Events events;
    Users users;

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

    void sellTickets(Integer amount) throws SoldOut {
        new TicketBuying(events, users)
            .of(targetEvent()).by(targetUser())
            .amountOf(amount)
            .buy();
    }

    @Nested
    class BuyingAvaliableTickets {

        @Assume("An user without bought tickets")
        void userShouldHaveNoTickets() {
            assumeTrue(users.byId(targetUser()).get().tickets().isEmpty());
        }
        
        @Assume("An event with no sales")
        void eventShouldHaveNoSales() {
            assumeTrue(events.byId(targetEvent()).get().boxOffice().sales() == 0);
        }
        
        @Assume("An event with two available tickets")
        void eventShouldHavetwoAvaliableTickets() {
            assumeTrue(events.byId(targetEvent()).get().boxOffice().available() == 2);
        }

        // Schenario 1 

        @When("Buy an available ticket")
        void buyingOneTicket() {
            assertDoesNotThrow(
                () -> 
                sellTickets(1)
            );
        }

        @Test
        @Then("Should have one individual ticket")
        void shouldHaveOneTicket() {
            // Given
            userShouldHaveNoTickets();

            // When
            buyingOneTicket();

            // Should
            final User actual = users.byId(targetUser()).get();
            assertTrue(actual.tickets().size() == 1);
        }

        @Test
        @Then("Should have two unit sold")
        void shouldHaveOneSell() {
            // Given
            eventShouldHaveNoSales();
            eventShouldHavetwoAvaliableTickets();
            
            // When
            buyingOneTicket();

            // Should
            final Event event = events.byId(targetEvent()).get();

            assertTrue(event.boxOffice().capacity() == 2);
            assertTrue(event.boxOffice().sales() == 1);
            assertFalse(event.boxOffice().isSoldOut());
        }


        // Schenario 2

        @When("Buy a ticket for couple")
        void buyingTicketForCouple() {
            assertDoesNotThrow(
                    () -> sellTickets(2));
        }

        @Test
        @Then("Should have one ticket for couples")
        void shouldHaveOneTicketForCouple() {
            // Given
            userShouldHaveNoTickets();

            // When
            buyingTicketForCouple();

            // Should
            final User actual = users.byId(targetUser()).get();
            assertTrue(actual.tickets().size() == 1);
        }

        @Test
        @Then("Should have two unit sold")
        void shouldHaveTwoSellsForCouple() {
            // Given
            eventShouldHaveNoSales();
            eventShouldHavetwoAvaliableTickets();
            
            // When
            buyingTicketForCouple();

            // Should
            final Event event = events.byId(targetEvent()).get();

            assertTrue(event.boxOffice().capacity() == 2);
            assertTrue(event.boxOffice().sales() == 2);
            assertTrue(event.boxOffice().isSoldOut());
        }


        // Schenario 3

        @When("Buy multiple individual tickets")
        void buyingTwoTickets() {
            assertAll("Won't sold out",
                () -> assertDoesNotThrow(() -> sellTickets(1)),
                () -> assertDoesNotThrow(() -> sellTickets(1))
            );
        }

        @Test
        @Then("User should have two individual tickets")
        void shouldHaveTwoTickets() {
            // Given
            userShouldHaveNoTickets();

            // When
            buyingTwoTickets();

            // Should
            var actualUser = users.byId(targetUser()).get();
            assertTrue(actualUser.tickets().size() == 2);
        }

        @Test
        @Then("Should have two unit sold")
        void shouldHaveTwoSells() {
            // Given
            eventShouldHaveNoSales();
            eventShouldHavetwoAvaliableTickets();
            
            // When
            buyingTwoTickets();

            // Should
            final Event event = events.byId(targetEvent()).get();

            assertTrue(event.boxOffice().capacity() == 2);
            assertTrue(event.boxOffice().sales() == 2);
            assertTrue(event.boxOffice().isSoldOut());
        }
    }

    @Nested
    class BuyingUnavailableTickets {

        @BeforeEach
        void soldOutEvent() {
            assumeTrue(events.byId(targetEvent()).get().boxOffice().available() == 2);
            
            try { sellTickets(2); } 
            catch (SoldOut soldout) { assumeNoException(soldout); }
        }

        @Assume("Event is sold-out")
        void eventShouldBeSoldOut() {
            assumeTrue(events.byId(targetEvent()).get().boxOffice().isSoldOut());
        }

        // Schenario 1

        @When("Buying unavailable Ticket")
        void buyingUnavailable() throws SoldOut {
            sellTickets(1);
        } 

        @Test
        @Then("Should throw exception")
        void shouldHaveOneTicket() {
            // Given
            eventShouldBeSoldOut();

            // When -> Should
            assertThrows(SoldOut.class, () -> buyingUnavailable());
        }
    }
}
