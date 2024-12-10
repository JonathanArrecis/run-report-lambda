package helloworld;

public class Transaction {

    private String savings_account_id;
    private String description;
    private String amount;
    private String operation_type;

    public Transaction(String savings_account_id, String description, String amount, String operation_type) {
        this.savings_account_id = savings_account_id;
        this.description = description;
        this.amount = amount;
        this.operation_type = operation_type;
    }

    public Transaction() {
    }

    public String getSavings_account_id() {
        return savings_account_id;
    }

    public void setSavings_account_id(String savings_account_id) {
        this.savings_account_id = savings_account_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOperation_type() {
        return operation_type;
    }

    public void setOperation_type(String operation_type) {
        this.operation_type = operation_type;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "savings_account_id='" + savings_account_id + '\'' +
                ", description='" + description + '\'' +
                ", amount='" + amount + '\'' +
                ", operation_type='" + operation_type + '\'' +
                '}';
    }
}
