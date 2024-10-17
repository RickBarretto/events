package main.domain.models.events;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import main.domain.models.evaluations.Evaluation;
import main.roles.Entity;

public class Event implements Entity<EventId> {
    private final EventId id = new EventId();
    private ArrayList<Evaluation> evaluations = new ArrayList<>();
    private final Poster poster;
    private BoxOffice boxOffice;

    public Event(Poster poster) { 
        this.poster = poster;
        this.boxOffice = new BoxOffice(new Ticket(id));
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
