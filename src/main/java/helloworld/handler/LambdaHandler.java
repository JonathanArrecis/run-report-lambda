package helloworld.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import helloworld.model.RunReportRequest;
import helloworld.model.GenericResultsetData;
import helloworld.service.TransactionService;
import software.amazon.lambda.powertools.tracing.Tracing;

public class LambdaHandler implements RequestHandler<RunReportRequest, GenericResultsetData> {
    private final TransactionService transactionService = new TransactionService();

    @Tracing
    @Override
    public GenericResultsetData handleRequest(RunReportRequest runReportRequest, Context context) {
        return transactionService.getTransactions(runReportRequest);
    }
}
