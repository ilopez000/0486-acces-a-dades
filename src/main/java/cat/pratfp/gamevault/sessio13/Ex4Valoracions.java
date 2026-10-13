package cat.pratfp.gamevault.sessio13;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Exemple 4: una consulta d'agregació i el pas de cada fila a un objecte. */
public class Ex4Valoracions {

    /** Una fila del resultat convertida en objecte. */
    record Valoracio(String titol, int ressenyes, double mitjana) { }

    public static void main(String[] args) throws SQLException {
        String sql = """
                SELECT j.titol, COUNT(r.id) AS ressenyes,
                       ROUND(AVG(r.nota), 2) AS mitjana
                FROM joc j
                JOIN ressenya r ON r.joc_id = j.id
                GROUP BY j.id, j.titol
                HAVING COUNT(r.id) >= ?
                ORDER BY mitjana DESC, j.titol
                """;

        List<Valoracio> valoracions = new ArrayList<>();
        try (Connection con = BaseDeDades.connecta();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, 2);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    valoracions.add(new Valoracio(
                            rs.getString("titol"),
                            rs.getInt("ressenyes"),
                            rs.getDouble("mitjana")));
                }
            }
        }

        System.out.println("Jocs amb 2 ressenyes o més: "
                + valoracions.size());
        for (Valoracio v : valoracions) {
            System.out.printf("  %5.2f  (%d)  %s%n",
                    v.mitjana(), v.ressenyes(), v.titol());
        }
    }
}
