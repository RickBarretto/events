package test.model.users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.models.users.Person;
import main.models.users.User;
import main.models.users.UserAccount;

public class UserTest {
    Person person;
    UserAccount account;
    User user;

    @BeforeEach
    void initializeGenericUser()
    {
        person = Person.generic();
        account = UserAccount.generic();
        user = new User(person, account);
    }

    @Test
    @DisplayName("Verify getters")
    void userCreation()
    {
        Assertions.assertEquals(person, user.person());
        Assertions.assertEquals(account, user.account());
    }

    @Test
    @DisplayName("Verify equals and Generic")
    void genericUserCreation()
    {
        Assertions.assertEquals(user, User.generic());
    }
    
}
