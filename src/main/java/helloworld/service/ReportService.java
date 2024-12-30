package helloworld.service;

import helloworld.data.GenericResultsetData;
import helloworld.repository.ReportRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import software.amazon.lambda.powertools.tracing.Tracing;

import java.util.Map;

public final  class ReportService {
    private final ReportRepository reportRepository = new ReportRepository();
    @Tracing
    public GenericResultsetData getReport(final String nameReport,final Map<String,String>  queryParams){
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        final String sql = reportRepository.getSqlToRun(nameReport,queryParams,namedParameters);
        System.out.println("Running sql: " + sql);
        final GenericResultsetData genericResultsetData = reportRepository.fillGenericResultSetData(sql,namedParameters);
        System.out.println("Result: " + genericResultsetData.getData().size() + " rows");
        return genericResultsetData;
    }




}
