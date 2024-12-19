package helloworld.config;


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
            String username = System.getenv("DBUSERNAME");
            String pass = System.getenv("DBPASSWORD");
            return DriverManager.getConnection(CONNECTION_STRING,username, pass);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

}
