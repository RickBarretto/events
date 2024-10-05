package test.domain.models.users;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import main.domain.models.users.Login;

public class LoginTest {
    Login login;

    @BeforeEach
    void init() { login = new Login("john.doe@example.com", "123456"); }

    @Nested
    class TestGetters {

        @Test
        void testEmail() {
            assertEquals("john.doe@example.com", login.email());
            assertNotEquals("jane.doe@example.com", login.email());

        }
    }

    @Nested
    class TestObject {

        @Test
        void testEquals() {
            assertTrue(
                    login.equals(new Login("john.doe@example.com", "123456")));

            assertFalse(login.equals(new Object()));
            assertFalse(
                    login.equals(new Login("jane.doe@example.com", "123456")));
            assertFalse(
                    login.equals(new Login("john.doe@example.com", "789123")));
        }

        @Test
        void testHashCode() {
            assertEquals(new Login("john.doe@example.com", "123456").hashCode(),
                    login.hashCode());
            assertNotEquals(
                    new Login("john.do@example.com", "123456").hashCode(),
                    login.hashCode());
        }
    }

    @Nested
    class TestFactoryMethods {

        @Test
        void testWithEmail() {
            var expected = new Login("jane.doe@example.com", "123456");
            assertNotEquals(login, login.withEmail("jane.doe@example.com"));
            assertEquals(expected, login.withEmail("jane.doe@example.com"));
        }

        @Test
        void testWithPassword() {
            var expected = new Login("john.doe@example.com", "789123");
            assertNotEquals(login, login.withPassword("789123"));
            assertEquals(expected, login.withPassword("789123"));
        }
    }

}
