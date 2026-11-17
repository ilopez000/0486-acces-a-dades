package cat.pratfp.gamevault.cacaerrors;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Cas02LlistaEstudis {

    public static void main(String[] args) throws SQLException {
        String sql = "SELECT nom, pais FROM estudis ORDER BY nom";
        try (Connection con = Connexio.obre();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getString("nom")
                        + " (" + rs.getString("pais") + ")");
            }
        }
    }
}
