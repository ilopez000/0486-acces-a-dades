package cat.pratfp.gamevault.cacaerrors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Cas01Connecta {

    static final String URL = "jdbc:mariadb://localhost:3306/gamevault_proves";
    static final String USUARI = "dam2";
    static final String CONTRASENYA = "dam2pas";

    public static void main(String[] args) throws SQLException {
        try (Connection con =
                 DriverManager.getConnection(URL, USUARI, CONTRASENYA)) {
            System.out.println("Connectat a " + con.getCatalog());
            System.out.println("Servidor: MariaDB "
                    + con.getMetaData().getDatabaseProductVersion());
        }
    }
}
