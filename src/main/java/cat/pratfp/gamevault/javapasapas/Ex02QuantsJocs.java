package cat.pratfp.gamevault.javapasapas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** Pas 2: una consulta que torna un sol valor. */
public class Ex02QuantsJocs {

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mariadb://localhost:3306/gamevault_proves";

        try (Connection con =
                     DriverManager.getConnection(url, "dam2", "dam2pass");
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM joc")) {

            rs.next();
            int quants = rs.getInt(1);
            System.out.println("Al catàleg hi ha " + quants + " jocs");
        }
    }
}
