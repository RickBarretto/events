package test.context;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import main.domain.contexts.user.forms.IdentityForms;
import main.domain.contexts.user.forms.LoginForms;
import main.domain.contexts.user.registering.UserRegistering;
import main.domain.exceptions.EmailAlreadyExists;
import main.domain.models.users.Login;
import main.domain.models.users.Person;
import main.infra.UsersInMemory;
import main.roles.repositories.Users;
import test.resources.*;

// @formatter:off
@Feature("Registering a new user")
public class UserRegisteringFeature {
    Users repository;

    @BeforeEach
    void emptyRepository() { repository = new UsersInMemory(); }

    Login validLogin() {
        return new LoginForms()
            .email("john.doe@example.com")
            .password("123456")
            .submit();
    }

    Person validPerson() {
        return new IdentityForms()
            .name("John Doe")
            .cpf("000.000.000-00")
            .submit();
    }

    @Nested
    @Scennario("Sucessfully registering some User")
    @Given("Some Login and Some Person")
    class Successful {

        @When("Registering Login and Person into a Repository")
        void register() throws EmailAlreadyExists {
            var login = validLogin();
            var person = validPerson();

            new UserRegistering()
                .into(repository)
                .login(login)
                .person(person)
                .register();
        }

        @Test
        @Then("Should register if Email is available")
        void shouldRegister() {
            // Precondition
            assumeFalse("Email must not be registered", repository.has("john.doe@example.com"));
            
            // Do
            assertDoesNotThrow(() -> this.register());

            // Assertions
            var owner = repository.ownerOf("john.doe@example.com", "123456");
            assertTrue("Email is now registered", repository.has("john.doe@example.com"));
            assertTrue("Owner is present", owner.isPresent());
        }

        @Test
        @And("Registered User is the same")
        void shouldBeTheSame() {
            // Do
            assertDoesNotThrow(() -> this.register());

            // Assertions
            var owner = repository.ownerOf("john.doe@example.com", "123456").get();

            assertEquals("john.doe@example.com", owner.login().email());
            assertEquals("John Doe", owner.person().name());
            assertEquals("000.000.000-00", owner.person().cpf());
            assertFalse(owner.isAdmin());
        }
    }

    @Nested
    @Scennario("Can't registering some User")
    @Given("Some Login and Some Person")
    class Unsucessful {

        @When("Registering Login and Person twice into a Repository")
        void registerTwice()
        throws EmailAlreadyExists 
        {
            var login = validLogin();
            var person = validPerson();

            var context = new UserRegistering()
                .into(repository)
                .login(login)
                .person(person);

            context.register();
            context.register();
        }

        @Test
        @Then("Should throw UserAlreadyRegistered if email is not available")
        void shouldNotRegister() {
            // Do
            assertThrows(EmailAlreadyExists.class, () -> registerTwice());
            
            // Assertions
            assertTrue(repository.has("john.doe@example.com"));
        }
    }
}
