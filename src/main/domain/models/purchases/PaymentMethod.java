package main.domain.models.purchases;

import main.domain.models.email.Html;

public class PaymentMethod {
    private final String method;
    private final String details;

    public PaymentMethod(String method, String details) {
        this.method = method;
        this.details = details;
    }

    public String html() {
    // @formatter:off
    return new StringBuilder(header())
        .append(details())
        .toString();
    }
    
    private String header() {
        var header = Html.node("h2")
        .content("Payment Method")
        .toString();
        return header;
    }
    
    private String details() {
        final var methodHtml = listItem("Method", method);
        final var detailsHtml = listItem("Details", details);
        return  Html.node("ul").content(methodHtml + detailsHtml).toString();
    }
    // @formatter:on

    private String listItem(String title, String description) {
        return Html.node("li").content(title + ": " + description).toString();
    }
}
