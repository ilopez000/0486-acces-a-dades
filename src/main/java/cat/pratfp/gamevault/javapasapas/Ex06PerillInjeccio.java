package cat.pratfp.gamevault.javapasapas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** Pas 6: per què no s'ha d'enganxar mai un text a la consulta. */
public class Ex06PerillInjeccio {

    public static void main(String[] args) throws SQLException {
        // El que un usuari «espavilat» escriu al camp del nick
        String nick = "x' OR '1'='1";

        try (Connection con = Connexio.obre()) {
            System.out.println("== Concatenant (MALAMENT) ==");
            String sql = "SELECT nick, email FROM jugador "
                    + "WHERE nick = '" + nick + "'";
            System.out.println("SQL enviat: " + sql);
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                mostra(rs);
            }

            System.out.println("== Amb PreparedStatement (BÉ) ==");
            String segur = "SELECT nick, email FROM jugador WHERE nick = ?";
            try (PreparedStatement ps = con.prepareStatement(segur)) {
                ps.setString(1, nick);
                try (ResultSet rs = ps.executeQuery()) {
                    mostra(rs);
                }
            }
        }
    }

    static void mostra(ResultSet rs) throws SQLException {
        int n = 0;
        while (rs.next()) {
            n++;
            if (n <= 3) {
                System.out.println("  " + rs.getString("nick") + " <"
                        + rs.getString("email") + ">");
            }
        }
        System.out.println("  Files retornades: " + n);
    }
}
