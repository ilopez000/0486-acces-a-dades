package cat.pratfp.gamevault.javapasapas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Pas 8: passar les files a objectes i treballar amb la llista. */
public class Ex08DeFilesAObjectes {

    public static void main(String[] args) throws SQLException {
        List<Versio> versions = llegeixVersions("Sony");

        System.out.println("Versions per a consoles de Sony: "
                + versions.size());

        Versio mesCara = versions.stream()
                .max(Comparator.comparing(Versio::preu))
                .orElseThrow();
        System.out.println("La més cara: " + mesCara.titol()
                + " (" + mesCara.plataforma() + ") " + mesCara.preu() + " €");

        List<String> esgotades = versions.stream()
                .filter(v -> v.estoc() == 0)
                .map(v -> v.titol() + " (" + v.plataforma() + ")")
                .toList();
        System.out.println("Esgotades: " + esgotades);

        BigDecimal valorEstoc = BigDecimal.ZERO;
        for (Versio v : versions) {
            BigDecimal valor = v.preu().multiply(BigDecimal.valueOf(v.estoc()));
            valorEstoc = valorEstoc.add(valor);
        }
        System.out.println("Valor de l'estoc: " + valorEstoc + " €");
    }

    static List<Versio> llegeixVersions(String fabricant)
            throws SQLException {
        String sql = """
                SELECT j.titol, p.codi, jp.preu, jp.estoc
                FROM joc_plataforma jp
                JOIN joc j        ON j.id = jp.joc_id
                JOIN plataforma p ON p.id = jp.plataforma_id
                WHERE p.fabricant = ?
                ORDER BY j.titol, p.codi
                """;
        List<Versio> llista = new ArrayList<>();
        try (Connection con = Connexio.obre();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fabricant);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    llista.add(new Versio(
                            rs.getString("titol"),
                            rs.getString("codi"),
                            rs.getBigDecimal("preu"),
                            rs.getInt("estoc")));
                }
            }
        }
        return llista;
    }
}
