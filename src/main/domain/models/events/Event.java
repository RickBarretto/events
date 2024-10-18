package main.domain.models.events;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import main.domain.models.evaluations.Evaluation;
import main.roles.Entity;

public class Event implements Entity<EventId> {
    private final EventId id = new EventId();
    private final Poster poster;
    private BoxOffice boxOffice;
    private ArrayList<Evaluation> evaluations;

    public Event(Poster poster) { this(poster, 0.0); }

    public Event(Poster poster, Double price) {
        this.poster = poster;
        this.boxOffice = new BoxOffice(new Ticket(id, price));
        this.evaluations = new ArrayList<>();
    }

    public void receiveEvaluation(Evaluation evaluation) {
        this.evaluations.add(evaluation);
    }

    public void addCapacity(Integer capacity) {
        this.boxOffice = this.boxOffice.ofCapacity(capacity);
    }

    public boolean isAvailableFor(LocalDate date) {
        var schedule = poster.date();
        return !schedule.isBefore(date);
    }

    public EventId id() { return id; }

    public Poster poster() { return poster; }

    public BoxOffice boxOffice() { return boxOffice; }

    public List<Evaluation> evaluations() { return List.copyOf(evaluations); }

}
