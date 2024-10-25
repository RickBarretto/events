package main.domain.models.users.values;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

/**
 * Utility class for hashing values using SHA-256.
 */
class Sha256 {
    private final String value;

    /**
     * Constructs a Sha256 object and hashes the provided value.
     *
     * @param value the value to be hashed
     */
    public Sha256(String value) { this.value = hash(value); }

    /**
     * Returns the hashed value.
     *
     * @return the hashed value
     */
    public String value() { return value; }

    /**
     * Hashes the given value using SHA-256.
     *
     * @param value the value to hash
     * @return the hashed value as a base64 string
     * @throws RuntimeException if SHA-256 algorithm is not available
     */
    public String hash(String value) {
        try {
            final var valueBytes = value.getBytes();
            final var digest = MessageDigest.getInstance("SHA-256");

            return Base64.getEncoder()
                    .encodeToString(digest.digest(valueBytes));
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    @Override
    public int hashCode() { return Objects.hash(value); }

    public boolean equals(String value) { return this.value.equals(value); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Sha256))
            return false;
        Sha256 other = (Sha256) obj;
        return Objects.equals(value, other.value);
    }
}

/**
 * Represents a password, storing its hash using SHA-256.
 */
public class Password {
    private final Sha256 value;

    /**
     * Constructs a Password object with the specified plain text password.
     *
     * @param value the plain text password
     */
    public Password(String value) { this.value = new Sha256(value); }

    @Override
    public int hashCode() { return Objects.hash(value); }

    public boolean equals(String value) { return this.value.equals(value); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Password))
            return false;
        return equals((Password) obj);
    }

    private boolean equals(Password other) {
        return Objects.equals(value, other.value);
    }
}
