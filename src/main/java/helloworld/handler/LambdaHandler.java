package helloworld.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import helloworld.data.GenericResultsetData;
import helloworld.service.JsonProcessorService;
import helloworld.service.ReportService;
import software.amazon.lambda.powertools.tracing.Tracing;

import java.util.Map;

public class LambdaHandler implements RequestHandler<String, GenericResultsetData> {
    private final ReportService reportService = new ReportService();
    private final JsonProcessorService jsonProcessorService = new JsonProcessorService();


    @Tracing
    @Override
    public GenericResultsetData handleRequest(String input, Context context) {
        Map<String, String> queryParams = jsonProcessorService.getQueryParams(input);
        String nameReport = jsonProcessorService.getNameReport(input);
        return reportService.getReport(nameReport, queryParams);
    }
}
