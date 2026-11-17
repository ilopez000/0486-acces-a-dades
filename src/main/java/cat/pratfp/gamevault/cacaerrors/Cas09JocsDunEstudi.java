package cat.pratfp.gamevault.cacaerrors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Cas09JocsDunEstudi {

    public static void main(String[] args) throws SQLException {
        String sql = "SELECT titol FROM joc WHERE estudi_id = ? "
                + "ORDER BY titol";
        try (Connection con = Connexio.obre();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(0, 4);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.println("  - " + rs.getString(1));
                }
            }
        }
    }
}
