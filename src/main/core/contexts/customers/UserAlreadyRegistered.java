package main.core.contexts.customers;

public class UserAlreadyRegistered 
extends Exception
{
    public UserAlreadyRegistered(String msg)
    {
        super(msg);
    }
}
