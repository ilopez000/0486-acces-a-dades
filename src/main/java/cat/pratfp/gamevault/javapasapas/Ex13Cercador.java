package cat.pratfp.gamevault.javapasapas;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/** Pas 13: una petita aplicació que fa servir el DAO. */
public class Ex13Cercador {

    public static void main(String[] args) {
        Scanner teclat = new Scanner(System.in);

        try (Connection con = Connexio.obre()) {
            JocDao dao = new JocDao(con);

            while (true) {
                System.out.print("Cerca (Enter per sortir): ");
                String text = teclat.nextLine().trim();
                if (text.isEmpty()) {
                    break;
                }

                List<JocDao.FitxaJoc> jocs = dao.cerca(text);
                if (jocs.isEmpty()) {
                    System.out.println("  Cap joc conté «" + text + "»");
                }
                for (JocDao.FitxaJoc f : jocs) {
                    String nota = (f.notaMitjana() == null) ? "sense nota"
                            : f.notaMitjana() + " (" + f.ressenyes() + ")";
                    System.out.println("  " + f.titol() + " · " + f.estudi()
                            + " · " + nota);
                    List<String> plat = dao.plataformes(f.id());
                    System.out.println("    "
                            + (plat.isEmpty() ? "no està a la venda" : plat));
                }
            }
            System.out.println("Fins aviat!");
        } catch (SQLException e) {
            System.out.println("Error de base de dades: " + e.getMessage());
        }
    }
}
