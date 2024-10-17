package main.domain.models.users;

import java.util.Objects;

public class Person {
    private final String name;
    private final String cpf;

    // Constructors

    public Person(String name, String cpf) {
        this.name = name;
        this.cpf = cpf;
    }

    // Getters

    /**
     * Name's getter
     * 
     * @return Person's name
     */
    public String name() { return name; }

    /**
     * CPF's getter
     * 
     * @return Person's CPF
     */
    public String cpf() { return cpf; }

    // Factory methods

    /**
     * Creates a copy with a new name
     * 
     * @param name New Person's name
     * @return a new Person
     */
    public Person withName(String name) {
        return new Person(name, this.cpf);
    }

    /**
     * Creates a copy with a new CPF
     * 
     * @param cpf New Person's CPF
     * @return a new Person
     */
    public Person withCpf(String cpf) { return new Person(this.name, cpf); }

    // Object's methods

    public boolean equals(Person other) {
        return Objects.equals(name, other.name)
                && Objects.equals(cpf, other.cpf);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person person)
            return this.equals(person);
        return false;
    }

    @Override
    public int hashCode() { return Objects.hash(name, cpf); }

}
