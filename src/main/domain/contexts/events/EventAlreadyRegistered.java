package main.domain.contexts.events;

public class EventAlreadyRegistered extends Exception {
    public EventAlreadyRegistered() { super(); }

    public EventAlreadyRegistered(String msg) { super(msg); }
}
