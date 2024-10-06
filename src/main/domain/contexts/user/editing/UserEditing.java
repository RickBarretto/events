package main.domain.contexts.user.editing;

import java.util.Objects;

import main.domain.exceptions.EmailAlreadyExists;
import main.domain.models.users.Login;
import main.domain.models.users.Person;
import main.domain.models.users.User;
import main.roles.Context;
import main.roles.UserRepository;

public class UserEditing implements Context {
    private UserRepository repository;
    private User target;

    public UserEditing targets(User user) {
        this.target = user;
        return this;
    }

    public UserEditing from(UserRepository repository) {
        this.repository = repository;
        return this;
    }

    public EditingWithTarget with() {
        Objects.requireNonNull(repository);
        Objects.requireNonNull(target);

        return new EditingWithTarget(repository, target);
    }

    public class EditingWithTarget {
        private UserRepository repository;
        private User target;
        private User updated;

        public EditingWithTarget(UserRepository repository, User target) {
            this.repository = repository;
            this.target = target;
            this.updated = target.copy();
        }

        public EditingWithTarget person(Person person) {
            this.updated = updated.with(person);
            return this;
        }

        public EditingWithTarget login(Login login) throws EmailAlreadyExists {
            shouldBeAvailable(login.email());
            this.updated = updated.with(login);
            return this;
        }

        private void shouldBeAvailable(String email) throws EmailAlreadyExists {
            final var emailHasChanged = email.equals(target.login().email());
            if (emailHasChanged)
                return;
            if (repository.has(email))
                throw new EmailAlreadyExists();
        }

        public void update() {
            Objects.requireNonNull(this.repository);
            Objects.requireNonNull(this.target);
            Objects.requireNonNull(this.updated);

            this.repository.update(target, updated);
        }
    }
}