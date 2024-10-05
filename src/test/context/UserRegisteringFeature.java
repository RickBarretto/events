package test.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.domain.contexts.user.registering.IdentityForms;
import main.domain.contexts.user.registering.LoginForms;
import main.domain.contexts.user.registering.UserAlreadyRegistered;
import main.domain.contexts.user.registering.UserRegistering;
import main.infra.VirtualUserRepository;
import main.roles.UserRepository;
import test.resources.*;

@Feature("Registering a new user")
public class UserRegisteringFeature {
    UserRepository repository;

    @BeforeEach
    void init() {
        repository = new VirtualUserRepository();
    }

    void register() throws UserAlreadyRegistered {
        // @formatter:off
        var login = new LoginForms()
            .email("john.doe@example.com")
            .password("123456")
            .submit();
        
        var info = new IdentityForms()
            .name("John Doe")
            .cpf("000.000.000-00")
            .submit();
        
        new UserRegistering()
            .into(repository)
            .login(login)
            .person(info)
            .register();
        // @formatter:on
    }

    @Test
    @Given("A new User that is not registered")
    @When("Registering the same")
    @Then("Should be registered into UserRepository")
    void souldRegisterNewUser() {
        assumeFalse(repository.has("john.doe@example.com"));

        // When
        assertDoesNotThrow(() -> register());

        // Then
        var owner = repository.ownerOf("john.doe@example.com", "123456");

        assertAll("User was registered",
                () -> assertTrue(repository.has("john.doe@example.com")),
                () -> assertTrue(owner.isPresent()));

        assertAll("Registered user is the same",
                () -> assertEquals("john.doe@example.com",
                        owner.get().login().email()),
                () -> assertEquals("John Doe", owner.get().person().name()),
                () -> assertEquals("000.000.000-00",
                        owner.get().person().cpf()),
                () -> assertFalse(owner.get().isAdmin()));
    }

    @Test
    @Given("An already registered Admin")
    @When("Trying to register the same again")
    @Then("Should not register, but throw an UserAlreadyRegistered Exception")
    void shouldNotRegister() {
        assumeFalse(repository.has("john.doe@example.com"));

        // When
        assertDoesNotThrow(() -> register());

        // Then
        assumeTrue(repository.has("john.doe@example.com"));
        assertThrows(UserAlreadyRegistered.class, () -> register());
    }
}
