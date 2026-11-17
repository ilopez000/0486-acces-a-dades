package cat.pratfp.gamevault.transaccions;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/** P1: un procediment que torna files, com si fos un SELECT. */
public class P1ProcedimentAmbResultat {

    public static void main(String[] args) throws SQLException {
        try (Connection con = Connexio.obre();
             CallableStatement cs =
                     con.prepareCall("{call jocs_per_genere(?)}")) {
            cs.setString(1, "Roguelike");
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    System.out.println("  - " + rs.getString("titol")
                            + " (PEGI " + rs.getInt("pegi") + ")");
                }
            }
        }
    }
}
