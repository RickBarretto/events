package main.domain.contexts.user.forms;

import java.util.Objects;

import main.domain.models.users.Person;

public class PersonalInformation {
    private String name;
    private String cpf;

    public PersonalInformation name(String name) {
        this.name = name;
        return this;
    }

    public PersonalInformation cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public Person submit() {
        Objects.requireNonNull(name);
        Objects.requireNonNull(cpf);
        return new Person(name, cpf);
    }
}
