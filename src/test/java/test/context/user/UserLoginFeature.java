package test.context.user;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Test;

import main.domain.contexts.user.UserLogin;
import main.domain.exceptions.PermissionDenied;
import main.domain.models.users.Login;
import main.infra.Session;
import main.roles.repositories.Users;
import test.resources.bdd.Assume;
import test.resources.bdd.Feature;
import test.resources.bdd.Given;
import test.resources.bdd.Scenario;
import test.resources.bdd.Then;
import test.resources.bdd.When;
import test.resources.entities.ConcreteUsers;

@Feature("User Login")
public class UserLoginFeature {

    private final Users users = ConcreteUsers.withJohnAndJane();

    @Scenario("Logging in as an existing user")
    @Given("An anonymous user")
    @Assume("Session is inactive")
    @When("Logging in as an existing user")
    @Then("Should log in as this user")
    @Test
    void shouldLogInAsUser() {
        var session = new Session();
        assumeFalse(session.isActive());

        assertDoesNotThrow(() -> new UserLogin(users)
                .withSession(session)
                .logAs(Login.of("john.doe@example.com", "123456")));

        assertEquals(ConcreteUsers.JohnDoe(), session.loggedUser().get());
        assertTrue(session.isActive());
    }

    @Scenario("Changing login to another existing user")
    @Given("A logged-in session")
    @Assume("Session is active")
    @When("Logging in as another existing user")
    @Then("Should change the current user to the new one")
    @Test
    void shouldLogInAsAnotherUser() {
        var session = Session.loggedAs(ConcreteUsers.JohnDoe());
        assumeTrue(session.isActive());
        assumeTrue(session.loggedUser().get().equals(ConcreteUsers.JohnDoe()));

        assertDoesNotThrow(() -> new UserLogin(users)
                .withSession(session)
                .logAs(Login.of("jane.doe@example.com", "789123")));

        assertEquals(ConcreteUsers.JaneDoe(), session.loggedUser().get());
        assertTrue(session.isActive());
    }

    @Scenario("Trying to log in as a nonexistent user")
    @Given("A logged-in session")
    @Assume("Session is active")
    @When("Trying to log in as a nonexistent user")
    @Then("Should throw, and keep the same session state")
    @Test
    void shouldThrow() {
        var session = Session.loggedAs(ConcreteUsers.JohnDoe());
        assumeTrue(session.isActive());
        assumeTrue(session.loggedUser().get().equals(ConcreteUsers.JohnDoe()));

        assertThrowsExactly(PermissionDenied.class,
                () -> new UserLogin(users)
                        .withSession(session)
                        .logAs(Login.of("johane.doe@example.com", "789123")));

        assertThrowsExactly(PermissionDenied.class, () -> new UserLogin(users)
                .withSession(session)
                .logAs(Login.of("jane.doe@example.com", "123456")));

        assertEquals(ConcreteUsers.JohnDoe(), session.loggedUser().get());
        assertTrue(session.isActive());
    }

    @Scenario("Logging out as an anonymous user")
    @Given("An anonymous user")
    @Assume("Session is inactive")
    @When("Trying to logout")
    @Then("Should remain anonymous")
    @Test
    void shouldRemainAnonymousWhenLoggingOut() {
        var session = new Session();
        assumeFalse(session.isActive());

        new UserLogin(users)
                .withSession(session)
                .logOut();

        assertTrue(session.loggedUser().isEmpty());
        assertFalse(session.isActive());
    }

    @Scenario("Logging out as a logged-in user")
    @Given("A logged-in session")
    @Assume("Session is active")
    @When("Logging out")
    @Then("Should disable the session")
    @Test
    void shouldDisableSessionWhenLoggingOut() {
        var session = Session.loggedAs(ConcreteUsers.JohnDoe());
        assumeTrue(session.isActive());
        assumeTrue(session.loggedUser().get().equals(ConcreteUsers.JohnDoe()));

        new UserLogin(users)
                .withSession(session)
                .logOut();

        assertTrue(session.loggedUser().isEmpty());
        assertFalse(session.isActive());
    }
}
