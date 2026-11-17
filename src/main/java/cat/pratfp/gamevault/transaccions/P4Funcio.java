package cat.pratfp.gamevault.transaccions;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/** P4: una funció torna un valor; es crida sola o dins d'un SELECT. */
public class P4Funcio {

    public static void main(String[] args) throws SQLException {
        try (Connection con = Connexio.obre()) {
            try (CallableStatement cs =
                         con.prepareCall("{? = call preu_amb_iva(?)}")) {
                cs.registerOutParameter(1, Types.DECIMAL);
                cs.setBigDecimal(2, new java.math.BigDecimal("59.99"));
                cs.execute();
                System.out.println("59.99 amb IVA: " + cs.getBigDecimal(1));
            }
            String sql = "SELECT j.titol, jp.preu, preu_amb_iva(jp.preu) "
                    + "AS amb_iva FROM joc j JOIN joc_plataforma jp "
                    + "ON jp.joc_id = j.id WHERE jp.plataforma_id = 6";
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.println("  " + rs.getString("titol") + ": "
                            + rs.getBigDecimal("preu") + " -> "
                            + rs.getBigDecimal("amb_iva"));
                }
            }
        }
    }
}
