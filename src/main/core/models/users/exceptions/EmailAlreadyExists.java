package main.core.models.users.exceptions;

public class EmailAlreadyExists 
extends Exception
{
    public EmailAlreadyExists(String msg)
    {
        super(msg);
    }
}
