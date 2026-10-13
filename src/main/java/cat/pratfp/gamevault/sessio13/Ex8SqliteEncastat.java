package cat.pratfp.gamevault.sessio13;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** Exemple 8: el mateix JDBC contra SQLite, un gestor encastat. */
public class Ex8SqliteEncastat {

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:sqlite:dades/proves.db";

        try (Connection con = DriverManager.getConnection(url);
             Statement st = con.createStatement()) {

            st.execute("PRAGMA foreign_keys = ON");
            DatabaseMetaData meta = con.getMetaData();
            System.out.println("Gestor: " + meta.getDatabaseProductName()
                    + " " + meta.getDatabaseProductVersion());

            st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS plataforma (
                      id   INTEGER PRIMARY KEY AUTOINCREMENT,
                      codi TEXT NOT NULL UNIQUE,
                      nom  TEXT NOT NULL)
                    """);
            int noves = st.executeUpdate("""
                    INSERT OR IGNORE INTO plataforma (codi, nom) VALUES
                      ('PC', 'PC (Windows)'),
                      ('PS5', 'PlayStation 5'),
                      ('SWITCH2', 'Nintendo Switch 2')
                    """);
            System.out.println("Files noves: " + noves);

            String sql = "SELECT id, codi, nom FROM plataforma ORDER BY id";
            try (ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    System.out.printf("  %d  %-8s %s%n", rs.getInt("id"),
                            rs.getString("codi"), rs.getString("nom"));
                }
            }
        }
    }
}
