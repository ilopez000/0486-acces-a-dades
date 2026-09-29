package cat.pratfp.gamevault.model;

import java.time.LocalDate;

/**
 * Un joc del catàleg GameVault.
 * És un record: immutable, amb constructor, accessors, equals, hashCode i toString generats.
 * El constructor compacte valida les dades abans de crear l'objecte.
 */
public record Joc(int id, String titol, String plataforma, String estudi,
                  LocalDate dataSortida, double horesJugades, double nota) {

    public Joc {
        if (titol == null || titol.isBlank()) {
            throw new IllegalArgumentException("el títol no pot ser buit");
        }
        if (horesJugades < 0) {
            throw new IllegalArgumentException("les hores no poden ser negatives: " + horesJugades);
        }
        if (nota < 0 || nota > 10) {
            throw new IllegalArgumentException("la nota ha d'anar de 0 a 10: " + nota);
        }
    }
}
