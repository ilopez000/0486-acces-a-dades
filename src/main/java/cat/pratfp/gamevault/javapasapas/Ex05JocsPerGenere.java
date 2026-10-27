package cat.pratfp.gamevault.javapasapas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/** Pas 5: una consulta amb un valor que escriu l'usuari. */
public class Ex05JocsPerGenere {

    public static void main(String[] args) throws SQLException {
        Scanner teclat = new Scanner(System.in);
        System.out.print("Quin gènere vols? ");
        String genere = teclat.nextLine().trim();

        String sql = "SELECT titol, pegi FROM joc WHERE genere = ? "
                + "ORDER BY titol";

        try (Connection con = Connexio.obre();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, genere);

            try (ResultSet rs = ps.executeQuery()) {
                int n = 0;
                while (rs.next()) {
                    n++;
                    System.out.println("  - " + rs.getString("titol"));
                }
                if (n == 0) {
                    System.out.println("No hi ha cap joc de " + genere);
                }
            }
        }
    }
}
