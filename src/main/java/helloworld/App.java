package helloworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<RunReportRequest, RunReportResponse> {

    RunReportService runReportService = new RunReportService();
    MysqlService mysqlService = new MysqlService();



    public RunReportResponse handleRequest(final RunReportRequest input, final Context context) {

        String dbUrl = "jdbc:mysql://10.26.4.143:3306/NexaCoreDB";
        String dbUser = "root";
        String dbPassword = "Pruebas123";

        Connection conn = mysqlService.getConnection(dbUrl, dbUser, dbPassword);
        List<Transaction> transactions = runReportService.getTransactions(conn,input);
        RunReportResponse runReportResponse = new RunReportResponse(transactions);
        return runReportResponse;

    }

    private String getPageContents(String address) throws IOException{
        URL url = new URL(address);
        try(BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
