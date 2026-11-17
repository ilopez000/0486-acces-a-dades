package cat.pratfp.gamevault.cacaerrors;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Cas08CercaGenere {

    public static void main(String[] args) throws SQLException {
        String entrada = "x' OR '1'='1";   // el que ha escrit l'usuari
        String sql = "SELECT titol FROM joc WHERE genere = '"
                + entrada + "' ORDER BY titol";
        try (Connection con = Connexio.obre();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            int n = 0;
            while (rs.next()) {
                n++;
                System.out.println("  - " + rs.getString("titol"));
            }
            if (n == 0) {
                System.out.println("No hi ha cap joc del gènere " + entrada);
            }
        }
    }
}
