package main.types;


public record Person(String name, String cpf) 
{
    public static Person generic()
    {
        return new Person("John Doe", "000.000.000-00");
    }
}
