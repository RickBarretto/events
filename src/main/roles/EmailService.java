package main.roles;

import main.domain.models.email.EmailDocument;

/**
 * 
 */

public interface EmailService {

    void send(EmailDocument email);

}
