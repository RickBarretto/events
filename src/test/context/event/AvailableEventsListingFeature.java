package test.context.event;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

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

@Feature("Listing available events")
@Given("A group of available and not available Events")
public class AvailableEventsListingFeature {
    LocalDate today;
    Events allEvents;
    List<Event> listedEvents;

    List<Event> available;
    List<Event> unavailable;

    List<Event> available() {
        return List.of(
                new Event(new Poster("Available", "...",
                        LocalDate.of(2024, 10, 15))),
                new Event(new Poster("Available", "...",
                        LocalDate.of(2024, 10, 16))),
                new Event(new Poster("Available", "...",
                        LocalDate.of(2024, 11, 15))),
                new Event(new Poster("Available", "...",
                        LocalDate.of(2024, 10, 15))),
                new Event(new Poster("Available", "...",
                        LocalDate.of(2030, 1, 15))));
    }

    List<Event> unavailable() {
        return List.of(
                new Event(new Poster("Available", "...",
                        LocalDate.of(2023, 10, 15))),
                new Event(new Poster("Available", "...",
                        LocalDate.of(2024, 10, 14))),
                new Event(new Poster("Available", "...",
                        LocalDate.of(2024, 9, 16))),
                new Event(new Poster("Available", "...",
                        LocalDate.of(2022, 10, 15))));
    }

    @BeforeEach
    void init() {
        this.today = LocalDate.of(2024, 10, 15);
        this.available = available();
        this.unavailable = unavailable();
        this.allEvents = new EventsInMemory();

        for (Event event : available) {
            this.allEvents.register(event);
        }

        for (Event event : unavailable) {
            this.allEvents.register(event);
        }
    }

    @When("When listing available events")
    void whenListing() {
        this.listedEvents = new AvailableEventsListing().from(allEvents)
                .beingToday(today).availables();
    }

    @Test
    @Then("Listed Events should include all available ones")
    void shouldIncludeAvailables() {
        whenListing();
        assertTrue(
            listedEvents.stream().allMatch((listed) -> available.contains(listed))
        );
    }

    @Test
    @Then("Listed Events should ignore all unavailable ones")
    void shouldIgnorePastEvents() {
        whenListing();
        assertFalse(listedEvents.stream()
                .anyMatch((listed) -> unavailable.contains(listed)));
    }

}
