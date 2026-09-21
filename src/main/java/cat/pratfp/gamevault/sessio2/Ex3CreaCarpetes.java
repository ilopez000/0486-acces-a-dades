package cat.pratfp.gamevault.sessio2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Exemple 3: crear carpetes i un fitxer de text. */
public class Ex3CreaCarpetes {

    public static void main(String[] args) throws IOException {
        Path dades = Path.of("dades");

        // createDirectories: crea la carpeta i les que faltin pel camí.
        // Si ja existeix, no fa res: es pot executar sempre.
        Files.createDirectories(dades.resolve("import"));
        Files.createDirectories(dades.resolve("export"));
        Files.createDirectories(dades.resolve("backup"));
        System.out.println("Carpetes import, export i backup a punt.");

        // createDirectory (sense «s») és estricte: falla si ja hi és.
        try {
            Files.createDirectory(dades.resolve("import"));
        } catch (FileAlreadyExistsException e) {
            System.out.println("createDirectory ha fallat: "
                    + e.getFile() + " ja existeix.");
        }

        // writeString: crea un fitxer de text (o el sobreescriu).
        Path nou = dades.resolve("import").resolve("nous_jocs.csv");
        String linia = "6;Hades;PC;Supergiant Games;2020-09-17;35.0;9.5\n";
        Files.writeString(nou, linia, StandardCharsets.UTF_8);
        System.out.println("Fitxer creat: " + nou
                + " (" + Files.size(nou) + " bytes)");
    }
}
