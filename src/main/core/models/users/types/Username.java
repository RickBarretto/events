package main.core.models.users.types;

import java.util.Objects;

// import java.util.regex.Pattern;

// import main.core.models.users.exceptions.InvalidUsername;

public class Username 
{
    private String value;

    public Username(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Username))
            return false;
        Username other = (Username) obj;
        return Objects.equals(value, other.value);
    }

    // public String value() throws InvalidUsername
    // {
    //     if (!this.isValid())
    //         throw new InvalidUsername(
    //             "Username must contain only letters, digits, " + 
    //             "dots and underscores"
    //             );
    //     return value.toLowerCase();
    // }

    // public boolean isValid()
    // {
    //     var pattern = "^[a-zA-Z0-9_]+$"; // Example: john.doe12, jane_doe
    //     return Pattern.compile(pattern)
    //                   .matcher(value)
    //                   .matches();
    // }
}
