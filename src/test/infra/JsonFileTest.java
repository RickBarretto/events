package test.infra;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import main.domain.models.users.Login;
import main.domain.models.users.Person;
import main.domain.models.users.User;
import main.infra.JsonFile;
import main.infra.UsersInMemory;
import main.infra.UsersJson;

// @formatter:off
public class JsonFileTest {

    @Nested
    class DealwithSomeExistentRepository {
        private final JsonFile file = new JsonFile("src/test/infra/resources", "all-users");
        private UsersJson allUsersJson;

        @BeforeEach
        void createJsonFile() {
            new UsersJson(file.toString(), new UsersInMemory(
                List.of(
                    new User(
                        new Login("john.doe@example.com", "123456"), 
                        new Person("John Doe", "000.000.000-00")))
            ));
        }
        
        @BeforeEach
        void loadJsonFile() {
            assumeTrue(file.exists());
            this.allUsersJson = new UsersJson(file.toString());
        }

        @Test
        void shouldContains() {
            assertTrue(1 == this.allUsersJson.list().size());
            assertTrue(this.allUsersJson.has("john.doe@example.com"));
            assertTrue(this.allUsersJson.ownerOf("john.doe@example.com", "123456").isPresent());
        }
        
        @Test
        void shouldRegister() {            
            registerJane();
            assertTrue(this.allUsersJson.ownerOf("jane.doe@example.com", "789456").isPresent());
            assertTrue(this.allUsersJson.has("jane.doe@example.com"));
        }
        
        @Test
        void shouldLoad() {
            // Forces read from json file after updating it      
            registerJane();

            var otherReference = new UsersJson(file.toString());
            assertTrue(otherReference.ownerOf("jane.doe@example.com", "789456").isPresent());
            assertTrue(otherReference.has("jane.doe@example.com"));
        }

        @Test
        void shouldUpdate() {
            // Prepare
            var user = this.allUsersJson.list().get(0);
            
            // Do
            this.allUsersJson.update(user, user.with(user.login().withEmail("john.new@example.com")));
            
            // Assert
            assertFalse(this.allUsersJson.has("john.doe@example.com"));
            assertTrue(this.allUsersJson.has("john.new@example.com"));
        }

        void registerJane() {
            this.allUsersJson.register(new User(
                new Login("jane.doe@example.com", "789123"), 
                new Person("Jane Doe", "111.111.111-11"))
            );
        }
    }

    @Nested
    class DealwithEmptyRepository {
        private final JsonFile file = new JsonFile("src/test/infra/resources", "none-users");
        private UsersJson noneUsersJson;

        @BeforeEach
        void createJsonFile() {
            new UsersJson(file.toString(), new UsersInMemory(List.of()));
        }
        
        @BeforeEach
        void loadJsonFile() {
            assumeTrue(file.exists());
            this.noneUsersJson = new UsersJson(file.toString());
        }

        @Test
        void shouldNotContain() {
            assertTrue(0 == this.noneUsersJson.list().size());
        }
        
        @Test
        void shouldLoadEmpty() {
            var otherReference = new UsersJson(file.toString());
            assertTrue(otherReference.list().isEmpty());
        }
    }
}
// @formatter:on
