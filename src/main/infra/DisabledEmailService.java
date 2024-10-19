package main.infra;

import main.domain.models.email.EmailDocument;
import main.roles.EmailService;

public class DisabledEmailService implements EmailService {
    private EmailDocument email;

    @Override
    public void send(EmailDocument email) { this.email = email; }

    public EmailDocument email() { return email; }

}
