package main.models.users;

public class UserAccount 
{
    private String username;
    private String email;
    private String password;
    private Boolean admin;

    /**
     * @param username
     * @param email
     * @param password
     */
    public UserAccount(String username, String email, String password) 
    {
        this.username = username;
        this.email = email;
        this.password = password;
        this.admin = false;
    }

    public static UserAccount generic()
    {
        return new UserAccount("john.doe", "john.doe@example.com", "J04nD03123");
    }
    
    public UserAccount asAdmin()
    {
        this.admin = true;
        return this;
    }

    public String username()
    {
        return username;
    }
    
    public String email()
    {
        return email;
    }

    public Boolean isAdmin()
    {
        return admin;
    }
    
    public Boolean isOwner(String loginOrEmail, String password)
    {
        if (!(loginOrEmail.equals(username) || loginOrEmail.equals(email))) return false;
        return this.password.equals(password);
    }

}
