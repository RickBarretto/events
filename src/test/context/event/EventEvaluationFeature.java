package test.context.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.domain.contexts.events.EventEvaluation;
import main.domain.models.events.Event;
import main.domain.models.events.Poster;
import main.domain.models.users.Login;
import main.domain.models.users.Person;
import main.domain.models.users.User;
import main.infra.virtual.EventsInMemory;
import main.roles.repositories.Events;
import test.resources.bdd.Feature;
import test.resources.bdd.Then;
import test.resources.bdd.When;

// @formatter:off
@Feature("Evaluate some Event by some User")
public class EventEvaluationFeature {
    Events events;
    Event event;
    User author;

    @BeforeEach
    void init() {
        event = new Event(new Poster("From Zero", "Linkin Park Show",
                LocalDate.of(2024, 10, 15)));
        author = new User(new Login("john.doe@example.com", "123456"),
                new Person("John Doe", "000.000.000-00"));
        events = new EventsInMemory();
        events.register(event);
    }

    @When("When evaluating some Event as John Doe")
    void whenEvaluating() {
        new EventEvaluation()
            .of(event.id())
            .from(events)
            .by(author.id())
            .evaluateWith("The show is simply perfect!");
    }

    @Then("Evaluation should be registered")
    @Test
    void shouldBeRegistered() {
        assumeTrue(events.byId(event.id()).get().evaluations().isEmpty());
        whenEvaluating();

        assertTrue(1 == events.byId(event.id()).get().evaluations().size());
        assertAll("Are the same",
        () -> assertEquals(
            "The show is simply perfect!",
            events.byId(event.id()).get().evaluations().get(0).comment()),
        () -> assertEquals(
            author.id(),
            events.byId(event.id()).get().evaluations().get(0).author()),
        () -> assertEquals(
            event.id(),
            events.byId(event.id()).get().evaluations().get(0).event())
        );
    }
}
