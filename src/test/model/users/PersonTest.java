package test.model.users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.core.models.users.Person;

public class PersonTest
{

    @Test
    @DisplayName("Given a Person named John Doe with CPF 000.000.000-00")
    void construction()
    {
        Person person = new Person("John Doe", "000.000.000-00");
        Assertions.assertEquals("John Doe", person.name());
        Assertions.assertEquals("000.000.000-00", person.cpf());
    }

}
