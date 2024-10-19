package main.domain.contexts.user.forms;

import java.util.Objects;

import main.domain.models.users.Login;

public class LoginInformation {
    private String email;
    private String password;

    public LoginInformation email(String email) {
        this.email = email;
        return this;
    }

    public LoginInformation password(String password) {
        this.password = password;
        return this;
    }

    public Login submit() {
        Objects.requireNonNull(this.email);
        Objects.requireNonNull(this.password);
        return new Login(email, password);
    }
}
