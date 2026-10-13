package cat.pratfp.gamevault.sessio13;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Obre connexions amb MariaDB. Les dades de connexió són a db.properties. */
public final class BaseDeDades {

    private static final Properties CONFIG = carregaConfiguracio();

    private BaseDeDades() { }

    private static Properties carregaConfiguracio() {
        Properties p = new Properties();
        try (InputStream in =
                 BaseDeDades.class.getResourceAsStream("/db.properties")) {
            if (in == null) {
                throw new IllegalStateException("Falta src/main/resources/"
                        + "db.properties: copia db.properties.example");
            }
            p.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return p;
    }

    /** La URL JDBC de la base de dades (sense usuari ni contrasenya). */
    static String url() {
        return CONFIG.getProperty("db.url");
    }

    /** Obre una connexió nova. Qui la demana l'ha de tancar. */
    static Connection connecta() throws SQLException {
        return DriverManager.getConnection(
                CONFIG.getProperty("db.url"),
                CONFIG.getProperty("db.user"),
                CONFIG.getProperty("db.password"));
    }
}
