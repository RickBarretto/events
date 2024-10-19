package test.context.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import main.domain.contexts.user.UserEditing;
import main.domain.exceptions.EmailAlreadyExists;
import main.domain.exceptions.InexistentUser;
import main.domain.exceptions.PermissionDenied;
import main.domain.models.users.Login;
import main.domain.models.users.Person;
import main.domain.models.users.User;
import main.infra.virtual.UsersInMemory;
import main.roles.repositories.Users;
import test.resources.bdd.And;
import test.resources.bdd.Feature;
import test.resources.bdd.Given;
import test.resources.bdd.Scennario;
import test.resources.bdd.Then;
import test.resources.bdd.When;
import test.resources.entities.ConcreteUsers;

// @formatter:off
@Feature("Editing some existing User")
public class UserEditingFeature {

    @Nested
    @Scennario("Sucessfully editing some existing User")
    @Given("Some existing user in some Repository")
    class Sucessfully {
        Users repository;
        Login expectedLogin;
        Person expectedPerson;

        @BeforeEach
        void registerTargetUser() {
            repository = ConcreteUsers.empty();
            repository.register(ConcreteUsers.JohnDoe());
        }

        @BeforeEach
        void submitForms() {
            expectedLogin = new Login("jane.doe@example.com", "789123");
            expectedPerson = new Person("Jane Doe", "111.111.111-11");
        }

        @When("Editing the Login and Person of an User")
        void editLogin() throws InexistentUser, EmailAlreadyExists {
            new UserEditing(repository)
                .of(ConcreteUsers.JohnDoe())
                .changing()
                    .email("jane.doe@example.com")
                    .password("789123")
                    .name("Jane Doe")
                    .cpf("111.111.111-11")
                .edit();
        }

        @Test
        @Then("Should update the Login if user exists")
        void shouldUpdateLogin() {
            var oldEmail = "john.doe@example.com";
            var newEmail = "jane.doe@example.com";

            // Pre-condition
            assumeTrue("Old email is registered", repository.has(oldEmail));

            // Execution
            assertDoesNotThrow(() -> editLogin());

            // Post-condition
            assertAll("Email was updated",
                    () -> assertFalse(repository.has(oldEmail)),
                    () -> assertTrue(repository.has(newEmail))
            );

            var updatedUser = repository.ownerOf(newEmail, "789123").get();
            assertEquals("Updated Login", expectedLogin, updatedUser.login());
            assertEquals("Updated Person", expectedPerson, updatedUser.person());
        }

        @Test
        @And("Should not change the ID")
        void shouldNotChangeTheID() {
            // Execution
            assertDoesNotThrow(() -> editLogin());

            // Post-condition
            var updatedUser = repository.ownerOf("jane.doe@example.com", "789123").get();
            assertEquals(ConcreteUsers.JohnDoe().id(), updatedUser.id());
        }
    }

    @Nested
    @Scennario("Trying editing some existing User to an existing email")
    @Given("Some existing user in some Repository")
    class NotAvailableEmail {
        Users repository;

        @BeforeEach
        void registerTargetUserAndOther() {
            repository = new UsersInMemory();
            repository.register(ConcreteUsers.JohnDoe());
            repository.register(new User(
                new Login("jane.doe@example.com", "789123"),
                new Person("Jane Doe", "111.111.111-11")
            ));
        }

        @When("Editing the Login and Person of an User")
        void editToExistingEmail() throws InexistentUser, EmailAlreadyExists {
            new UserEditing(repository)
                .of(ConcreteUsers.JohnDoe())
                .changing()
                    .email("jane.doe@example.com");
        }

        @Test
        @Then("Should thows EmailAlreadyExists and not update")
        void shouldUpdateLogin() {
            // Pre-condition
            assumeTrue("User is registered", repository.has("john.doe@example.com"));
            
            // Execution
            assertThrows(EmailAlreadyExists.class, () -> editToExistingEmail());
            
            // Post-condition
            assertAll("User was not updated", 
                () -> assertTrue(repository.has("john.doe@example.com")),
                () -> assertTrue(repository.has("jane.doe@example.com"))
            );
        }
    }

    @Nested
    @Scennario("Trying to edit an inexistent user")
    @Given("Some inexistent User")
    class UserDoesNotExist {
        Users repository;

        @BeforeEach
        void registerTargetUserAndOther() {
            repository = new UsersInMemory();
        }

        User inexistenUser() {
            return new User(
                new Login("john.doe@example.com", "123456"),
                new Person("John Doe", "000.000.000-00")
            );
        }

        @When("Editing the Login and Person of an User")
        void editInexistentUser() throws InexistentUser {
            new UserEditing(repository)
                .of(inexistenUser())
                .changing();
        }

        @Test
        @Then("Should throw InexistentUser, and not register it.")
        void shouldThrow() {
            // Pre-condition
            assumeFalse(repository.has("john.doe@example.com"));

            // Execution
            assertThrows(InexistentUser.class, () -> this.editInexistentUser());
            
            // Post-condition
            assertFalse(repository.has("john.doe@example.com"));
        }
    }

    @Nested
    @Scennario("Replace password requires old password")
    @Given("")
    class ReplacePasswordNeedsPermission {
        Users repository;

        @BeforeEach
        void registerTargetUserAndOther() {
            repository = new UsersInMemory();
        }

        @When("Editing the Login and Person of an User")
        void editToExistingEmail() throws InexistentUser, PermissionDenied, EmailAlreadyExists {
        }
    }

}
