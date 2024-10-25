package test.context.purchase;

import static org.junit.Assert.assertThrows;
import static org.junit.Assume.assumeNoException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import main.domain.contexts.purchases.TicketBuying;
import main.domain.exceptions.PurchaseForInactiveEvent;
import main.domain.exceptions.SoldOut;
import main.domain.models.events.Event;
import main.domain.models.events.EventId;
import main.domain.models.events.Poster;
import main.domain.models.purchases.PaymentMethod;
import main.domain.models.users.Login;
import main.domain.models.users.Person;
import main.domain.models.users.User;
import main.domain.models.users.UserId;
import main.domain.models.users.values.EmailAddress;
import main.domain.models.users.values.Password;
import main.infra.json.EventsJson;
import main.infra.json.JsonFile;
import main.infra.json.UsersJson;
import main.infra.virtual.EventsInMemory;
import main.infra.virtual.UsersInMemory;
import main.roles.repositories.Events;
import main.roles.repositories.Users;
import test.resources.bdd.And;
import test.resources.bdd.Assume;
import test.resources.bdd.Feature;
import test.resources.bdd.Given;
import test.resources.bdd.Scenario;
import test.resources.bdd.Then;
import test.resources.bdd.When;

@Feature("Ticket Buying")
public class TicketBuyingFeature {
    private final LocalDate activeDate = LocalDate.of(2024, 10, 15);
    private final LocalDate inactiveDate = LocalDate.of(2024, 10, 16);
    private Events events;
    private Users users;

    @BeforeEach
    void loadRepositories() {
        var event = new Event(new Poster("From Zero", "A LP show",
                LocalDate.of(2024, 10, 15)));
        event.addCapacity(2);
        events = new EventsInMemory(List.of(event));
        users = new UsersInMemory(
                List.of(new User(Login.of("john.doe@example.com",
                        "123456"),
                        new Person("John Doe", "000.000.000-00"))));
    }

    EventId targetEvent() { return events.list().get(0).id(); }

    UserId targetUser() { return users.list().get(0).id(); }

    void sellTickets(Integer amount, LocalDate currentDay)
            throws SoldOut, PurchaseForInactiveEvent {
        new TicketBuying(events, users,
                currentDay)
                        .of(targetEvent())
                        .by(targetUser())
                        .via(new PaymentMethod("...", "..."))
                        .buy(amount);
    }

    @Scenario("Denied sales for Inactive Event")
    @Given("An inactive event with two available tickets and a user without bought tickets")
    @Assume("An inactive event with two available tickets")
    @When("Buying some ticket")
    @Then("Should throw PurchaseForInactiveEvent")
    @And("Should not sell")
    @Test
    void shouldNotSell() {
        // Assumptions
        assumeTrue(users.byId(targetUser()).get().tickets().isEmpty());
        assumeTrue(
                events.byId(targetEvent()).get().boxOffice().available() == 2);

        // When
        assertThrowsExactly(PurchaseForInactiveEvent.class,
                () -> sellTickets(1, inactiveDate));

        // Then
        User actualUser = users.byId(targetUser()).get();
        Event actualEvent = events.byId(targetEvent()).get();

        assertTrue(actualUser.tickets().isEmpty());
        assertTrue(actualEvent.boxOffice().available() == 2);
    }

    @Scenario("Buying Available Tickets")
    @Given("An active event with two available tickets and a user without bought tickets")
    @Assume("An active event with two available tickets")
    @When("Buying an available ticket")
    @Then("Should have one individual ticket")
    @Test
    void shouldHaveOneIndividualTicket() {
        // Assumptions
        assumeTrue(users.byId(targetUser()).get().tickets().isEmpty());
        assumeTrue(
                events.byId(targetEvent()).get().boxOffice().available() == 2);

        // When
        assertDoesNotThrow(() -> sellTickets(1, activeDate));

        // Then
        User actualUser = users.byId(targetUser()).get();
        assertTrue(actualUser.tickets().size() == 1);
    }

    @Scenario("Buying Available Tickets")
    @Given("An active event with two available tickets and no sales")
    @Assume("An active event with no sales")
    @When("Buying an available ticket")
    @Then("Should have two units sold")
    @Test
    void shouldHaveTwoUnitsSold() {
        // Assumptions
        assumeTrue(events.byId(targetEvent()).get().boxOffice().sales() == 0);
        assumeTrue(
                events.byId(targetEvent()).get().boxOffice().available() == 2);

        // When
        assertDoesNotThrow(() -> sellTickets(1, activeDate));

        // Then
        Event event = events.byId(targetEvent()).get();
        assertTrue(event.boxOffice().capacity() == 2);
        assertTrue(event.boxOffice().sales() == 1);
        assertFalse(event.boxOffice().isSoldOut());
    }

    @Scenario("Buying a ticket for a couple")
    @Given("An active event with two available tickets and no sales")
    @Assume("An active event with no sales")
    @When("Buying a ticket for a couple")
    @Then("Should have one ticket for couples and two units sold")
    @Test
    void shouldHaveOneTicketForCouple() {
        // Assumptions
        assumeTrue(users.byId(targetUser()).get().tickets().isEmpty());
        assumeTrue(events.byId(targetEvent()).get().boxOffice().sales() == 0);

        // When
        assertDoesNotThrow(() -> sellTickets(2, activeDate));

        // Then
        User actualUser = users.byId(targetUser()).get();
        Event event = events.byId(targetEvent()).get();
        assertTrue(actualUser.tickets().size() == 1);
        assertTrue(event.boxOffice().sales() == 2);
        assertTrue(event.boxOffice().isSoldOut());
    }

    @Scenario("Buying multiple individual tickets")
    @Given("An active event with two available tickets and a user without bought tickets")
    @Assume("An active event with two available tickets and no sales")
    @When("Buying multiple individual tickets")
    @Then("User should have two individual tickets and event should have two units sold")
    @Test
    void shouldHaveTwoIndividualTickets() {
        // Assumptions
        assumeTrue(users.byId(targetUser()).get().tickets().isEmpty());
        assumeTrue(events.byId(targetEvent()).get().boxOffice().sales() == 0);

        // When
        assertAll("Won't sold out",
                () -> assertDoesNotThrow(() -> sellTickets(1,
                        activeDate)),
                () -> assertDoesNotThrow(() -> sellTickets(1, activeDate)));

        // Then
        User actualUser = users.byId(targetUser()).get();
        Event event = events.byId(targetEvent()).get();
        assertTrue(actualUser.tickets().size() == 2);
        assertTrue(event.boxOffice().sales() == 2);
        assertTrue(event.boxOffice().isSoldOut());
    }

    @Scenario("Buying unavailable tickets")
    @Given("An active event with no available tickets")
    @Assume("Event is sold-out")
    @When("Buying unavailable ticket")
    @Then("Should throw SoldOut exception")
    @Test
    void shouldThrowSoldOut() {
        // Given
        assertDoesNotThrow(() -> sellTickets(2, activeDate));
        assumeTrue(events.byId(targetEvent()).get().boxOffice().isSoldOut());

        // When -> Then
        assertThrows(SoldOut.class, () -> sellTickets(3, activeDate));
    }

    @Nested
    @Scenario("Selling tickets for JSON Repositories")
    class UsingJson {

        static final String directory = "src/test/context/resources";
        static final JsonFile eventsFile = new JsonFile(directory,
                "all-events");
        static final JsonFile usersFile = new JsonFile(directory, "all-users");

        void sellTickets(Integer amount) throws SoldOut {
            var eventsJson = new EventsJson(eventsFile,
                    (EventsInMemory) events);
            var usersJson = new UsersJson(usersFile, (UsersInMemory) users);

            LocalDate activeDate = LocalDate.of(2024, 10, 15);

            try {
                new TicketBuying(eventsJson, usersJson,
                        activeDate)
                                .of(targetEvent())
                                .by(targetUser())
                                .via(new PaymentMethod("...", "..."))
                                .buy(amount);
            }
            catch (Exception e) {
                assumeNoException(e);
            }
        }

        @Given("An active event and user JSON repository")
        @When("Selling tickets for JSON Repositories")
        @Then("The repositories should be updated accordingly")
        @Test
        void testJsonRepository() {
            // Act
            assertDoesNotThrow(() -> sellTickets(2));

            // Assert
            Event event = new EventsJson(eventsFile).byId(targetEvent()).get();
            User user = new UsersJson(usersFile).byId(targetUser()).get();

            assertEquals(2, event.boxOffice().capacity());
            assertEquals(2, event.boxOffice().sales());
            assertEquals(0, event.boxOffice().available());
            assertTrue(event.boxOffice().isSoldOut());
            assertEquals(1, user.tickets().size());
            assertEquals(targetEvent(), user.tickets().get(0).event());
        }

        @AfterAll
        static void removeFiles() {
            eventsFile.delete();
            usersFile.delete();
        }

    }

}
