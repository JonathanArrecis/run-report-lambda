package helloworld;

import java.util.List;

public class RunReportResponse {
    private List<Transaction> transactions;

    public RunReportResponse(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
