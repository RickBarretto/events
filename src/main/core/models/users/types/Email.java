package main.core.models.users.types;

import java.util.Objects;

// import java.text.MessageFormat;
// import java.util.regex.Pattern;

// import main.core.models.users.exceptions.InvalidEmail;

public final class Email 
{
    private String value;

    public Email(String value)
    {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Email))
            return false;
        Email other = (Email) obj;
        return Objects.equals(value, other.value);
    }

    @Override
    public String toString()
    {
        return value;
    }

    // public String value() throws InvalidEmail
    // {
    //     if (!this.isValid())
    //         throw new InvalidEmail("Email pattern is not valid.");
    //     return value;
    // }

    // /** ## An email is valid if has the format:
    //  *  username@host.com
    //  * 
    //  *  Examples of valid emails:
    //  *    someuser@pm.me
    //  *    some.one@valid.email.com
    //  *    hmm-im_still@valid.hehe
    //  * 
    //  * @return if is valid
    //  */
    // public boolean isValid()
    // {
    //     var username = "[a-zA-Z0-9_-.]+";       // Example: john.doe12, jane_doe, joe-doe
    //     var hostDomain = "[a-zA-Z0-9.-]+";      // Example: gmail, protonmail
    //     var topLevelDomain = "[a-zA-Z]{2,}";    // Example: com, co, me

    //     return Pattern.compile(MessageFormat.format(
    //         "^{0}@{1}\\.{2}$", 
    //         username, hostDomain, topLevelDomain
    //     )).matcher(value).matches();
    // }
}
