package test.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import main.domain.contexts.events.CantRegisterPastEvent;
import main.domain.contexts.events.EventAlreadyRegistered;
import main.domain.contexts.events.EventRegistering;
import main.domain.contexts.events.PosterForms;
import main.domain.exceptions.PermissionDenied;
import main.domain.models.events.Event;
import main.domain.models.events.Poster;
import main.domain.models.users.Login;
import main.domain.models.users.Person;
import main.domain.models.users.User;
import main.infra.VirtualEventRepository;
import main.roles.EventRepository;
import test.resources.*;

// @formatter:off
public class EventRegisteringFeature {
    private EventRepository repository;

    @BeforeEach
    void emptyRepository() {
        repository = new VirtualEventRepository();
    }

    User someCustomer() {
        return new User(
            new Login("john.doe@example.com", "123456"),
            new Person("John Doe", "000.000.000-00")
        );
    }

    User someAdmin() {
        return someCustomer().asAdmin();
    }

    Poster somePoster() {
        return new PosterForms()
            .title("From Zero")
            .description("Linkin Park's show")
            .scheduledFor(LocalDate.of(2024, 10, 15))
            .submit();
    }

    Optional<Event> actualEvent(EventRepository repository) {
        return repository.event(
            "From Zero", 
            LocalDate.of(2024, 10, 15)
        );
    }


    @Nested
    @Scennario("Sucessfully registering an Event")
    @Given("Some Post, an Admin user and the Current Day")
    class Sucessful {
        
        @When("Registering a new future Event as an Admin")
        void register()
        throws PermissionDenied, EventAlreadyRegistered, CantRegisterPastEvent 
        {
            var poster = somePoster();
            var user = someAdmin();
            var currentDay = LocalDate.of(2024, 10, 1);

            new EventRegistering()
                .into(repository)
                .poster(poster)
                .by(user)
                .on(currentDay)
                .register();
        }

        @Test
        @Then(
            "Should register if User is Admin," +
            "And is a future Event" +
            "And Event's title and date is Available"
            )
        void shouldRegister() {
            Supplier<Boolean> isRegistered = () -> 
                repository.has("From Zero", LocalDate.of(2024, 10, 15));
            
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
    class UnsucessfulForCustomer {
        
    }

    @Nested
    class UnsucessfulForAlreadyRegistered {
        
    }
    
    @Nested
    class UnsucessfulForPastEvent {

    }

}
