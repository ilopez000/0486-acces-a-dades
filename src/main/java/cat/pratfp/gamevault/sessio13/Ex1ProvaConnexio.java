package cat.pratfp.gamevault.sessio13;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/** Exemple 1: obrir una connexió amb MariaDB i preguntar-li qui és. */
public class Ex1ProvaConnexio {

    public static void main(String[] args) {
        try (Connection con = BaseDeDades.connecta()) {
            DatabaseMetaData meta = con.getMetaData();
            System.out.println("Gestor:        "
                    + meta.getDatabaseProductName() + " "
                    + meta.getDatabaseProductVersion());
            System.out.println("Driver:        "
                    + meta.getDriverName() + " " + meta.getDriverVersion());
            System.out.println("URL:           " + BaseDeDades.url());
            System.out.println("Usuari:        " + meta.getUserName());
            System.out.println("Base de dades: " + con.getCatalog());
        } catch (SQLException e) {
            System.out.println("No m'he pogut connectar: " + e.getMessage());
            System.out.println("Codi d'error: " + e.getErrorCode()
                    + " · SQLState: " + e.getSQLState());
        }
    }
}
