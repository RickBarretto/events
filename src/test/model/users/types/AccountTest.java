package test.model.users.types;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.core.models.users.types.Account;
import main.core.models.users.types.Email;

public class AccountTest
{
    Account joesAccount;

    @BeforeEach
    void createJohnDoe()
    {
        joesAccount = new Account(
            new Email("joe.doe@pm.me"),
            "joe-doe45617"
        );
    }

    @Test
    void testOwnability()
    {
        // Given same login credentials and password
        // Then Joe is the owner
        assertTrue(joesAccount.hasLogin(new Email("joe.doe@pm.me"), "joe-doe45617"));
        
        // Given a different login credentials
        // Then Joe is not the owner
        assertFalse(joesAccount.hasLogin(new Email("joe.doe@gmail.com"), "joe-doe45617"));
        
        // Given same login credentials but wrong password
        // Then Joe is not the owner
        assertFalse(joesAccount.hasLogin(new Email("joe.doe@pm.me"), "joe-doe"));
    }

}
