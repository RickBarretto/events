package main.domain.models.purchases;

import main.domain.models.users.User;

public class Participant {
    private String email;
    private String name;
    private String taxId;

    private Participant(String email, String name, String taxId) {
        this.email = email;
        this.name = name;
        this.taxId = taxId;
    }

    static public Participant customer(User customer) {
        return new Participant(customer.login().email(),
                customer.person().name(), customer.person().cpf());
    }

    static public Participant business() {
        var business = new EventManagementBusiness();
        return new Participant(business.email(), business.name(),
                business.cpnj());
    }

    public String email() { return email; }

    @Override
    public String toString() {
        // @formatter:off
        return new StringBuilder(name)
            .append(" (")
            .append(taxId)
            .append(")")
            .toString();
        // @formatter:on
    }

}
