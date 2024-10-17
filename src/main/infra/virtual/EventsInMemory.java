package main.infra.virtual;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import main.domain.models.events.Event;
import main.domain.models.events.EventId;
import main.roles.repositories.Events;

class EventIndex {
    String title;
    LocalDate date;

    public EventIndex(String left, LocalDate right) {
        this.title = left;
        this.date = right;
    }

    public String left() { return title; }

    public LocalDate right() { return date; }

    @Override
    public int hashCode() { return Objects.hash(title, date); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof EventIndex))
            return false;
        EventIndex other = (EventIndex) obj;
        return Objects.equals(title, other.title)
                && Objects.equals(date, other.date);
    }
}

public class EventsInMemory implements Events {
    private HashMap<EventId, Event> events = new HashMap<>();
    private HashMap<EventIndex, EventId> infoIndex = new HashMap<>();

    public EventsInMemory() { this(List.of()); }

    public EventsInMemory(List<Event> eventsList) {
        eventsList.forEach(event -> register(event));
    }

    @Override
    public void register(Event event) {
        var index = new EventIndex(event.poster().title(),
                event.poster().date());
        this.events.put(event.id(), event);
        this.infoIndex.put(index, event.id());
    }

    @Override
    public void update(Event event) {
        events.replace(event.id(), event);
    }

    @Override
    public List<Event> list() { return List.copyOf(events.values()); }

    @Override
    public List<Event> availableOn(LocalDate date) {
        return events.values().stream()
                .filter(event -> event.isAvailableFor(date)).toList();
    }

    @Override
    public Optional<Event> byId(EventId id) {
        return Optional.ofNullable(events.get(id));
    }

    @Override
    public Optional<Event> event(String title, LocalDate date) {
        var index = new EventIndex(title, date);
        return this.byId(infoIndex.get(index));
    }

    @Override
    public boolean has(EventId id) { return events.containsKey(id); }

    @Override
    public boolean has(String title, LocalDate inDate) {
        return infoIndex.containsKey(new EventIndex(title, inDate));
    }
}
