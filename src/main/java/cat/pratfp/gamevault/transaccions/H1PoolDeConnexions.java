package cat.pratfp.gamevault.transaccions;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** H1: obrir 200 connexions de zero o demanar-les a un pool. */
public class H1PoolDeConnexions {

    static final String URL = "jdbc:mariadb://localhost:3306/gamevault_proves";

    public static void main(String[] args) throws SQLException {
        long t0 = System.nanoTime();
        for (int i = 0; i < 200; i++) {
            try (Connection con =
                         DriverManager.getConnection(URL, "dam2", "dam2pass")) {
                con.isValid(1);
            }
        }
        long sensePool = (System.nanoTime() - t0) / 1_000_000;

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(URL);
        cfg.setUsername("dam2");
        cfg.setPassword("dam2pass");
        cfg.setMaximumPoolSize(5);
        try (HikariDataSource pool = new HikariDataSource(cfg)) {
            long t1 = System.nanoTime();
            for (int i = 0; i < 200; i++) {
                try (Connection con = pool.getConnection()) {
                    con.isValid(1);           // close() la torna al pool
                }
            }
            long ambPool = (System.nanoTime() - t1) / 1_000_000;
            System.out.println("200 connexions noves: " + sensePool + " ms");
            System.out.println("200 préstecs del pool: " + ambPool + " ms");
        }
    }
}
