package cat.pratfp.gamevault.transaccions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** T3: què veuen altres connexions d'una transacció no acabada. */
public class T3NivellsAillament {

    static final String PREU =
            "SELECT preu FROM joc_plataforma "
            + "WHERE joc_id = 13 AND plataforma_id = 1";

    public static void main(String[] args) throws SQLException {
        try (Connection botiga = Connexio.obre();
             Connection curiosa = Connexio.obre();
             Connection prudent = Connexio.obre()) {

            curiosa.setTransactionIsolation(
                    Connection.TRANSACTION_READ_UNCOMMITTED);
            prudent.setTransactionIsolation(
                    Connection.TRANSACTION_READ_COMMITTED);

            System.out.println("Preu inicial: " + preu(prudent));

            botiga.setAutoCommit(false);
            try (Statement st = botiga.createStatement()) {
                st.executeUpdate("UPDATE joc_plataforma SET preu = 9.99 "
                        + "WHERE joc_id = 13 AND plataforma_id = 1");
            }
            System.out.println("La botiga posa 9.99 i NO fa commit");
            System.out.println("  READ UNCOMMITTED veu: " + preu(curiosa));
            System.out.println("  READ COMMITTED   veu: " + preu(prudent));

            botiga.rollback();
            botiga.setAutoCommit(true);
            System.out.println("La botiga fa rollback");
            System.out.println("  READ UNCOMMITTED veu: " + preu(curiosa));
            System.out.println("  READ COMMITTED   veu: " + preu(prudent));
        }
    }

    static String preu(Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(PREU);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getBigDecimal(1).toPlainString();
        }
    }
}
