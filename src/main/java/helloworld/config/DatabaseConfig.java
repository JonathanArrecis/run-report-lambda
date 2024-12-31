package helloworld.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Optional;

public class DatabaseConfig {


    private static final String DB_MODE = Optional.ofNullable(System.getenv("DB_MODE")).orElse("write");
    private static final JdbcTemplate jdbcTemplate = initJdbcTemplate();


    private static JdbcTemplate initJdbcTemplate() {

        final String CONNECTION_STRING_READ = String.format("jdbc:mysql://%s:%s/%s?useSSL=true&requireSSL=true",
                System.getenv("HOSTNAME_READ"),
                System.getenv("PORT_READ"),
                System.getenv("DB_NAME_READ"));

        final String CONNECTION_STRING_WRITE = String.format("jdbc:mysql://%s:%s/%s?useSSL=true&requireSSL=true",
                System.getenv("HOSTNAME_WRITE"),
                System.getenv("PORT_WRITE"),
                System.getenv("DB_NAME_WRITE"));

        final String DB_USERNAME_READ = System.getenv("DB_USERNAME_READ");
        final String DB_PASSWORD_READ = System.getenv("DB_PASSWORD_READ");
        final String DB_USERNAME_WRITE = System.getenv("DB_USERNAME_WRITE");
        final String DB_PASSWORD_WRITE = System.getenv("DB_PASSWORD_WRITE");
        final String READ_MODE = "read";

        String mode = DB_MODE.equalsIgnoreCase("read") ? "read" : "write";
        System.out.println("DB_MODE: " + mode);

        String dbUrl = READ_MODE.equalsIgnoreCase(mode) ? CONNECTION_STRING_READ : CONNECTION_STRING_WRITE;
        System.out.println("DB_URL: " + dbUrl);
        String dbUsername = READ_MODE.equalsIgnoreCase(mode) ? DB_USERNAME_READ : DB_USERNAME_WRITE;
        System.out.println("DB_USERNAME_write: " + DB_USERNAME_WRITE);
        System.out.println("DB_USERNAME_read: " + DB_USERNAME_READ);
        System.out.println("DB_USERNAME: " + dbUsername);
        String dbPassword = READ_MODE.equalsIgnoreCase(mode) ? DB_PASSWORD_READ : DB_PASSWORD_WRITE;
        System.out.println("DB_PASSWORD_write: " + DB_PASSWORD_WRITE);
        System.out.println("DB_PASSWORD_read: " + DB_PASSWORD_READ);
        System.out.println("DB_PASSWORD: " + dbPassword);

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

