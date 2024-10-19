package test.context.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

import main.domain.contexts.user.AdminRegistering;
import main.domain.models.users.User;
import main.roles.repositories.Users;
import test.resources.entities.ConcreteUsers;

public class AdminRegisteringFeature {
    Users admins = ConcreteUsers.empty();
    User admin = ConcreteUsers.JohnDoe().asAdmin();

    @Test
    void testAdmin() {
        assertDoesNotThrow(() -> new AdminRegistering(admins).login(admin.login())
                .person(admin.person()).register());
        assertTrue(admins.list().get(0).isOwnerOf(admin.login()));
        assertTrue(admins.list().get(0).isAdmin());
    }

}
