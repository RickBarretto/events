package main.domain.models.events;

public class Event {
    private EventId id;
    private Poster poster;

    public Event(Poster poster) { this.poster = poster; }

    public EventId id() { return id; }

    public Poster poster() { return poster; }
}
