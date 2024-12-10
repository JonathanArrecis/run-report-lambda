package helloworld.service;

import helloworld.model.RunReportRequest;
import helloworld.model.GenericResultsetData;
import helloworld.repository.TransactionRepository;

public class TransactionService {
    private final TransactionRepository transactionRepository = new TransactionRepository();

    public GenericResultsetData getTransactions(RunReportRequest request){
        return transactionRepository.fetchTransactions(request);
    }
}
