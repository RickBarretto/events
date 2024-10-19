package test.context.user;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.domain.contexts.user.AdminRegistering;
import main.domain.models.users.User;
import main.roles.repositories.Users;
import test.resources.entities.ConcreteUsers;
import test.resources.bdd.*;

@Feature("Admin Registration")
public class AdminRegisteringFeature {

    private Users admins;
    private User admin;

    @BeforeEach
    void setUp() {
        admins = ConcreteUsers.empty();
        admin = ConcreteUsers.JohnDoe().asAdmin();
    }

    @Scennario("Registering a new admin user")
    @Given("An empty user repository and a user with admin rights")
    @When("The user is registered as an admin")
    @Then("The user should be registered and recognized as an admin")
    @Test
    void testAdminRegistration() {
        assertDoesNotThrow(() -> new AdminRegistering(admins)
                .login(admin.login()).person(admin.person()).register());

        assertTrue(admins.list().get(0).isOwnerOf(admin.login()));
        assertTrue(admins.list().get(0).isAdmin());
    }
}
