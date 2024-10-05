package main.utils;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class Guard {
    private Optional<String> message = Optional.empty();

    public <T> Requirement<T> requires(T value) {
        return new Requirement<T>(this, value);
    }

    public static Guard withMessage(String msg) {
        var result = new Guard();
        result.message = Optional.of(msg);
        return result;
    }

    public Optional<String> message() { return message; }

    public boolean isConsumed() { return message.isPresent(); }

    public <T extends Exception> void raises(Function<String, T> converter)
            throws T {
        throw converter.apply(message.get());
    }

    public class Requirement<T> {
        private Guard guard;
        private T value;

        public Requirement(Guard guard, T value) {
            this.guard = guard;
            this.value = value;
        }

        public Guard to(String message, Predicate<T> predicate) {
            if (guard.isConsumed() || predicate.test(value))
                return guard;
            return Guard.withMessage(message);
        }
    }
}
