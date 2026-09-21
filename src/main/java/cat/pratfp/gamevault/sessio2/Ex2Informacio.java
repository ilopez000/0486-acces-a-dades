package cat.pratfp.gamevault.sessio2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/** Exemple 2: preguntar al disc com és un fitxer. */
public class Ex2Informacio {

    public static void main(String[] args) throws IOException {
        Path f = Path.of("dades", "catalog.csv");

        // Preguntes ràpides: cadascuna retorna true o false.
        System.out.println("És un fitxer?      " + Files.isRegularFile(f));
        System.out.println("És una carpeta?    " + Files.isDirectory(f));
        System.out.println("Es pot llegir?     " + Files.isReadable(f));
        System.out.println("Es pot escriure?   " + Files.isWritable(f));

        // Mida en bytes. Si el fitxer no existeix, llança excepció.
        System.out.println("Mida               " + Files.size(f) + " bytes");

        // Tots els atributs bàsics en una sola lectura del disc.
        BasicFileAttributes at =
                Files.readAttributes(f, BasicFileAttributes.class);
        System.out.println("Creat              " + at.creationTime());
        System.out.println("Última modificació " + at.lastModifiedTime());
    }
}
