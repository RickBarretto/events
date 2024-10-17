package main.domain.models.events;

import java.util.Objects;

public class Ticket {
    private final EventId event;
    private final Integer availableFor;

    public Ticket(EventId event) { this(event, 1); }

    private Ticket(EventId event, Integer availableFor) {
        this.event = event;
        this.availableFor = availableFor;
    }

    public Ticket copy() { return new Ticket(event, availableFor); }

    public Ticket packedFor(Integer persons) {
        assert persons > 0;
        return new Ticket(event, persons);
    }

    public EventId event() { return event; }

    public Integer availableFor() { return availableFor; }

    @Override
    public int hashCode() { return Objects.hash(event, availableFor); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Ticket))
            return false;
        return sameEvent((Ticket) obj);
    }

    public boolean sameEvent(Ticket other) {
        return Objects.equals(event, other.event);
    }

}
