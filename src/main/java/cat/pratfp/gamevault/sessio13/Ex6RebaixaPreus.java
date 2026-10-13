package cat.pratfp.gamevault.sessio13;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Exemple 6: modificar files (UPDATE) i comprovar-ho abans i després. */
public class Ex6RebaixaPreus {

    static final String CONSULTA = """
            SELECT j.titol, jp.preu
            FROM joc_plataforma jp
            JOIN joc j ON j.id = jp.joc_id
            JOIN plataforma p ON p.id = jp.plataforma_id
            WHERE p.codi = ?
            ORDER BY j.titol
            """;

    static final String REBAIXA = """
            UPDATE joc_plataforma
            SET preu = ROUND(preu * (100 - ?) / 100, 2)
            WHERE plataforma_id =
                  (SELECT id FROM plataforma WHERE codi = ?)
            """;

    public static void main(String[] args) throws SQLException {
        String plataforma = "SWITCH2";
        int percentatge = 10;

        try (Connection con = BaseDeDades.connecta()) {
            mostraPreus(con, plataforma, "Abans");

            try (PreparedStatement ps = con.prepareStatement(REBAIXA)) {
                ps.setInt(1, percentatge);
                ps.setString(2, plataforma);
                int files = ps.executeUpdate();
                System.out.println("Rebaixa del " + percentatge
                        + "% · files modificades: " + files);
            }

            mostraPreus(con, plataforma, "Després");
        }
    }

    static void mostraPreus(Connection con, String plataforma,
                            String moment) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(CONSULTA)) {
            ps.setString(1, plataforma);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println(moment + ":");
                while (rs.next()) {
                    System.out.printf("  %6.2f €  %s%n",
                            rs.getBigDecimal("preu"), rs.getString("titol"));
                }
            }
        }
    }
}
