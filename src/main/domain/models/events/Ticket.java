package main.domain.models.events;

public class Ticket {
    private final Poster event;
    private final Integer availableFor;

    public Ticket(Poster event) { this(event, 1); }

    private Ticket(Poster event, Integer availableFor) {
        this.event = event;
        this.availableFor = availableFor;
    }

    public Ticket packedFor(Integer persons) {
        assert persons > 0;
        return new Ticket(event, persons);
    }

    public Poster event() { return event; }

    public Integer availableFor() { return availableFor; }
}
