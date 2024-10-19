package test.context.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import main.domain.contexts.user.UserLogin;
import main.domain.exceptions.PermissionDenied;
import main.infra.Session;
import main.roles.repositories.Users;
import test.resources.bdd.Assume;
import test.resources.bdd.Then;
import test.resources.bdd.When;
import test.resources.entities.ConcreteUsers;

public class UserLoginFeature {
    private final Users users = ConcreteUsers.withJohnAndJane();

    @Assume("Session is inactive")
    void shouldBeInactive(Session session) { assumeFalse(session.isActive()); }

    @Assume("Session is active")
    void shouldBeActive(Session session) { assumeTrue(session.isActive()); }

    @Nested
    class LogIn {
        Session session;

        @Test
        @Assume("An anonymous user")
        @When("Login as some existent user")
        @Then("Should log in as this user")
        void shouldLogInAsUser() {
            // Given
            var session = new Session();
            shouldBeInactive(session);

            // When
            assertDoesNotThrow(() -> new UserLogin(users).withSession(session)
                    .logAs("john.doe@example.com", "123456"));

            // Then
            assertEquals(ConcreteUsers.JohnDoe(), session.loggedUser().get());
            assertTrue(session.isActive());
        }

        @Test
        @Assume("A logged-in session")
        @When("Log-in as other existent user")
        @Then("Should change the current user to the new one")
        void shouldLogInAsAnotherUser() {
            // Given
            var session = Session.loggedAs(ConcreteUsers.JohnDoe());
            shouldBeActive(session);
            assumeTrue(
                    session.loggedUser().get().equals(ConcreteUsers.JohnDoe()));

            // When
            assertDoesNotThrow(() -> new UserLogin(users).withSession(session)
                    .logAs("jane.doe@example.com", "789123"));

            // Then
            assertEquals(ConcreteUsers.JaneDoe(), session.loggedUser().get());
            assertTrue(session.isActive());
        }

        @Test
        @When("Try to log-in as an inexistent User")
        @Then("Should throw, and keep the same Session state")
        void shouldThrow() {
            // Given
            var session = Session.loggedAs(ConcreteUsers.JohnDoe());
            shouldBeActive(session);
            assumeTrue(
                    session.loggedUser().get().equals(ConcreteUsers.JohnDoe()));

            // When
            assertThrowsExactly(PermissionDenied.class,
                    () -> new UserLogin(users).withSession(session)
                            .logAs("johane.doe@example.com", "789123"));
            assertThrowsExactly(PermissionDenied.class,
                    () -> new UserLogin(users).withSession(session)
                            .logAs("jane.doe@example.com", "123456"));

            // Then
            assertEquals(ConcreteUsers.JohnDoe(), session.loggedUser().get());
            assertTrue(session.isActive());
        }
    }

    @Nested
    class LogOut {
        Session session;

        @Test
        @Assume("An anonymous user")
        @When("Try to logout")
        @Then("Should remain anonymous")
        void shouldLogInAsUser() {
            // Given
            var session = new Session();
            shouldBeInactive(session);

            // When
            new UserLogin(users).withSession(session).logOut();

            // Then
            assertTrue(session.loggedUser().isEmpty());
            assertFalse(session.isActive());
        }

        @Test
        @Assume("A logged-in session")
        @When("Loggin out")
        @Then("Should disable the session")
        void shouldLogInAsAnotherUser() {
            // Given
            var session = Session.loggedAs(ConcreteUsers.JohnDoe());
            shouldBeActive(session);
            assumeTrue(
                    session.loggedUser().get().equals(ConcreteUsers.JohnDoe()));

            // When
            new UserLogin(users).withSession(session).logOut();

            // Then
            assertTrue(session.loggedUser().isEmpty());
            assertFalse(session.isActive());
        }
    }
}
