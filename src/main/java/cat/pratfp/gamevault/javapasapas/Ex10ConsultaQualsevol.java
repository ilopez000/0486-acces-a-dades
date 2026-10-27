package cat.pratfp.gamevault.javapasapas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/** Pas 10: mostrar el resultat de qualsevol SELECT sense conèixer-lo. */
public class Ex10ConsultaQualsevol {

    public static void main(String[] args) throws SQLException {
        try (Connection con = Connexio.obre()) {
            mostra(con, "SELECT codi, nom, fabricant FROM plataforma "
                    + "WHERE fabricant = 'Nintendo'");
            mostra(con, "SELECT genere, COUNT(*) AS jocs FROM joc "
                    + "GROUP BY genere HAVING COUNT(*) >= 3");
        }
    }

    static void mostra(Connection con, String sql) throws SQLException {
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            ResultSetMetaData meta = rs.getMetaData();
            int columnes = meta.getColumnCount();

            System.out.println(sql);
            for (int i = 1; i <= columnes; i++) {
                System.out.printf("%-20s", meta.getColumnLabel(i));
            }
            System.out.println();

            while (rs.next()) {
                for (int i = 1; i <= columnes; i++) {
                    System.out.printf("%-20s", rs.getString(i));
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
