package main.domain.models.purchases;

public class Html {
    private final String tag;    
    private final String content;
    private final String attributes;
    private final boolean closable;

    private Html(String tag) {
        this(tag, "", "", true);
    }

    private Html(String tag, String content, String attributes,
            boolean closable) {
        this.tag = tag;
        this.content = content;
        this.attributes = attributes;
        this.closable = closable;
    }

    public static Html br() {
        return new Html("br").empty();
    }

    public static Html node(String tag) { return new Html(tag); }
    
    public Html copy() {
        return new Html(tag, content, attributes, closable);
    }

    public Html content(String content) {
        assert closable;
        return new Html(tag, content, attributes, true);
    }

    public Html empty() {
        assert content.isEmpty();
        return new Html(tag, "", attributes, false);
    }

    public Html attributes() {
        return new Html(tag, content, attributes, closable);
    }

    // @formatter:off
    public String toString() {
        return new StringBuilder(open())
            .append(content)
            .append(close())
            .toString();
    }

    private String open() {
        return new StringBuilder("<")
            .append(tag)
            .append((attributes.isEmpty())? "" : " ")
            .append(attributes)
            .append((closable)? ">" : "/>\n")
            .toString();
    }

    private String close() {
        return new StringBuilder("</")
            .append(tag)
            .append(">\n")
            .toString();
    }
    // @formatter:on
    

}
