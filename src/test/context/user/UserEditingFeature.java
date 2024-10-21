package test.context.user;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.domain.contexts.user.UserEditing;
import main.domain.exceptions.EmailAlreadyExists;
import main.domain.exceptions.InexistentUser;
import main.domain.models.users.Login;
import main.domain.models.users.Person;
import main.domain.models.users.User;
import main.roles.repositories.Users;
import test.resources.bdd.*;
import test.resources.entities.ConcreteUsers;

@Feature("Editing some existing User")
public class UserEditingFeature {

    private Users repository;
    private Login expectedLogin;
    private Person expectedPerson;

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

    @Scenario("Successfully editing an existing User")
    @Given("An existing user in the repository")
    @When("Editing the Login and Person details of the User")
    @Then("Should update the Login if the user exists")
    @Test
    void shouldUpdateLogin() {
        var oldEmail = "john.doe@example.com";
        var newEmail = "jane.doe@example.com";
        // Given
        assumeTrue(repository.has(oldEmail), "Old email is registered");

        // When
        assertDoesNotThrow(() -> {
            new UserEditing(repository).of(ConcreteUsers.JohnDoe()).changing()
                    .email("jane.doe@example.com").password("789123")
                    .name("Jane Doe").cpf("111.111.111-11").edit();
        });

        // Then
        assertAll("Email was updated",
                () -> assertFalse(repository.has(oldEmail)),
                () -> assertTrue(repository.has(newEmail)));

        var updatedUser = repository.ownerOf(newEmail, "789123").get();
        assertEquals(expectedLogin, updatedUser.login());
        assertEquals(expectedPerson, updatedUser.person());
    }

    @Scenario("Successfully editing an existing User")
    @Given("An existing user in the repository")
    @When("Editing the Login and Person details of the User")
    @Then("Should not change the ID")
    @Test
    void shouldNotChangeTheID() {
        // When
        assertDoesNotThrow(() -> {
            new UserEditing(repository).of(ConcreteUsers.JohnDoe()).changing()
                    .email("jane.doe@example.com").password("789123")
                    .name("Jane Doe").cpf("111.111.111-11").edit();
        });

        // Then
        var updatedUser = repository.ownerOf("jane.doe@example.com", "789123")
                .get();
        assertEquals(ConcreteUsers.JohnDoe().id(), updatedUser.id());
    }

    @Scenario("Trying to edit an existing User to an existing email")
    @Given("An existing user in the repository")
    @When("Editing the Login and Person details of the User to an existing email")
    @Then("Should throw EmailAlreadyExists and not update")
    @Test
    void shouldNotUpdateToExistingEmail() {
        repository
                .register(new User(new Login("jane.doe@example.com", "789123"),
                        new Person("Jane Doe", "111.111.111-11")));
        // Given
        assumeTrue(
                repository.has("john.doe@example.com"), "User is registered");
        
        // When
        assertThrows(EmailAlreadyExists.class, () -> {
            new UserEditing(repository).of(ConcreteUsers.JohnDoe()).changing()
                    .email("jane.doe@example.com").edit();
        });
        
        // Then
        assertAll("User was not updated",
                () -> assertTrue(repository.has("john.doe@example.com")),
                () -> assertTrue(repository.has("jane.doe@example.com")));
    }

    @Scenario("Trying to edit a nonexistent user")
    @Given("A nonexistent User")
    @When("Editing the Login and Person details of the nonexistent User")
    @Then("Should throw InexistentUser and not register it.")
    @Test
    void shouldThrowInexistentUser() {
        // Given
        repository = ConcreteUsers.empty();
        assumeFalse(repository.has("john.doe@example.com"));
        
        // When
        assertThrows(InexistentUser.class, () -> {
            new UserEditing(repository).of(ConcreteUsers.JohnDoe()).changing()
                    .edit();
        });
        
        // Then
        assertFalse(repository.has("john.doe@example.com"));
    }

}
