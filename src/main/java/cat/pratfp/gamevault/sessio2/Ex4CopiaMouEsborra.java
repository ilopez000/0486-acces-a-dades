package cat.pratfp.gamevault.sessio2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Exemple 4: copiar, moure i esborrar fitxers. */
public class Ex4CopiaMouEsborra {

    public static void main(String[] args) throws IOException {
        Path dades = Path.of("dades");
        Path cataleg = dades.resolve("catalog.csv");

        // 1. COPIAR: còpia de seguretat amb la data i l'hora al nom.
        DateTimeFormatter format =
                DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        String marca = LocalDateTime.now().format(format);
        Path copia = dades.resolve("backup")
                          .resolve("catalog_" + marca + ".csv");
        // REPLACE_EXISTING: si el destí ja hi és, el sobreescriu.
        Files.copy(cataleg, copia, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Copiat  -> " + copia);

        // 2. MOURE: desapareix de l'origen i apareix al destí.
        Path origen = dades.resolve("import").resolve("nous_jocs.csv");
        Path desti = dades.resolve("backup").resolve("nous_jocs.csv");
        if (Files.exists(origen)) {
            Files.move(origen, desti, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Mogut   -> " + desti);
        }

        // 3. ESBORRAR: true si l'ha esborrat, false si no hi era.
        boolean esborrat = Files.deleteIfExists(desti);
        System.out.println("Esborrat? " + esborrat);
        System.out.println("I un altre cop? " + Files.deleteIfExists(desti));
    }
}
