package test.utils;

import org.junit.jupiter.api.Assertions;
import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import junit.framework.TestCase;
import main.utils.Pair;

public class PairTest
{
    
    @Test
    void testConstruction()
    {
        // Given the person Pair<Name, Age>: John Doe of 32 years old
        Pair<String, Integer> person = new Pair<String, Integer>("John Doe", 32);

        Assertions.assertAll("left, first and key should be the same method",
            () -> assertEquals("John Doe", person.left()),
            () -> assertEquals("John Doe", person.first()),
            () -> assertEquals("John Doe", person.key())
        );

        var age = Integer.valueOf(32); 
        Assertions.assertAll("right, second, last and value should be the same method",
            () -> assertEquals(age, person.right()),
            () -> assertEquals(age, person.second()),
            () -> assertEquals(age, person.last()),
            () -> assertEquals(age, person.value())
        );
    }

}
