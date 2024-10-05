package test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import main.domain.exceptions.InvalidValue;
import main.utils.Guard;

public class GuardTest {

    @Test
    void testFailOnFirstRequirement() {
        // @formatter:off
        var guard = new Guard()
            .requires("john.doe_example.com")
                .to("Have @ in the String", (email) -> email.contains("@"))
            .requires("123456")
                .to("Contains at least 6 chars", (pwd) -> pwd.length() > 5);
        // @formatter:on

        assertThrows(InvalidValue.class,
                () -> guard.raises((msg) -> new InvalidValue(msg)));
        assertEquals("Have @ in the String", guard.message().get());
    }

    @Test
    void testFailOnSecondRequirement() {
        // @formatter:off
        var guard = new Guard()
            .requires("john.doe@example.com")
                .to("Have @ in the String", (email) -> email.contains("@"))
            .requires("12345")
                .to("Contains at least 6 chars", (pwd) -> pwd.length() > 5);
            // @formatter:on

        assertThrows(InvalidValue.class,
                () -> guard.raises((msg) -> new InvalidValue(msg)));
        assertEquals("Contains at least 6 chars", guard.message().get());
    }
}
