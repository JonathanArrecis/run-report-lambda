package helloworld;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class MysqlService {

    private static Connection connection = null;

    public Connection getConnection(String url, String user, String password) {

        if(connection == null){
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Connection to MySQL DB successful!");
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return connection;
        }

        return connection;
    }


}
