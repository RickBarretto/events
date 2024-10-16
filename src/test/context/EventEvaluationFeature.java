package test.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.domain.contexts.events.EventEvaluation;
import main.domain.models.events.Event;
import main.domain.models.events.Poster;
import main.domain.models.users.Login;
import main.domain.models.users.Person;
import main.domain.models.users.User;
import main.infra.EvaluationsInMemory;
import main.roles.repositories.Evaluations;
import test.resources.*;
import test.resources.bdd.And;
import test.resources.bdd.Feature;
import test.resources.bdd.Then;
import test.resources.bdd.When;

@Feature("Evaluate some Event by some User")
public class EventEvaluationFeature {
    Evaluations evaluations;
    Event event;
    User author;

    @BeforeEach
    void init() {
        this.evaluations = new EvaluationsInMemory();
        this.event = new Event(new Poster("From Zero", "Linkin Park Show",
                LocalDate.of(2024, 10, 15)));
        this.author = new User(new Login("john.doe@example.com", "123456"),
                new Person("John Doe", "000.000.000-00"));
    }

    @When("When evaluating some Event as John Doe")
    void whenEvaluating() {
        new EventEvaluation().of(this.event).from(evaluations).by(this.author)
                .evaluateWith("The show is simply perfect!");
    }

    @Then("Evaluation should be registered")
    @Test
    void shouldBeRegistered() {
        assumeTrue(evaluations.wroteBy(author.id()).isEmpty());
        whenEvaluating();

        var evaluation = evaluations.wroteBy(author.id());
        assertTrue(evaluation.isPresent());
        assertEquals(author.id(), evaluation.get().author());
        assertEquals(event.id(), evaluation.get().event());
    }

    @And("Is the same")
    @Test
    void shouldBeTheSame() {
        whenEvaluating();
        var evaluation = evaluations.wroteBy(author.id()).get();

        assertEquals(author.id(), evaluation.author());
        assertEquals(event.id(), evaluation.event());
        assertEquals("The show is simply perfect!", evaluation.comment());
    }

}
