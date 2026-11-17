package cat.pratfp.gamevault.cacaerrors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Cas04FitxaJoc {

    public static void main(String[] args) throws SQLException {
        String sql = "SELECT titol, genere, pegi FROM joc WHERE id = ?";
        try (Connection con = Connexio.obre();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, 8);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println(rs.getString("title") + " · "
                            + rs.getString("genere") + " · PEGI "
                            + rs.getInt("pegi"));
                }
            }
        }
    }
}
