package main.models.users;

import java.util.Objects;

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

    public UserAccount account()
    {
        return account;
    }

    public Person person()
    {
        return person;
    }    
    
    
    @Override
    public int hashCode() {
        return Objects.hash(person, account);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof User))
            return false;
        User other = (User) obj;
        return Objects.equals(person, other.person) && Objects.equals(account, other.account);
    }
}
