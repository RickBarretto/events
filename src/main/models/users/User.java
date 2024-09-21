package main.models.users;

public class User 
{
    private Person person;
    private UserAccount account;

    public User(Person person, UserAccount account)
    {
        this.person = person;
        this.account = account;
    }

    public static User generic()
    {
        Person person = Person.generic();
        UserAccount account = UserAccount.generic();
        return new User(person, account);
    }

}
