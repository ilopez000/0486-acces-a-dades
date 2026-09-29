package cat.pratfp.gamevault.sessio5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

/** Exemple 1: llegir i escriure text línia a línia amb fluxos de caràcters. */
public class Ex1FluxosDeText {

    public static void main(String[] args) throws IOException {
        Path cataleg = Path.of("dades", "catalog.csv");
        Path log = Path.of("dades", "carrega.log");

        // 1. LLEGIR: BufferedReader llegeix línia a línia, amb la codificació explícita.
        int linies = 0;
        try (BufferedReader lector = Files.newBufferedReader(cataleg, StandardCharsets.UTF_8)) {
            String linia;
            while ((linia = lector.readLine()) != null) {
                linies++;
                if (linies == 1) continue;              // saltem la capçalera
                String[] camps = linia.split(";");
                System.out.println(camps[1] + " -> " + camps[5] + " h");
            }
        }
        System.out.println("Línies llegides: " + linies);

        // 2. ESCRIURE EN MODE APPEND: cada execució afegeix una línia al final del log.
        try (BufferedWriter escriptor = Files.newBufferedWriter(log, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            escriptor.write(LocalDateTime.now() + " | catàleg llegit | " + (linies - 1) + " jocs");
            escriptor.newLine();
        }
        System.out.println("Log: " + Files.readString(log, StandardCharsets.UTF_8).strip());
    }
}
