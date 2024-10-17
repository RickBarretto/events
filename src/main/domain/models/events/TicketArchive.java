package main.domain.models.events;

class SoldOut extends Exception { public SoldOut() { super(); } }

public class TicketArchive {
    private Poster poster;
    private Integer capacity;
    private Integer sold;

    private TicketArchive(Poster poster) {
        this.poster = poster;
        this.capacity = 0;
        this.sold = 0;
    }

    public TicketArchive ofCapacity(Integer amount) {
        assert amount >= 0;
        this.capacity = amount;
        return this;
    }

    public Ticket ticket() { return new Ticket(poster); }

    public Ticket tickets() { return new Ticket(poster).packedFor(2); }

    public void sell(Integer amount) throws SoldOut {
        assert this.sold <= this.capacity;

        if ((this.sold + amount) > this.capacity)
            throw new SoldOut();

        this.sold += amount;
    }

    public void sell() throws SoldOut { sell(1); }

    public void refund() {

    }

    public Integer available() {
        var result = this.capacity - this.sold;

        assert result >= 0;
        return result;
    }

    public Integer sold() { return sold; }

    public Integer capacity() { return capacity; }
}
