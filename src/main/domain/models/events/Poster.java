package main.domain.models.events;

import java.time.LocalDate;

public class Poster {
    private final String title;
    private final String description;
    private final String schedule;

    /**
     * @param title
     * @param description
     * @param schedule
     */
    public Poster(String title, String description, LocalDate schedule) {
        this.title = title;
        this.description = description;
        this.schedule = schedule.toString();
    }

    public String title() { return title; }

    public String description() { return description; }

    public LocalDate date() { return LocalDate.parse(schedule); }

}
