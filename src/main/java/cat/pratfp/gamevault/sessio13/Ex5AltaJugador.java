package cat.pratfp.gamevault.sessio13;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;

/** Exemple 5: inserir una fila i recuperar la clau generada per MariaDB. */
public class Ex5AltaJugador {

    public static void main(String[] args) throws SQLException {
        String sql = """
                INSERT INTO jugador (nick, email, data_alta, ciutat)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = BaseDeDades.connecta();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, "gamer_prat");
            ps.setString(2, "gamer.prat@example.com");
            ps.setObject(3, LocalDate.of(2026, 10, 27));
            ps.setNull(4, Types.VARCHAR);

            int files = ps.executeUpdate();
            System.out.println("Files inserides: " + files);

            try (ResultSet claus = ps.getGeneratedKeys()) {
                if (claus.next()) {
                    System.out.println("Id del jugador nou: "
                            + claus.getLong(1));
                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("No s'ha inserit: " + e.getMessage());
        }
    }
}
