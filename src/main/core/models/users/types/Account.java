package main.core.models.users.types;

public class Account 
{
    private Username username;
    private Email email;
    private String password;

    public Account(Username username, Email email, String password)
    {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Email email()
    {
        return email;
    }

    public Username username()
    {
        return username;
    }

    public boolean hasLogin(String loginOrEmail, String rawPassword) 
    {
        var isSameLogin = this.username.toString().equals(loginOrEmail);
        var isSameEmail = this.email.toString().equals(loginOrEmail);

        if (!(isSameEmail || isSameLogin)) return false;

        return this.password.equals(rawPassword);
    }
}
