package main.domain.models.users.values;

import java.util.Objects;

/**
 * Represents an email address with validation.
 */
public class EmailAddress {
    private final String value;

    /**
     * Constructs an EmailAddress object with the specified value.
     *
     * @param value the email address
     */
    public EmailAddress(String value) { this.value = value; }

    /**
     * Returns the email address value.
     *
     * @return the email address
     */
    public String value() { return value; }

    @Override
    public int hashCode() { return Objects.hash(value); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof EmailAddress))
            return false;
        return equals((EmailAddress) obj);
    }


    public boolean equals(EmailAddress other) {
        return Objects.equals(value, other.value);
    }

    public boolean equals(String other) {
        return Objects.equals(value, other);
    }

    @Override
    public String toString() { return value; }
}
