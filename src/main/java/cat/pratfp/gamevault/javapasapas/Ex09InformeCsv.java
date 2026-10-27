package cat.pratfp.gamevault.javapasapas;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** Pas 9: desar el resultat d'una consulta en un fitxer CSV. */
public class Ex09InformeCsv {

    public static void main(String[] args) throws SQLException, IOException {
        String sql = """
                SELECT e.nom AS estudi, COUNT(c.id) AS vendes,
                       SUM(c.preu_pagat) AS facturat
                FROM estudi e
                JOIN joc j    ON j.estudi_id = e.id
                JOIN compra c ON c.joc_id = j.id
                GROUP BY e.id, e.nom
                ORDER BY facturat DESC
                """;
        Path fitxer = Path.of("dades", "export", "facturacio_estudis.csv");
        Files.createDirectories(fitxer.getParent());

        try (Connection con = Connexio.obre();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql);
             BufferedWriter out =
                     Files.newBufferedWriter(fitxer, StandardCharsets.UTF_8)) {

            out.write("estudi;vendes;facturat");
            out.newLine();
            int files = 0;
            while (rs.next()) {
                out.write(rs.getString("estudi") + ";"
                        + rs.getInt("vendes") + ";"
                        + rs.getBigDecimal("facturat"));
                out.newLine();
                files++;
            }
            System.out.println("Escrites " + files + " files a " + fitxer);
        }
    }
}
