package cat.pratfp.gamevault.sessio13;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/** Exemple 7: esborrar files (DELETE) i topar amb una clau forana. */
public class Ex7EsborraJugador {

    public static void main(String[] args) throws SQLException {
        String sql = "DELETE FROM jugador WHERE nick = ?";
        String[] nicks = {"gamer_prat", "gamer_prat", "pixelmarta"};

        try (Connection con = BaseDeDades.connecta();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (String nick : nicks) {
                ps.setString(1, nick);
                try {
                    int files = ps.executeUpdate();
                    System.out.println("DELETE " + nick
                            + " → files esborrades: " + files);
                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println("DELETE " + nick
                            + " → no es pot: " + e.getMessage());
                }
            }
        }
    }
}
