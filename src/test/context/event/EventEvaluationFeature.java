package test.context.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.domain.contexts.events.EventEvaluation;
import main.domain.exceptions.TryingToEvaluateActiveEvent;
import main.domain.models.evaluations.Evaluation;
import main.domain.models.events.Event;
import main.domain.models.events.Poster;
import main.domain.models.users.User;
import main.infra.virtual.EventsInMemory;
import main.roles.repositories.Events;
import test.resources.bdd.*;
import test.resources.entities.ConcreteUsers;

@Feature("Evaluate some Event by some User")
public class EventEvaluationFeature {
    private final LocalDate postEventDate = LocalDate.of(2024, 10, 16);
    private final LocalDate preEventDate = LocalDate.of(2024, 10, 15);

    private Events events;
    private Event event;
    private User author;

    @BeforeEach
    void init() {
        event = new Event(new Poster("From Zero", "Linkin Park Show",
                LocalDate.of(2024, 10, 15)));
        author = ConcreteUsers.JohnDoe();
        events = new EventsInMemory();
        events.register(event);
    }

    void evaluate(LocalDate date) throws TryingToEvaluateActiveEvent {
        new EventEvaluation(events, date)
                .of(event.id())
                .by(author.id())
                .evaluateWith("The show is simply perfect!");
    }

    @Scenario("Evaluating an Inactive Event")
    @Given("Some Inactive Event without evaluations")
    @And("A logged User")
    @When("Evaluating this Event")
    @Then("Evaluation should be registered")
    @Test
    void shouldBeRegistered() {
        // Given
        assumeEventHasNoEvaluation();
        // When
        assertDoesNotThrow(() -> evaluate(postEventDate));
        // Then
        final List<Evaluation> evaluations = events.byId(event.id()).get()
                .evaluations();

        final Evaluation evaluation = evaluations.get(0);
        assertTrue(evaluations.size() == 1, "Event has exactly one evaluation");
        assertAll("The evaluation is the same",
                () -> assertEquals("The show is simply perfect!",
                        evaluation.comment()),
                () -> assertEquals(author.id(), evaluation.author()),
                () -> assertEquals(event.id(), evaluation.event()));
    }

    @Scenario("Evaluating an Active Event")
    @Given("Some Active  Event without evaluations")
    @And("A logged User")
    @When("Evaluating this Event")
    @Then("Should throw TryingToEvaluateActiveEvent")
    @And("Event should not be evaluated")
    @Test
    void shouldNotEvaluate() {
        // Given
        assumeEventHasNoEvaluation();
        // When
        assertThrowsExactly(TryingToEvaluateActiveEvent.class,
                () -> evaluate(preEventDate));
        // Then
        final List<Evaluation> evaluations = events.byId(event.id()).get()
                .evaluations();

        assertTrue(evaluations.size() == 0, "Event was not evaluated");
    }

    @Assume("Event has no evaluations")
    private void assumeEventHasNoEvaluation() {
        assumeTrue(events.byId(event.id()).get().evaluations().isEmpty());
    }
}
