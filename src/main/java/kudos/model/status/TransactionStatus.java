package kudos.model.status;

public enum TransactionStatus {
    COMPLETED("COMPLETED"), PENDING("PENDING"), CANCELED("CANCELED"), SHOP("SHOP");

    private final String value;

    TransactionStatus(String value) {
        this.value = value;
    }

    public String toValue() {
        return value;
    }

}
