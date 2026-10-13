package cat.pratfp.gamevault.sessio13;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Exemple 3: una consulta amb paràmetre (PreparedStatement) reutilitzada. */
public class Ex3JocsPerGenere {

    public static void main(String[] args) throws SQLException {
        String sql = """
                SELECT j.titol, e.nom AS estudi,
                       YEAR(j.data_llancament) AS any_sortida
                FROM joc j
                JOIN estudi e ON e.id = j.estudi_id
                WHERE j.genere = ?
                ORDER BY j.data_llancament
                """;
        String[] generes = {"RPG", "Plataformes", "Esports"};

        try (Connection con = BaseDeDades.connecta();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (String genere : generes) {
                ps.setString(1, genere);
                try (ResultSet rs = ps.executeQuery()) {
                    System.out.println("== " + genere + " ==");
                    int n = 0;
                    while (rs.next()) {
                        n++;
                        System.out.printf("  %-26s %-20s %d%n",
                                rs.getString("titol"),
                                rs.getString("estudi"),
                                rs.getInt("any_sortida"));
                    }
                    System.out.println("  " + n + " jocs");
                }
            }
        }
    }
}
