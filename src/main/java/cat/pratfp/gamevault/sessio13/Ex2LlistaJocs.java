package cat.pratfp.gamevault.sessio13;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/** Exemple 2: una consulta fixa amb Statement i el recorregut del ResultSet. */
public class Ex2LlistaJocs {

    public static void main(String[] args) throws SQLException {
        String sql = """
                SELECT id, titol, genere, data_llancament, pegi
                FROM joc
                WHERE data_llancament >= '2023-01-01'
                   OR data_llancament IS NULL
                ORDER BY data_llancament
                """;

        try (Connection con = BaseDeDades.connecta();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String titol = rs.getString("titol");
                String genere = rs.getString("genere");
                LocalDate data =
                        rs.getObject("data_llancament", LocalDate.class);
                int pegi = rs.getInt("pegi");
                boolean senseNota = rs.wasNull();

                System.out.printf("%2d | %-42s | %-12s | %-10s | PEGI %s%n",
                        id, titol, genere,
                        data == null ? "sense data" : data,
                        senseNota ? "-" : pegi);
            }
        }
    }
}
