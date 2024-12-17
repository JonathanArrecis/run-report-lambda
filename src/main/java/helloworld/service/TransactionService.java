package helloworld.service;

import helloworld.model.RunReportRequest;
import helloworld.model.GenericResultsetData;
import helloworld.repository.TransactionRepository;
import software.amazon.lambda.powertools.tracing.Tracing;

public class TransactionService {
    private final TransactionRepository transactionRepository = new TransactionRepository();

    @Tracing
    public GenericResultsetData getTransactions(RunReportRequest request){
        System.out.println("TransactionService.getTransactions");
        return transactionRepository.fetchTransactions(request);
    }
}
