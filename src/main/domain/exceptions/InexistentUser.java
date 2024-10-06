package main.domain.exceptions;

public class InexistentUser extends Exception {
    public InexistentUser() { super(); }

    public InexistentUser(String msg) { super(msg); }
}
