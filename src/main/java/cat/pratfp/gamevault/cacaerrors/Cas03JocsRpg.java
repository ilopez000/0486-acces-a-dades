package cat.pratfp.gamevault.cacaerrors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Cas03JocsRpg {

    public static void main(String[] args) throws SQLException {
        String sql = "SELECT titol FROM joc"
                + "WHERE genere = ?"
                + " ORDER BY titol";
        try (Connection con = Connexio.obre();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "RPG");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.println("  - " + rs.getString("titol"));
                }
            }
        }
    }
}
