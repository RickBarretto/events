package test.context.event;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import main.domain.contexts.events.EventRegistering;
import main.domain.contexts.events.forms.EventInformation;
import main.domain.exceptions.CantRegisterPastEvent;
import main.domain.exceptions.EventAlreadyRegistered;
import main.domain.exceptions.PermissionDenied;
import main.domain.models.events.Event;
import main.domain.models.events.Poster;
import main.domain.models.users.User;
import main.infra.virtual.EventsInMemory;
import main.roles.repositories.Events;
import test.resources.bdd.*;
import test.resources.entities.ConcreteUsers;

public class EventRegisteringFeature {

    private Events repository;

    @BeforeEach
    void emptyRepository() { repository = new EventsInMemory(); }

    User someCustomer() { return ConcreteUsers.JohnDoe(); }

    User someAdmin() { return someCustomer().asAdmin(); }

    Poster somePoster() {
        return new EventInformation().title("From Zero")
                .description("Linkin Park's show")
                .scheduledFor(LocalDate.of(2024, 10, 15)).submit();
    }

    Optional<Event> actualEvent(Events repository) {
        return repository.event("From Zero", LocalDate.of(2024, 10, 15));
    }

    @Nested
    @Scennario("Successfully registering an Event")
    @Given("Some Post, an Admin user and the Current Day")
    class Successful {

        @When("Registering a new future Event as an Admin")
        void register() throws PermissionDenied, EventAlreadyRegistered,
                CantRegisterPastEvent {
            var poster = somePoster();
            var user = someAdmin();
            var currentDay = LocalDate.of(2024, 10, 1);
            new EventRegistering().into(repository).poster(poster).by(user)
                    .on(currentDay).register();
        }

        @Test
        @Then("Should register if User is Admin," + "And is a future Event"
                + "And Event's title and date is Available")
        void shouldRegister() {
            Supplier<Boolean> isRegistered = () -> repository.has("From Zero",
                    LocalDate.of(2024, 10, 15));

            // Precondition
            assumeFalse("Event must not be registered", isRegistered.get());

            // Do
            assertDoesNotThrow(() -> this.register());

            // Assertions
            var event = actualEvent(repository);
            assertTrue("Title and Date are now registered", isRegistered.get());
            assertTrue("Event is present", event.isPresent());
        }

        @Test
        @And("Registered event is the same")
        void shouldBeTheSame() {
            // Do
            assertDoesNotThrow(() -> this.register());

            // Assertions
            var event = actualEvent(repository).get();
            assertEquals("From Zero", event.poster().title());
            assertEquals("Linkin Park's show", event.poster().description());
            assertEquals(LocalDate.of(2024, 10, 15), event.poster().date());
        }
    }

    @Nested
    @Scennario("Customer trying to register an Event")
    @Given("Some Post, a Customer User and the Current Day")
    class UnsuccessfulForCustomer {

        @When("Trying to register a new future Event as a Customer")
        void tryRegister() throws PermissionDenied {
            var poster = somePoster();
            var user = someCustomer();
            var currentDay = LocalDate.of(2024, 10, 1);
            new EventRegistering().into(repository).poster(poster).by(user)
                    .on(currentDay);
        }

        @Then("Should not register and throw PermissionDenied")
        @Test
        void shouldNotAllow() {
            assertThrows(PermissionDenied.class, () -> tryRegister());
            assertFalse(
                    repository.has("From Zero", LocalDate.of(2024, 10, 15)));
        }
    }

    @Nested
    @Scennario("Admin trying to re-register an Event")
    @Given("Some Post, an Admin User and the Current Day")
    class UnsuccessfulForAlreadyRegistered {

        @When("Trying to register an already registered Event")
        void registerTwice() throws PermissionDenied, EventAlreadyRegistered,
                CantRegisterPastEvent {
            var poster = somePoster();
            var user = someAdmin();
            var currentDay = LocalDate.of(2024, 10, 1);
            var context = new EventRegistering().into(repository).poster(poster)
                    .by(user).on(currentDay);
            context.register();
            context.register();
        }

        @Test
        @Then("Should not re-register and throw EventAlreadyRegistered")
        void shouldNotAllow() {
            assertThrows(EventAlreadyRegistered.class, () -> registerTwice());
            assertTrue(repository.has("From Zero", LocalDate.of(2024, 10, 15)));
        }
    }

    @Nested
    @Scennario("An Admin trying to register a past Event")
    @Given("Some Post, an Admin User and the Current Day")
    class UnsuccessfulForPastEvent {

        @When("Trying to register a past Event")
        void tryRegister(LocalDate today) throws PermissionDenied,
                EventAlreadyRegistered, CantRegisterPastEvent {
            var poster = somePoster();
            var user = someAdmin();
            new EventRegistering().into(repository).poster(poster).by(user)
                    .on(today).register();
        }

        @Then("Should not register on same day")
        @Test
        void shouldNotRegisterForSameDate() {
            // Pre-Condition
            var today = LocalDate.of(2024, 10, 15);
            assumeTrue(somePoster().date().isEqual(today));

            // Do
            assertThrows(CantRegisterPastEvent.class, () -> tryRegister(today));

            // Post-Condition
            assertFalse(
                    repository.has("From Zero", LocalDate.of(2024, 10, 15)));
        }

        @Then("Should not register for past days")
        @Test
        void shouldNotRegisterForPastDay() {
            // Pre-Condition
            var today = LocalDate.of(2024, 10, 16);
            assumeTrue(somePoster().date().isBefore(today));

            // Do
            assertThrows(CantRegisterPastEvent.class, () -> tryRegister(today));

            // Post-Condition
            assertFalse(
                    repository.has("From Zero", LocalDate.of(2024, 10, 15)));
        }
    }
}
