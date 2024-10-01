package main.core.contexts.users;

public class UserAlreadyRegistered 
extends Exception
{
    public UserAlreadyRegistered(String msg)
    {
        super(msg);
    }
}
