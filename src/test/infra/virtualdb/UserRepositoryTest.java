package test.infra.virtualdb;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import main.core.models.users.Customer;
import main.core.models.users.types.Account;
import main.core.models.users.types.CustomerID;
import main.core.models.users.types.Email;
import main.infra.virtualdb.VirtualUserRepository;

public class UserRepositoryTest
{
    Customer johnDoe;
    Customer janeDoe;
    List<Customer> customers;


    @BeforeEach
    void initCustomers()
    {
        this.johnDoe = new Customer(new CustomerID(), new Account(
            new Email("john@example.com"), 
            "12345678"
        ));

        this.janeDoe = new Customer(new CustomerID(), new Account(
            new Email("jane@example.co"), 
            "12345678"
        ));

        this.customers = List.of(johnDoe, janeDoe);
    }

    @Nested
    class EmptyRepoTest
    {
        VirtualUserRepository<Customer, CustomerID> emptyRepo;

        @BeforeEach
        void initEmptyRepository()
        {
            emptyRepo = new VirtualUserRepository<>();
        }

        @Test
        void testRegistering()
        {
            customers.forEach((customer) -> {
                assertDoesNotThrow(() -> emptyRepo.register(customer));
            });

            customers.forEach((customer) -> {
                assertThrows(
                    AssertionError.class, 
                    () -> emptyRepo.register(customer));
            });
        }

        @Test
        void testExistence()
        {
            assertFalse(emptyRepo.exists(johnDoe.id()));
            assertFalse(emptyRepo.exists(johnDoe.account().email()));
            
            assertFalse(emptyRepo.exists(janeDoe.id()));
            assertFalse(emptyRepo.exists(janeDoe.account().email()));
        }

        @Test
        void testSearchesBy()
        {
            assertTrue(emptyRepo.by(johnDoe.id()).isEmpty());
            assertTrue(emptyRepo.by(johnDoe.account().email()).isEmpty());
            
            assertTrue(emptyRepo.by(janeDoe.id()).isEmpty());
            assertTrue(emptyRepo.by(janeDoe.account().email()).isEmpty());
        }
    }

    @Nested
    class FilledRepoTest
    {
        VirtualUserRepository<Customer, CustomerID> filledRepo;

        @BeforeEach
        void initEmptyRepository()
        {
            filledRepo = new VirtualUserRepository<>();
            filledRepo.register(johnDoe);
            filledRepo.register(janeDoe);
        }

        @Test
        void testExistence()
        {
            assertTrue(filledRepo.exists(johnDoe.id()));
            assertTrue(filledRepo.exists(johnDoe.account().email()));
            
            assertTrue(filledRepo.exists(janeDoe.id()));
            assertTrue(filledRepo.exists(janeDoe.account().email()));
        }

        @Test
        void testSearchesBy()
        {
            assertTrue(filledRepo.by(johnDoe.id()).isPresent());
            assertTrue(filledRepo.by(johnDoe.account().email()).isPresent());

            assertTrue(filledRepo.by(janeDoe.id()).isPresent());
            assertTrue(filledRepo.by(janeDoe.account().email()).isPresent());
        }
    }



}
