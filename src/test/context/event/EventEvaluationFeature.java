package test.context.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.domain.contexts.events.EventEvaluation;
import main.domain.models.evaluations.Evaluation;
import main.domain.models.events.Event;
import main.domain.models.events.Poster;
import main.domain.models.users.User;
import main.infra.virtual.EventsInMemory;
import main.roles.repositories.Events;
import test.resources.bdd.*;
import test.resources.entities.ConcreteUsers;

@Feature("Evaluate some Event by some User")
@Given("Some Event without evaluations")
public class EventEvaluationFeature {

    Events events;
    Event event;
    User author;

    @BeforeEach
    void init() {
        event = new Event(new Poster("From Zero", "Linkin Park Show",
                LocalDate.of(2024, 10, 15)));
        author = ConcreteUsers.JohnDoe();
        events = new EventsInMemory();
        events.register(event);
    }

    void evaluate() {
        new EventEvaluation().of(event.id()).from(events).by(author.id())
                .evaluateWith("The show is simply perfect!");
    }

    @And("A logged User")
    @When("When evaluating this Event")
    @Then("Evaluation should be registered")
    @Test
    void shouldBeRegistered() {
        // Given
        assumeEventHasNoEvaluation();
        // When
        evaluate();
        // Then
        final List<Evaluation> evaluations = events.byId(event.id()).get()
                .evaluations();
        final Evaluation evaluation = evaluations.get(0);

        assertTrue("Event has exactly one evaluation", 1 == evaluations.size());
        assertAll("The evaluation is the same",
                () -> assertEquals("The show is simply perfect!",
                        evaluation.comment()),
                () -> assertEquals(author.id(), evaluation.author()),
                () -> assertEquals(event.id(), evaluation.event()));
    }

    @Assume("Event has no evaluations")
    private void assumeEventHasNoEvaluation() {
        assumeTrue(events.byId(event.id()).get().evaluations().isEmpty());
    }
}
