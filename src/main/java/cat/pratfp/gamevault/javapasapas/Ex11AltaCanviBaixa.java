package cat.pratfp.gamevault.javapasapas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/** Pas 11: INSERT, UPDATE i DELETE amb executeUpdate. */
public class Ex11AltaCanviBaixa {

    public static void main(String[] args) throws SQLException {
        try (Connection con = Connexio.obre()) {

            // 1) INSERT: un jugador nou
            String alta = "INSERT INTO jugador (nick, email, data_alta) "
                    + "VALUES (?, ?, ?)";
            int idNou;
            try (PreparedStatement ps = con.prepareStatement(
                    alta, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, "provajava");
                ps.setString(2, "prova.java@example.com");
                ps.setObject(3, LocalDate.of(2026, 10, 27));
                int files = ps.executeUpdate();
                try (ResultSet claus = ps.getGeneratedKeys()) {
                    claus.next();
                    idNou = claus.getInt(1);
                }
                System.out.println("INSERT: " + files + " fila, id " + idNou);
            }

            // 2) UPDATE: li posem ciutat
            String canvi = "UPDATE jugador SET ciutat = ? WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(canvi)) {
                ps.setString(1, "El Prat de Llobregat");
                ps.setInt(2, idNou);
                System.out.println("UPDATE: " + ps.executeUpdate() + " fila");
            }

            // 3) DELETE: l'esborrem
            String baixa = "DELETE FROM jugador WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(baixa)) {
                ps.setInt(1, idNou);
                System.out.println("DELETE: " + ps.executeUpdate() + " fila");
                System.out.println("DELETE un altre cop: "
                        + ps.executeUpdate() + " files");
            }
        }
    }
}
