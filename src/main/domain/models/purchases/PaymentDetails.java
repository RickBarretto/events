package main.domain.models.purchases;

public class PaymentDetails {
    private final PaymentMethod method;
    private final Double amount;

    public PaymentDetails(PaymentMethod method, Double amount) {
        this.method = method;
        this.amount = amount;
    }

    public String amount() { return "R$" + String.format("%.2f", amount); }

    public String html() {
        return method.html();
    }

}
