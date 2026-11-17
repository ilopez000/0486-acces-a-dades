package cat.pratfp.gamevault.cacaerrors;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Cas07AnysEstudis {

    public static void main(String[] args) throws SQLException {
        String sql = "SELECT nom, any_fundacio FROM estudi ORDER BY nom";
        try (Connection con = Connexio.obre();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                int any = rs.getInt("any_fundacio");
                String text = (any == -1) ? "any desconegut"
                                          : "fundat el " + any;
                System.out.println(rs.getString("nom") + ": " + text);
            }
        }
    }
}
