package cat.pratfp.gamevault.javapasapas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** Pas 3: recórrer totes les files d'un resultat. */
public class Ex03LlistaTitols {

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mariadb://localhost:3306/gamevault_proves";
        String sql = "SELECT titol FROM joc WHERE genere = 'RPG' "
                + "ORDER BY titol";

        try (Connection con =
                     DriverManager.getConnection(url, "dam2", "dam2pass");
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            int n = 0;
            while (rs.next()) {
                n++;
                System.out.println(n + ". " + rs.getString("titol"));
            }
            System.out.println("Total: " + n + " jocs RPG");
        }
    }
}
