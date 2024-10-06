package test.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import main.domain.contexts.user.editing.UserEditing;
import main.domain.exceptions.EmailAlreadyExists;
import main.domain.exceptions.InexistentUser;
import main.domain.models.users.Login;
import main.domain.models.users.Person;
import main.domain.models.users.User;
import main.infra.VirtualUserRepository;
import main.roles.UserRepository;
import test.resources.*;

// @formatter:off
@Feature("Editing some existing User")
public class UserEditingFeature {
    private User targetUser;

    @BeforeEach
    void initTargetUser() {
        targetUser = new User(
            new Login("john.doe@example.com", "123456"),
            new Person("John Doe", "000.000.000-00")
        );
    }


    @Nested
    @Scennario("Sucessfully editing some existing User")
    @Given("Some existing user in some Repository")
    class Sucessfully {
        UserRepository repository;
        Login updatedLogin;
        Person updatedPerson;

        @BeforeEach
        void registerTargetUser() {
            repository = new VirtualUserRepository();
            repository.register(targetUser);
        }

        @BeforeEach
        void submitForms() {
            updatedLogin = new Login("jane.doe@example.com", "789123");
            updatedPerson = new Person("Jane Doe", "111.111.111-11");
        }

        @When("Editing the Login and Person of an User")
        void editLogin() throws InexistentUser, EmailAlreadyExists {
            new UserEditing()
                .from(repository)
                .targets(targetUser)
                .with()
                    .login(updatedLogin)
                    .person(updatedPerson)
                .update();
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
            assertEquals("Updated Login", updatedLogin, updatedUser.login());
            assertEquals("Updated Person", updatedPerson, updatedUser.person());
        }

        @Test
        @And("Should not change the ID")
        void shouldNotChangeTheID() {
            var oldId = targetUser.id();

            // Pre-condition
            assumeTrue("User is registered with oldId", repository.has(oldId));

            // Execution
            assertDoesNotThrow(() -> editLogin());

            // Post-condition
            var updatedUser = repository.ownerOf("jane.doe@example.com", "789123").get();
            assertAll("User is still registered with oldId",
                () -> assertTrue(repository.has(oldId)),
                () -> assertEquals(targetUser.id(), updatedUser.id())
            );
        }
    }
}
