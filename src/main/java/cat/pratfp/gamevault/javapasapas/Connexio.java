package cat.pratfp.gamevault.javapasapas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Un sol lloc on hi ha les dades de connexió. */
public final class Connexio {

    private static final String URL =
            "jdbc:mariadb://localhost:3306/gamevault_proves";
    private static final String USUARI = "dam2";
    private static final String CONTRASENYA = "dam2pass";

    private Connexio() { }

    /** Obre una connexió nova. Qui la demana l'ha de tancar. */
    public static Connection obre() throws SQLException {
        return DriverManager.getConnection(URL, USUARI, CONTRASENYA);
    }
}
