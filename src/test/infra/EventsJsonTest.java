package test.infra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import main.domain.models.evaluations.Evaluation;
import main.domain.models.events.Event;
import main.domain.models.events.Poster;
import main.domain.models.users.UserId;
import main.infra.json.EventsJson;
import main.infra.json.JsonFile;
import main.infra.virtual.EventsInMemory;
import test.resources.bdd.Then;

// @formatter:off
public class EventsJsonTest {
    private static final String directory = "src/test/infra/resources";

    @AfterAll
    static void removeFiles() {
        new JsonFile(directory, "all-events").delete();
        new JsonFile(directory, "all-evaluated-events").delete();
        new JsonFile(directory, "none-events").delete();
    }

    @Nested
    class ExistentRepository {
        private final JsonFile file = new JsonFile(directory, "all-events");
        private EventsJson allEventsJson;

        @BeforeEach
        void createJsonFile() {
            new EventsJson(file, new EventsInMemory(
                List.of(
                    new Event(new Poster(
                        "From Zero Tour",
                        "LP show", 
                        LocalDate.of(2024, 11, 15)
                    ))
                )
            ));
        }

        @BeforeEach
        void loadJsonFile() {
            assumeTrue(file.exists());
            this.allEventsJson = new EventsJson(file);
        }

        @Test
        void shouldContainEvent() {
            assertTrue(1 == this.allEventsJson.list().size());
            assertTrue(this.allEventsJson.has("From Zero Tour", LocalDate.of(2024, 11, 15)));
            assertTrue(this.allEventsJson.event("From Zero Tour", LocalDate.of(2024, 11, 15)).isPresent());
        }
        
        @Test
        void shouldRegister() {
            registerExtraShow();
            assertTrue(this.allEventsJson.has("From Zero (Extra show)", LocalDate.of(2024, 11, 16)));
            assertTrue(this.allEventsJson.event("From Zero (Extra show)", LocalDate.of(2024, 11, 16)).isPresent());
        }
        
        @Test
        @Then("Should load list from file")
        void shouldLoadFromFile() {
            registerExtraShow();
            
            var otherReference = new EventsJson(file);
            assertTrue(otherReference.event(
                "From Zero (Extra show)", 
                LocalDate.of(2024, 11, 16)
            ).isPresent());
        }

        void registerExtraShow() {
            this.allEventsJson.register(new Event(new Poster(
                "From Zero (Extra show)",
                "Extra show of LP",
                LocalDate.of(2024, 11, 16)
            )));
        }
    }

    @Nested
    class EmptyRepository {
        private final JsonFile file = new JsonFile(directory, "none-events");
        private EventsJson noEvents;

        @BeforeEach
        void createJsonFile() {
            new EventsJson(file, new EventsInMemory(List.of()));
        }
        
        @BeforeEach
        void loadJsonFile() {
            assumeTrue(file.exists());
            this.noEvents = new EventsJson(file);
        }

        @Test
        @Then("Internal list should be empty")
        void shouldBeEmpty() {
            assertTrue(this.noEvents.list().isEmpty());
        }
        
        @Test
        @Then("Should load an empty list from the json file")
        void shouldLoadEmptyListFromFile() {
            var otherReference = new EventsJson(file);
            assertTrue(otherReference.list().isEmpty());
        }
    }


    @Nested
    class RepositoryOfEvaluatedEvents {
        private final JsonFile file = new JsonFile(directory, "all-evaluated-events");
        private Event event;
        private EventsJson events;

        @BeforeEach
        void createJsonFile() {
            event = new Event(new Poster(
                "From Zero Tour",
                "LP show", 
                LocalDate.of(2024, 11, 15)
            ));
            new EventsJson(file, new EventsInMemory(List.of(event)));
        }

        @BeforeEach
        void loadJsonFile() {
            assumeTrue(file.exists());
            this.events = new EventsJson(file);
        }

        @Test
        void shouldUpdate() {
            var author = new UserId();
            event.receiveEvaluation(new Evaluation(event.id(), author, "Good show!"));
            events.update(event);

            var newEvents = new EventsJson(file);
            var storedEvaluation = newEvents.byId(event.id()).get().evaluations().get(0);
            assertAll("Evaluation was stored",
                () -> assertEquals("Good show!", storedEvaluation.comment()),
                () -> assertEquals(author, storedEvaluation.author()),
                () -> assertEquals(event.id(), storedEvaluation.event())
                    
            );
        }   
    }
}
