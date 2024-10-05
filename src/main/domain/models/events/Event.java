package main.domain.models.events;

public class Event {
    private Poster poster;

    public Event(Poster poster) { this.poster = poster; }

    public Poster poster() { return poster; }
}
