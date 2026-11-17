package cat.pratfp.gamevault.transaccions;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/** P2: un procediment que torna dades pels paràmetres OUT. */
public class P2ParametresDeSortida {

    public static void main(String[] args) throws SQLException {
        try (Connection con = Connexio.obre();
             CallableStatement cs =
                     con.prepareCall("{call total_gastat(?, ?, ?)}")) {
            cs.setInt(1, 1);                              // IN: pixelmarta
            cs.registerOutParameter(2, Types.INTEGER);    // OUT: compres
            cs.registerOutParameter(3, Types.DECIMAL);    // OUT: total
            cs.execute();
            System.out.println("pixelmarta ha fet " + cs.getInt(2)
                    + " compres i s'ha gastat " + cs.getBigDecimal(3) + " €");
        }
    }
}
