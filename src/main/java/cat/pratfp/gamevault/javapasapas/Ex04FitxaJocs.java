package cat.pratfp.gamevault.javapasapas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/** Pas 4: llegir columnes de tipus diferents i els valors NULL. */
public class Ex04FitxaJocs {

    public static void main(String[] args) throws SQLException {
        String sql = """
                SELECT id, titol, data_llancament, pegi
                FROM joc
                WHERE estudi_id = 2
                ORDER BY id
                """;

        try (Connection con = Connexio.obre();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String titol = rs.getString("titol");
                LocalDate data =
                        rs.getObject("data_llancament", LocalDate.class);
                Integer pegi = rs.getObject("pegi", Integer.class);

                String textData = (data == null) ? "sense data"
                        : data.getDayOfMonth() + "/" + data.getMonthValue()
                          + "/" + data.getYear();
                String textPegi = (pegi == null) ? "pendent" : "PEGI " + pegi;

                System.out.println(id + " · " + titol + " · " + textData
                        + " · " + textPegi);
            }
        }
    }
}
