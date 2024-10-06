package main.domain.models.events;

import java.time.LocalDate;
import java.util.Objects;

public class Event {
    private EventId id;
    private Poster poster;

    public Event(Poster poster) {
        this.id = new EventId();
        this.poster = poster;
    }

    public boolean isAvailableFor(LocalDate date) {
        var schedule = poster.date();
        return !schedule.isBefore(date);
    }

    public EventId id() { return id; }

    public Poster poster() { return poster; }

    @Override
    public int hashCode() { return Objects.hash(id, poster); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Event))
            return false;
        Event other = (Event) obj;
        return Objects.equals(id, other.id)
                && Objects.equals(poster, other.poster);
    }

}
