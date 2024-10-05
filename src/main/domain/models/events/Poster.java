package main.domain.models.events;

import java.time.LocalDate;

public class Poster {
    private String title;
    private String description;
    private LocalDate schedule;

    /**
     * @param title
     * @param description
     * @param schedule
     */
    public Poster(String title, String description, LocalDate schedule) {
        this.title = title;
        this.description = description;
        this.schedule = schedule;
    }

    public String title() { return title; }

    public String description() { return description; }

    public LocalDate date() { return schedule; }

}
