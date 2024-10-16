package main.infra.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import main.domain.models.events.Event;
import main.domain.models.events.EventId;
import main.infra.virtual.EventsInMemory;
import main.roles.repositories.Events;

public class EventsJson implements Events {
    private final JsonFile file;
    private EventsInMemory events;

    public EventsJson(JsonFile filepath) {
        this(filepath, new EventsInMemory(load(filepath)));
    }

    public EventsJson(JsonFile file, EventsInMemory repository) {
        this.file = file;
        this.events = repository;
        persist();
    }

    private static List<Event> load(JsonFile file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return new Gson().fromJson(reader, new TypeToken<List<Event>>() {});
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void persist() {
        try (FileWriter writer = new FileWriter(file)) {
            new Gson().toJson(events.asList(), writer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Event> asList() { return events.asList(); }

    @Override
    public List<Event> availableFor(LocalDate date) {
        return events.availableFor(date);
    }

    @Override
    public Optional<Event> event(String title, LocalDate date) {
        return events.event(title, date);
    }

    @Override
    public boolean has(EventId id) { return events.has(id); }

    @Override
    public boolean has(String title, LocalDate inDate) {
        return events.has(title, inDate);
    }

    @Override
    public void register(Event event) {
        events.register(event);
        persist();
    }
}
