package main.domain.exceptions;

public class PermissionDenied extends Exception {
    public PermissionDenied(String msg) { super(msg); }

    public PermissionDenied() { super(); }
}
