package main.domain.contexts.user.registering;

import java.util.Objects;

import main.domain.models.users.Person;

public class IdentityForms {
    private String name;
    private String cpf;

    public IdentityForms name(String name) {
        this.name = name;
        return this;
    }

    public IdentityForms cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public Person submit() {
        Objects.requireNonNull(name);
        Objects.requireNonNull(cpf);
        return new Person(name, cpf);
    }
}
