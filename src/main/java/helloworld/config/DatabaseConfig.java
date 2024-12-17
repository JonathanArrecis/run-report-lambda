package helloworld.config;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rdsdata.RdsDataClient;
import software.amazon.awssdk.services.rdsdata.model.ExecuteStatementRequest;
import software.amazon.awssdk.services.rdsdata.model.ExecuteStatementResponse;
import software.amazon.awssdk.services.rdsdata.model.Field;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String  CONNECTION_STRING = String.format("jdbc:mysql://%s:%s/%s?useSSL=true&requireSSL=true",
            System.getenv("HOSTNAME"),
            System.getenv("PORT"),
            System.getenv("DBNAME"));


    static {
        try {
            System.out.println("Loading MySQL JDBC driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading MySQL Driver", e);
        }
    }

    public static Connection getConnection() throws RuntimeException{
        try {
            System.out.println("Creating connection to the database");
            String token = createAuthToken();
            System.out.println("Connection to the database created");
            return DriverManager.getConnection(CONNECTION_STRING, System.getenv("DBUSERNAME"), token);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    public static String createAuthToken(){
        System.out.println("Creating RDS IAM Authentication token");
        RdsDataClient rdsDataClient = RdsDataClient.builder()
                .region(Region.of(System.getenv("REGION")))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        System.out.println("RDS IAM Authentication token created");
        ExecuteStatementRequest request = ExecuteStatementRequest.builder()
                .resourceArn(System.getenv("HOSTNAME"))
                .secretArn(System.getenv("DBUSERNAME"))
                .database(System.getenv("DBNAME"))
                .sql("SELECT 'RDS IAM Authentication'")
                .build();

        System.out.println("Executing SQL statement");
        ExecuteStatementResponse response = rdsDataClient.executeStatement(request);
        System.out.println("SQL statement executed");
        Field token = response.records().get(0).get(0);

        return token.stringValue();
    }
}
