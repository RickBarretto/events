package main.domain.contexts.events;

public class CantRegisterPastEvent extends Exception {
    public CantRegisterPastEvent() { super(); }

    public CantRegisterPastEvent(String msg) { super(msg); }
}
