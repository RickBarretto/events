package main.infra;

import main.domain.models.email.Email;
import main.roles.EmailService;

public class DisabledEmailService implements EmailService {
    private Email email;

    @Override
    public void send(Email email) { this.email = email; }

    public Email email() { return email; }

}
