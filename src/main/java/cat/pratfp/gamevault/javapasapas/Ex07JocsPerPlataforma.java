package cat.pratfp.gamevault.javapasapas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Pas 7: una consulta sobre tres taules (JOIN) amb dos paràmetres. */
public class Ex07JocsPerPlataforma {

    public static void main(String[] args) throws SQLException {
        String sql = """
                SELECT j.titol, jp.preu, jp.estoc
                FROM joc j
                JOIN joc_plataforma jp ON jp.joc_id = j.id
                JOIN plataforma p      ON p.id = jp.plataforma_id
                WHERE p.codi = ?
                  AND jp.preu <= ?
                ORDER BY jp.preu, j.titol
                """;

        try (Connection con = Connexio.obre();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "SWITCH");
            ps.setBigDecimal(2, new BigDecimal("20.00"));

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Jocs de Switch per 20 € o menys:");
                while (rs.next()) {
                    BigDecimal preu = rs.getBigDecimal("preu");
                    int estoc = rs.getInt("estoc");
                    System.out.printf("  %-26s %6.2f €  (%d en estoc)%n",
                            rs.getString("titol"), preu, estoc);
                }
            }
        }
    }
}
