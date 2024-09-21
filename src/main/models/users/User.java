package main.models.users;

import java.util.Objects;

public class User 
{
    private Person person;
    private Account account;

    public User(Person person, Account account)
    {
        this.person = person;
        this.account = account;
    }

    public static User generic()
    {
        Person person = Person.generic();
        Account account = Account.generic();
        return new User(person, account);
    }

    public Account account()
    {
        return account;
    }
    
    public Person person()
    {
        return person;
    }

    public Boolean isOwner(Account account)
    {
        return this.account.equals(account);
    }
    
    public Boolean isOwner(String emailOrLogin, String password)
    {
        return this.account.equals(emailOrLogin, password);
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
