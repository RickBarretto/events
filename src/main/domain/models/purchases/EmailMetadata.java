package main.domain.models.purchases;

public class EmailMetadata {
    private final String senderEmail;
    private final String recipientEmail;
    private final String subject;

    public EmailMetadata(String senderEmail, String recipientEmail,
            String subject) {
        this.senderEmail = senderEmail;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
    }

    public String sender() { return senderEmail; }

    public String recipient() { return recipientEmail; }

    public String subject() { return subject; }

}
