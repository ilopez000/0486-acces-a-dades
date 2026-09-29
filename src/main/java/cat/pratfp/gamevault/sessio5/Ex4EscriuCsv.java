package cat.pratfp.gamevault.sessio5;

import cat.pratfp.gamevault.model.Joc;
import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.List;

/** Exemple 4: escriure un catàleg sencer i afegir-hi un joc al final amb OpenCSV. */
public class Ex4EscriuCsv {

    private static final String[] CAPCALERA =
            {"id", "titol", "plataforma", "estudi", "dataSortida", "horesJugades", "nota"};

    public static void main(String[] args) throws IOException {
        Path desti = Path.of("dades", "export", "cataleg_nou.csv");
        Files.createDirectories(desti.getParent());

        List<Joc> jocs = List.of(
                new Joc(1, "Hollow Knight", "PC", "Team Cherry", LocalDate.of(2017, 2, 24), 42.5, 9.5),
                new Joc(2, "Baldur's Gate 3; Deluxe", "PC", "Larian Studios", LocalDate.of(2023, 8, 3), 120.0, 9.8));

        // 1. ESCRIURE-HO TOT: sobreescriu el fitxer i posa la capçalera.
        escriu(desti, jocs);

        // 2. AFEGIR UN JOC: mode APPEND, sense repetir la capçalera.
        afegeix(desti, new Joc(3, "Gris", "PC", "Nomada Studio", LocalDate.of(2018, 12, 13), 4.0, 8.0));

        System.out.print(Files.readString(desti, StandardCharsets.UTF_8));
    }

    static void escriu(Path p, List<Joc> jocs) throws IOException {
        try (Writer w = Files.newBufferedWriter(p, StandardCharsets.UTF_8);
             CSVWriter csv = new CSVWriter(w, ';', ICSVWriter.DEFAULT_QUOTE_CHARACTER,
                     ICSVWriter.DEFAULT_ESCAPE_CHARACTER, ICSVWriter.DEFAULT_LINE_END)) {
            csv.writeNext(CAPCALERA, false);      // false = sense cometes si no calen
            for (Joc j : jocs) {
                csv.writeNext(aCamps(j), false);
            }
        }
    }

    static void afegeix(Path p, Joc j) throws IOException {
        try (Writer w = Files.newBufferedWriter(p, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
             CSVWriter csv = new CSVWriter(w, ';', ICSVWriter.DEFAULT_QUOTE_CHARACTER,
                     ICSVWriter.DEFAULT_ESCAPE_CHARACTER, ICSVWriter.DEFAULT_LINE_END)) {
            csv.writeNext(aCamps(j), false);
        }
    }

    /** Converteix un Joc en la fila de text que espera el CSV. */
    static String[] aCamps(Joc j) {
        return new String[]{String.valueOf(j.id()), j.titol(), j.plataforma(), j.estudi(),
                j.dataSortida().toString(), String.valueOf(j.horesJugades()), String.valueOf(j.nota())};
    }
}
