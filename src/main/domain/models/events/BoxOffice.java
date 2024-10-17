package main.domain.models.events;

import main.domain.exceptions.SoldOut;

public class BoxOffice {
    private final Ticket sample;
    private final Integer capacity;
    private Integer sales;

    public BoxOffice(Ticket ticket) {
        this(ticket, 0, 0);
    }

    private BoxOffice(Ticket ticket, Integer capacity, Integer sales) {
        this.sample = ticket;
        this.capacity = capacity;
        this.sales = sales;
    }

    public BoxOffice ofCapacity(Integer amount) {
        assert amount >= 0;
        return new BoxOffice(sample, amount, sales);
    }

    public Ticket ticket(Integer amount) {
        return sample.copy().packedFor(amount);
    }

    public void sell(Ticket ticket) throws SoldOut {
        assert this.sales <= this.capacity;

        if ((this.sales + ticket.availableFor()) > this.capacity)
            throw new SoldOut();

        this.sales += ticket.availableFor();
    }

    public void refund(Ticket ticket) {
        assert sales >= ticket.availableFor();
        assert ticket.sameEvent(sample);

        this.sales -= ticket.availableFor();
        assert sales >= 0;
    }

    public Integer available() {
        var result = this.capacity - this.sales;

        assert result >= 0;
        return result;
    }

    public boolean isSoldOut() {
        return capacity == sales;
    }

    public Integer sales() { return sales; }

    public Integer capacity() { return capacity; }
}
