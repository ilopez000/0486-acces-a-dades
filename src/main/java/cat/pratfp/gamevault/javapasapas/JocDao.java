package cat.pratfp.gamevault.javapasapas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Totes les consultes sobre jocs, en una sola classe (DAO). */
public class JocDao {

    /** El que mostra el cercador de cada joc trobat. */
    public record FitxaJoc(int id, String titol, String estudi,
                           Double notaMitjana, int ressenyes) { }

    private final Connection con;

    public JocDao(Connection con) {
        this.con = con;
    }

    /** Jocs amb el text al títol, amb el seu estudi i la nota mitjana. */
    public List<FitxaJoc> cerca(String text) throws SQLException {
        String sql = """
                SELECT j.id, j.titol, e.nom AS estudi,
                       ROUND(AVG(r.nota), 1) AS mitjana,
                       COUNT(r.id) AS ressenyes
                FROM joc j
                JOIN estudi e        ON e.id = j.estudi_id
                LEFT JOIN ressenya r ON r.joc_id = j.id
                WHERE j.titol LIKE ?
                GROUP BY j.id, j.titol, e.nom
                ORDER BY j.titol
                """;
        List<FitxaJoc> trobats = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + text + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    trobats.add(new FitxaJoc(
                            rs.getInt("id"),
                            rs.getString("titol"),
                            rs.getString("estudi"),
                            rs.getObject("mitjana", Double.class),
                            rs.getInt("ressenyes")));
                }
            }
        }
        return trobats;
    }

    /** Les plataformes on es ven un joc, amb el preu. */
    public List<String> plataformes(int jocId) throws SQLException {
        String sql = """
                SELECT p.codi, jp.preu
                FROM joc_plataforma jp
                JOIN plataforma p ON p.id = jp.plataforma_id
                WHERE jp.joc_id = ?
                ORDER BY jp.preu, p.codi
                """;
        List<String> llista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, jocId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    llista.add(rs.getString("codi") + " "
                            + rs.getBigDecimal("preu") + " €");
                }
            }
        }
        return llista;
    }
}
