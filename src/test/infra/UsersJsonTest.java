package test.infra;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import main.infra.json.JsonFile;
import main.infra.json.UsersJson;
import test.resources.entities.ConcreteUsers;

// @formatter:off
public class UsersJsonTest {
    private static final String directory = "src/test/infra/resources";

    @AfterAll
    static void removeFiles() {
        new JsonFile(directory, "all-users").delete();
        new JsonFile(directory, "none-users").delete();
    }

    @Nested
    class DealwithSomeExistentRepository {
        private final JsonFile file = new JsonFile(directory, "all-users");
        private UsersJson allUsersJson;

        @BeforeEach
        void createJsonFile() {
            new UsersJson(file, ConcreteUsers.withJohn());
        }
        
        @BeforeEach
        void loadJsonFile() {
            assumeTrue(file.exists());
            this.allUsersJson = new UsersJson(file);
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
            assertTrue(this.allUsersJson.ownerOf("jane.doe@example.com", "789123").isPresent());
            assertTrue(this.allUsersJson.has("jane.doe@example.com"));
        }
        
        @Test
        void shouldLoad() {
            // Forces read from json file after updating it      
            registerJane();

            var otherReference = new UsersJson(file);
            assertTrue(otherReference.ownerOf("jane.doe@example.com", "789123").isPresent());
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
            this.allUsersJson.register(ConcreteUsers.JaneDoe());
        }
    }

    @Nested
    class DealwithEmptyRepository {
        private final JsonFile file = new JsonFile(directory, "none-users");
        private UsersJson noneUsersJson;

        @BeforeEach
        void createJsonFile() {
            new UsersJson(file, ConcreteUsers.empty());
        }
        
        @BeforeEach
        void loadJsonFile() {
            assumeTrue(file.exists());
            this.noneUsersJson = new UsersJson(file);
        }

        @Test
        void shouldNotContain() {
            assertTrue(0 == this.noneUsersJson.list().size());
        }
        
        @Test
        void shouldLoadEmpty() {
            var otherReference = new UsersJson(file);
            assertTrue(otherReference.list().isEmpty());
        }
    }
}
// @formatter:on
