package cat.pratfp.gamevault.transaccions;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

/** P3: la lògica viu a MariaDB; Java decideix commit o rollback. */
public class P3CompraAmbProcediment {

    public static void main(String[] args) throws SQLException {
        try (Connection con = Connexio.obre()) {
            compra(con, 5, 8, 1);    // zeldafan: Elden Ring per a PC
            compra(con, 5, 5, 3);    // GTA V per a PS4: estoc 0
            compra(con, 99, 8, 1);   // el jugador 99 no existeix
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT estoc FROM "
                         + "joc_plataforma WHERE joc_id = 8 "
                         + "AND plataforma_id = 1")) {
                rs.next();
                System.out.println("Estoc d'Elden Ring per a PC: "
                        + rs.getInt(1) + " (abans n'hi havia 18)");
            }
        }
    }

    static void compra(Connection con, int jugador, int joc, int plat)
            throws SQLException {
        con.setAutoCommit(false);
        try (CallableStatement cs =
                     con.prepareCall("{call registra_compra(?, ?, ?, ?)}")) {
            cs.setInt(1, jugador);
            cs.setInt(2, joc);
            cs.setInt(3, plat);
            cs.registerOutParameter(4, Types.INTEGER);
            cs.execute();
            con.commit();
            System.out.println("Compra " + cs.getInt(4) + " feta");
        } catch (SQLException e) {
            con.rollback();
            System.out.println("Desfeta [" + e.getSQLState() + "] "
                    + e.getMessage());
        } finally {
            con.setAutoCommit(true);
        }
    }
}
