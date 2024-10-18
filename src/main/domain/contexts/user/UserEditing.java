package main.domain.contexts.user;

import java.util.Objects;

import main.domain.exceptions.EmailAlreadyExists;
import main.domain.exceptions.InexistentUser;
import main.domain.models.users.User;
import main.roles.Context;
import main.roles.repositories.Users;

public class UserEditing implements Context {
    private Users repository;
    private User target;

    public UserEditing targets(User user) {
        this.target = user;
        return this;
    }

    public UserEditing from(Users repository) {
        this.repository = repository;
        return this;
    }

    public EditingWithTarget with() throws InexistentUser {
        Objects.requireNonNull(repository);
        Objects.requireNonNull(target);

        if (!repository.has(target.login().email()))
            throw new InexistentUser();
        return new EditingWithTarget(repository, target);
    }

    public class EditingWithTarget {
        private Users repository;
        private User target;
        private User updated;

        public EditingWithTarget(Users repository, User target) {
            this.repository = repository;
            this.target = target;
            this.updated = target.copy();
        }

        public EditingWithTarget name(String name) {
            this.updated = updated.with(updated.person().withName(name));
            return this;
        }

        public EditingWithTarget cpf(String cpf) {
            this.updated = updated.with(updated.person().withCpf(cpf));
            return this;
        }

        public EditingWithTarget email(String email) throws EmailAlreadyExists {
            shouldBeAvailable(email);
            this.updated = updated.with(updated.login().withEmail(email));
            return this;
        }

        public EditingWithTarget password(String password) {
            this.updated = updated.with(updated.login().withPassword(password));
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