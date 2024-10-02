package test.contexts.customer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import main.core.contexts.users.subcontexts.AccountAvailability;
import main.core.models.users.Customer;
import main.core.models.users.types.Account;
import main.core.models.users.types.CustomerID;
import main.core.models.users.types.Email;
import main.core.models.users.types.Username;
import main.core.roles.UserRepository;
import main.infra.virtualdb.VirtualUserRepository;

public class AccountAvailabilityFeature 
{
    private Account account;
    private UserRepository<Customer, CustomerID> repository;

    @BeforeEach
    public void initAccount()
    {
        this.account = new Account(
            new Username("john.doe"),
            new Email("john@example.com"),
            "12345678"
        );
        
        this.repository = new VirtualUserRepository<>();
        this.repository.register(new Customer(new CustomerID(), account));
    }

    /** Scenario: Checking an Account availability 
     *    Given some Account
     *      And a Repository without this Account
     *    When checking availability
     *      Then Account should be available
     */
    @Test
    @DisplayName("Given some Account And a Repository without this Account")
    public void shouldBeAvailable()
    {
        // When checking availability
        var context = new AccountAvailability(
            new VirtualUserRepository<Customer, CustomerID>(),
            account
        );

        assertTrue(context.isAvailable());
        assertEquals(Optional.empty(), context.reason());
    }

    /** Scenario: Checking an Account availability 
     *    Given some Account
     *      And a Repository with the same Email
     *    When checking availability
     *      Then Account should be available
     */
    @Test
    public void shouldBeUnavailable()
    {
        // When registering a new Account
        var context = new AccountAvailability(
            repository,
            account
        );

        assertFalse(context.isAvailable());
        assertEquals(
            "Email and Username are already registered", 
            context.reason().get()
        );
    }

    /** Scenario: Checking an Account availability 
     *    Given some Account
     *      And a Repository with the same Email
     *    When checking availability
     *      Then Account should be available
     */
    @Test
    public void shouldBeUnavailableForSameEmail()
    {
        // When registering a new Account
        var context = new AccountAvailability(
            repository,
            new Account(
                new Username(""), 
                new Email("john@example.com"), 
                ""
            )
        );

        assertFalse(context.isAvailable());
        assertEquals(
            "Email is already registered", 
            context.reason().get()
        );
    }

    /** Scenario: Checking an Account availability 
     *    Given some Account
     *      And a Repository with the same Email
     *    When checking availability
     *      Then Account should be available
     */
    @Test
    public void shouldBeUnavailableForSameUsername()
    {
        var context = new AccountAvailability(
            repository,
            new Account(
                new Username("john.doe"), 
                new Email(""), 
                ""
            )
        );

        assertFalse(context.isAvailable());
        assertEquals(
            "Username is already registered", 
            context.reason().get()
        );
    }

}
