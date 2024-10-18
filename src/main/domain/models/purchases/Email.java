package main.domain.models.purchases;

public class Email {
    private final EmailMetadata meta;
    private final Html body;

    public Email(EmailMetadata meta, Html body) {
        this.meta = meta;
        this.body = body;
    }

    public String body() { return body.toString(); }

    public EmailMetadata metadata() { return meta; }

}
