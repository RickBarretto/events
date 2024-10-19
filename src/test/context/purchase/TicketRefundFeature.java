package test.context.purchase;

import static org.junit.Assume.assumeNoException;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import test.resources.bdd.Feature;
import test.resources.bdd.Given;
import test.resources.bdd.Scenario;
import test.resources.bdd.Then;
import test.resources.bdd.When;

@Feature("Ticket Refund")
public class TicketRefundFeature {

    private Events events;
    private Users users;

    @BeforeEach
    void loadRepositories() {
        var event = new Event(new Poster("From Zero", "A LP show",
                LocalDate.of(2024, 10, 15)));
        event.addCapacity(2);
        events = new EventsInMemory(List.of(event));
        users = new UsersInMemory(
                List.of(new User(new Login("john.doe@example.com", "123456"),
                        new Person("John Doe", "000.000.000-00"))));
    }

    EventId targetEvent() { return events.list().get(0).id(); }

    UserId targetUser() { return users.list().get(0).id(); }

    void sellTickets(Integer amount) throws SoldOut {
        new TicketBuying(events, users).of(targetEvent()).by(targetUser())
                .via(new PaymentMethod("...", "...")).buy(amount);
    }

    void refundFirstTicket() {
        new TicketRefund(events, users).to(targetUser())
                .via(new PaymentMethod("...", "..."))
                .owning(users.byId(targetUser()).get().tickets().get(0))
                .refund();
    }

    @Scenario("Refunding Tickets")
    @Given("One couple tickets sold")
    @Assume("Two tickets have been sold")
    @When("Refunding one ticket for couples")
    @Then("Event should have 2 available tickets and user 0 tickets")
    @Test
    void shouldBeEmptyForCouple() {
        try {
            sellTickets(2);
        }
        catch (Exception e) {
            assumeNoException(e);
        }

        // When
        refundFirstTicket();

        // Then
        assertTrue(
                events.byId(targetEvent()).get().boxOffice().available() == 2);
        assertTrue(users.byId(targetUser()).get().tickets().isEmpty());
    }

    @Scenario("Refunding Individual Tickets")
    @Given("Two individual tickets have been sold")
    @Assume("Two tickets have been sold")
    @When("Refunding one ticket of two individual ones")
    @Then("Event should have 1 available ticket and user 1 ticket")
    @Test
    void shouldBeEmptyForIndividual() {
        try {
            sellTickets(1);
            sellTickets(1);
        }
        catch (Exception e) {
            assumeNoException(e);
        }

        // When
        refundFirstTicket();

        // Then
        assertTrue(
                events.byId(targetEvent()).get().boxOffice().available() == 1);
        assertTrue(users.byId(targetUser()).get().tickets().size() == 1);
    }
}
