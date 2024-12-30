package helloworld.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class DatabaseConfig {

    private static final String DB_MODE = System.getenv("DB_MODE");

    private static final String CONNECTION_STRING_READ = String.format("jdbc:mysql://%s:%s/%s?useSSL=true&requireSSL=true",
            System.getenv("HOSTNAME_READ"),
            System.getenv("PORT_READ"),
            System.getenv("DBNAME_READ"));

    private static final String CONNECTION_STRING_WRITE = String.format("jdbc:mysql://%s:%s/%s?useSSL=true&requireSSL=true",
            System.getenv("HOSTNAME_WRITE"),
            System.getenv("PORT_WRITE"),
            System.getenv("DBNAME_WRITE"));

    private static final JdbcTemplate jdbcTemplate = initJdbcTemplate();
    private static final String DB_USERNAME_READ = System.getenv("DBUSERNAME_READ");
    private static final String DB_PASSWORD_READ = System.getenv("DBPASSWORD_READ");
    private static final String DB_USERNAME_WRITE = System.getenv("DBUSERNAME_WRITE");
    private static final String DB_PASSWORD_WRITE = System.getenv("DBPASSWORD_WRITE");

    private static final String READ_MODE = "read";
    static {
        try {
            System.out.println("Loading MySQL JDBC driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading MySQL Driver", e);
        }
    }


    private static JdbcTemplate initJdbcTemplate() {

        String mode = (DB_MODE == null || DB_MODE.isEmpty()) ? "write" : DB_MODE;
        System.out.println("DB_MODE: " + mode);

        String dbUrl = READ_MODE.equalsIgnoreCase(mode) ? CONNECTION_STRING_READ : CONNECTION_STRING_WRITE;
        String dbUsername = READ_MODE.equalsIgnoreCase(mode) ? DB_USERNAME_READ : DB_USERNAME_WRITE;
        String dbPassword = READ_MODE.equalsIgnoreCase(mode) ? DB_PASSWORD_READ : DB_PASSWORD_WRITE;

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);
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

