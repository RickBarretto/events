package main.core.models.users.types;

public final class Account 
{
    private Email email;
    private String password;

    public Account(Email email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public Email email()
    {
        return email;
    }

    public boolean hasLogin(Email email, String rawPassword) 
    {
        if (!(this.email.toString().equals(email.toString()))) return false;
        return this.password.equals(rawPassword);
    }
}
