package cat.pratfp.gamevault.sessio2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/** Exemple 5: recórrer carpetes amb Files.list i Files.walk. */
public class Ex5Recorre {

    public static void main(String[] args) throws IOException {
        Path dades = Path.of("dades");

        // Files.list: NOMÉS el primer nivell (no entra a subcarpetes).
        // El Stream deixa la carpeta oberta: el try la tanca en acabar.
        System.out.println("== Files.list: primer nivell ==");
        try (Stream<Path> contingut = Files.list(dades)) {
            contingut.sorted()
                     .forEach(p -> System.out.println("  " + p.getFileName()));
        }

        // Files.walk: tot l'arbre, carpeta per carpeta, fins al final.
        // Ens quedem només amb els fitxers i els ordenem.
        System.out.println("== Files.walk: tots els fitxers ==");
        List<Path> fitxers;
        try (Stream<Path> arbre = Files.walk(dades)) {
            fitxers = arbre.filter(Files::isRegularFile)
                           .sorted()
                           .toList();
        }

        // Recorrem la llista i sumem les mides.
        long total = 0;
        for (Path f : fitxers) {
            long mida = Files.size(f);
            total += mida;
            System.out.printf("  %-42s %5d bytes%n", f, mida);
        }
        System.out.printf("Total: %d fitxers, %d bytes%n",
                fitxers.size(), total);
    }
}
