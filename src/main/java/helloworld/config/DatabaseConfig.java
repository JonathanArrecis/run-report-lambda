package helloworld.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

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
    private static final JdbcTemplate jdbcTemplate = initJdbcTemplate();

    private static JdbcTemplate initJdbcTemplate() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(CONNECTION_STRING);
        config.setUsername(System.getenv("DBUSERNAME"));
        config.setPassword(System.getenv("DBPASSWORD"));
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(System.getenv("MAX_POOL_SIZE") != null ? Integer.parseInt(System.getenv("MAX_POOL_SIZE")) : 10);
        config.setMinimumIdle(System.getenv("MIN_IDLE") != null ? Integer.parseInt(System.getenv("MIN_IDLE")) : 2);
        config.setConnectionTimeout(System.getenv("CONNECTION_TIMEOUT") != null ? Long.parseLong(System.getenv("CONNECTION_TIMEOUT")) : 30000);
        config.setIdleTimeout(System.getenv("IDLE_TIMEOUT") != null ? Long.parseLong(System.getenv("IDLE_TIMEOUT")) : 600000);
        config.setMaxLifetime(System.getenv("MAX_LIFETIME") != null ? Long.parseLong(System.getenv("MAX_LIFETIME")) : 1800000);
        DataSource dataSource = new HikariDataSource(config);
        return new JdbcTemplate(dataSource);
    }

    public static JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
