package main.core.models.events;

import java.time.LocalDate;

import main.core.models.events.types.EventID;

public class Event 
{
    private EventID id;
    private String title;
    private String description;
    private LocalDate date;

    public Event(EventID id, String title, String description, LocalDate date)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public EventID id()
    {
        return id;
    }

    public String title()
    {
        return title;
    }

    public String description()
    {
        return description;
    }

    public LocalDate date()
    {
        return date;
    }
}
