package test.contexts.customer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import main.core.contexts.users.UserSession;
import main.core.models.LocalSession;
import main.core.models.users.Customer;
import main.core.models.users.types.Account;
import main.core.models.users.types.CustomerID;
import main.core.models.users.types.Email;
import main.core.roles.UserRepository;
import main.infra.virtualdb.VirtualUserRepository;

public class CustomerSessionFeature 
{
    private UserRepository<Customer, CustomerID> repository;

    @BeforeEach
    void initRepo()
    {
        var account = new Account(
            new Email("john@example.com"),
            "12345678"
        );
        this.repository = new VirtualUserRepository<>();
        this.repository.register(new Customer(new CustomerID(), account));
    }

    /** Feature: LogIn as an User
     *    In order to logIn as an User,
     *    I want to insert my login and password
     *    to be logged into my Account.
     */
    @Nested
    public class LogInFeature
    {
        @Test
        public void shouldLogin()
        {
            var session = new LocalSession<Customer, CustomerID>();
            var context = new UserSession(session, repository);

            assertAll("No user is logged in",
                () -> assertEquals(Optional.empty(), context.current()),
                () -> assertFalse(context.isLoggedIn())
            );

            context.loginAs(new Email("john@example.com"), "12345678");
            assertTrue(context.current().isPresent());
            assertEquals(
                "john@example.com", context.current().get().account().email().toString()
            );
        }
    }
}
