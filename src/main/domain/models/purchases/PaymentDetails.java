package main.domain.models.purchases;

public class PaymentDetails {
    private final String method;
    private final String details;
    private final Double amount;

    public PaymentDetails(String method, String details, Double amount) {
        this.method = method;
        this.details = details;
        this.amount = amount;
    }

    public String amount() { return "R$" + String.format("%.2f", amount); }

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
