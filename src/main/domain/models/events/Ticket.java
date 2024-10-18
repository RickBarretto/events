package main.domain.models.events;

import java.util.Objects;

public class Ticket {
    private final EventId event;
    private final Double price;
    private final Integer availableFor;

    public Ticket(EventId event, Double price) { this(event, price, 1); }
    public Ticket(EventId event) { this(event, 0.0, 1); }

    private Ticket(EventId event, Double price, Integer availableFor) {
        this.event = event;
        this.price = price;
        this.availableFor = availableFor;
    }

    public Ticket copy() { return new Ticket(event, price, availableFor); }

    public Ticket packedFor(Integer persons) {
        assert persons > 0;
        return new Ticket(event, price, persons);
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
