package disco;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;

public class Migrate {
    public static void main(String[] args) {
        var ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://localhost:5432/disco-chat");
        ds.setUsername("postgres");
        ds.setPassword("example");

        Flyway.configure()
              .dataSource(ds)
              .load()
              .migrate();
    }
}