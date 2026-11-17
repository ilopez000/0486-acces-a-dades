package cat.pratfp.gamevault.cacaerrors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Cas06PreuVersio {

    public static void main(String[] args) throws SQLException {
        String sql = "SELECT preu, estoc FROM joc_plataforma "
                + "WHERE joc_id = ? AND plataforma_id = ?";
        try (Connection con = Connexio.obre();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, 8);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Elden Ring per a PS5: "
                            + rs.getBigDecimal("preu") + " € · estoc "
                            + rs.getInt("estoc"));
                }
            }
        }
    }
}
