package cat.pratfp.gamevault.javapasapas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Pas 1: obrir una connexió amb la base de dades i tancar-la. */
public class Ex01Connecta {

    public static void main(String[] args) {
        String url = "jdbc:mariadb://localhost:3306/gamevault_proves";
        String usuari = "dam2";
        String contrasenya = "dam2pass";

        try (Connection con =
                     DriverManager.getConnection(url, usuari, contrasenya)) {
            System.out.println("Connectat a " + con.getCatalog());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
