package test.context.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;
import org.junit.jupiter.api.Test;

import main.domain.contexts.user.UserLogin;
import main.domain.exceptions.PermissionDenied;
import main.infra.Session;
import main.roles.repositories.Users;

import test.resources.bdd.*;
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
        assertDoesNotThrow(() -> new UserLogin(users).withSession(session)
                .logAs("john.doe@example.com", "123456"));
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
        assertDoesNotThrow(() -> new UserLogin(users).withSession(session)
                .logAs("jane.doe@example.com", "789123"));
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
                () -> new UserLogin(users).withSession(session)
                        .logAs("johane.doe@example.com", "789123"));
        assertThrowsExactly(PermissionDenied.class, () -> new UserLogin(users)
                .withSession(session).logAs("jane.doe@example.com", "123456"));
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
        new UserLogin(users).withSession(session).logOut();
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
        new UserLogin(users).withSession(session).logOut();
        assertTrue(session.loggedUser().isEmpty());
        assertFalse(session.isActive());
    }
}
