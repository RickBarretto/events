package main.roles;

import main.domain.models.email.Email;

/**
 * 
 */

public interface EmailService {

    void send(Email email);

}
