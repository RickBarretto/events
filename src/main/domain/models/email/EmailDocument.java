package main.domain.models.email;

public class EmailDocument {
    private final EmailMetadata meta;
    private final Html body;

    public EmailDocument(EmailMetadata meta, Html body) {
        this.meta = meta;
        this.body = body;
    }

    public String body() { return body.toString(); }

    public EmailMetadata metadata() { return meta; }

}
