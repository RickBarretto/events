package main.domain.models.events;

public class Ticket {
    private Poster event;
    private Integer availableFor;

    public Ticket(Poster event) {
        this.event = event;
        this.availableFor = 1;
    }

    public Ticket packedFor(Integer persons) {
        assert persons > 0;
        this.availableFor = persons;
        return this;
    }

    public Poster event() { return event; }

    public Integer availableFor() { return availableFor; }
}
