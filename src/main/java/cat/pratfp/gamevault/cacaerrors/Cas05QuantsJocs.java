package cat.pratfp.gamevault.cacaerrors;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Cas05QuantsJocs {

    public static void main(String[] args) throws SQLException {
        try (Connection con = Connexio.obre();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM joc")) {
            int total = rs.getInt(1);
            System.out.println("Hi ha " + total + " jocs al catàleg");
        }
    }
}
