package test.types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.types.Person;
import main.types.UserAccount;

public class UserAccountTest 
{    
    UserAccount johnDoe;

    @BeforeEach
    void initializeGenericUser()
    {
        johnDoe = new UserAccount("john.doe", "john.doe@example.com", "J04nD03123");
    }


    @Test
    @DisplayName("Given a valid login credential for Customer")
    void customerAccount()
    {
        Assertions.assertEquals("john.doe", johnDoe.username());
        Assertions.assertEquals("john.doe@example.com", johnDoe.email());
        Assertions.assertFalse(johnDoe.isAdmin());
    }
    
    @Test
    @DisplayName("Using generic factory method")
    void usingGenericFactory()
    {
        UserAccount generic = UserAccount.generic();
        Assertions.assertEquals(johnDoe.username(), generic.username());
        Assertions.assertEquals(johnDoe.email(), generic.email());
        Assertions.assertFalse(johnDoe.isAdmin());
    }

    @Test
    @DisplayName("Given a valid login credential for Administrator")
    void adminAccount()
    {
        Assertions.assertTrue(johnDoe.asAdmin().isAdmin());
    }

    @Test
    void isOwnerTest()
    {
        Assertions.assertTrue(johnDoe.isOwner("john.doe", "J04nD03123"), "Correct login and password");
        Assertions.assertTrue(johnDoe.isOwner("john.doe@example.com", "J04nD03123"), "Correct email and password");

        Assertions.assertFalse(johnDoe.isOwner("jane.doe", "J04nD03123"), "Incorrect login");
        Assertions.assertFalse(johnDoe.isOwner("jane.doe@example.com", "J04nD03123"), "Incorrect email");
        Assertions.assertFalse(johnDoe.isOwner("john.doe", "J4n3D03123"), "Correct login and Incorrect password");
        Assertions.assertFalse(johnDoe.isOwner("john.doe@example.com", "J4n3D03123"), "Correct email and Incorrect password");
    }

}

