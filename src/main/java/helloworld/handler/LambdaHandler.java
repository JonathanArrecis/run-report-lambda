package helloworld.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import helloworld.data.GenericResultsetData;
import helloworld.service.JsonProcessorService;
import helloworld.service.ReportService;
import software.amazon.lambda.powertools.tracing.Tracing;

import java.util.Map;
import java.util.Objects;

public class LambdaHandler implements RequestHandler<Map<String,Object>,GenericResultsetData> {
    private final ReportService reportService = new ReportService();
    private final JsonProcessorService jsonProcessorService = new JsonProcessorService();


    @Tracing
    @Override
    public GenericResultsetData  handleRequest(Map<String, Object> input, Context context) {
        if (Objects.isNull(input)) {
            throw new IllegalArgumentException("Input is required");
        }
        System.out.println("Input: " + input.toString());
        Map<String, String> queryParams = jsonProcessorService.getQueryParams(input);
        String nameReport = jsonProcessorService.getNameReport(input);
        return reportService.getReport(nameReport, queryParams);
    }
}
