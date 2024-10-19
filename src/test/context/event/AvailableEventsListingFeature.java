package test.context.event;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.domain.contexts.events.AvailableEventsListing;
import main.domain.models.events.Event;
import main.domain.models.events.Poster;
import main.infra.virtual.EventsInMemory;
import main.roles.repositories.Events;
import test.resources.bdd.Feature;
import test.resources.bdd.Given;
import test.resources.bdd.Then;
import test.resources.bdd.When;

class AvailableEvents {
    private static final List<Event> events = List.of(
            new Event(
                    new Poster("Available", "...", LocalDate.of(2024, 10, 15))),
            new Event(
                    new Poster("Available", "...", LocalDate.of(2024, 10, 16))),
            new Event(
                    new Poster("Available", "...", LocalDate.of(2024, 11, 15))),
            new Event(
                    new Poster("Available", "...", LocalDate.of(2024, 10, 15))),
            new Event(
                    new Poster("Available", "...", LocalDate.of(2030, 1, 15))));

    public static List<Event> all() { return events; }
}

class UnavailableEvents {
    private static final List<Event> events = List.of(
            new Event(new Poster("Unavailable", "...",
                    LocalDate.of(2023, 10, 15))),
            new Event(new Poster("Unavailable", "...",
                    LocalDate.of(2024, 10, 14))),
            new Event(new Poster("Unavailable", "...",
                    LocalDate.of(2024, 9, 16))),
            new Event(new Poster("Unavailable", "...",
                    LocalDate.of(2022, 10, 15))));

    public static List<Event> all() { return events; }
}

@Feature("Listing available events")
public class AvailableEventsListingFeature {
    LocalDate today;
    Events allEvents;
    List<Event> listedEvents;

    @BeforeEach
    void init() {
        this.today = LocalDate.of(2024, 10, 15);

        // @formatter:off
        this.allEvents = new EventsInMemory(
            Stream.concat(
                AvailableEvents.all().stream(), UnavailableEvents.all().stream()
            ).collect(Collectors.toList()
        ));
        // @formatter:on
    }

    void listEvents() {
        // @formatter:off
        this.listedEvents = new AvailableEventsListing()
            .from(allEvents)
            .beingToday(today)
            .availables();
        // @formatter:on
    }

    @Test
    @Given("Available and Unavailable Events")
    @When("When listing Available Events")
    @Then("Listed Events should include all available ones")
    void shouldIncludeAvailables() {
        listEvents();
        assertTrue(listedEvents.stream()
                .allMatch((listed) -> AvailableEvents.all().contains(listed)));
    }

    @Test
    @Given("Available and Unavailable Events")
    @When("When listing Available Events")
    @Then("Listed Events should ignore all unavailable ones")
    void shouldIgnorePastEvents() {
        listEvents();
        assertFalse(listedEvents.stream().anyMatch(
                (listed) -> UnavailableEvents.all().contains(listed)));
    }

}
