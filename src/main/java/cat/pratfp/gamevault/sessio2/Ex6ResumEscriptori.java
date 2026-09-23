package cat.pratfp.gamevault.sessio2;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Exemple 6: recorre TOT l'escriptori amb Files.walk i en fa un resum:
 * quants fitxers hi ha i quant ocupen en total.
 *
 * Continua l'exemple 5, però sobre una carpeta real i gran, no sobre "dades".
 */
public class Ex6ResumEscriptori {

    public static void main(String[] args) throws IOException {

        // 1) La carpeta d'inici.
        // System.getProperty("user.home") és la carpeta de l'usuari
        // (C:\Users\ignac a Windows, /home/usuari a Linux).
        // resolve("Desktop") hi afegeix la subcarpeta de l'escriptori.
        // Si el Windows està en català o castellà pot ser "Escriptori" o "Escritorio":
        // per això comprovem que existeixi abans de recórrer-la.
        Path escriptori = Path.of(System.getProperty("user.home")).resolve("Desktop");

        if (!Files.isDirectory(escriptori)) {
            System.out.println("No trobo la carpeta: " + escriptori);
            return;   // sortim del programa sense petar
        }

        System.out.println("Recorrent " + escriptori + " ...");

        // 2) Comptadors. Han de ser long, no int:
        // un escriptori pot passar fàcilment dels 2 GB i int només arriba a ~2.147 milions.
        long fitxers = 0;
        long carpetes = 0;
        long bytes = 0;

        // 3) Files.walk baixa per tot l'arbre: la carpeta, les seves subcarpetes,
        // les subcarpetes d'aquestes... fins al final.
        // Torna un Stream<Path> mandrós que va obrint carpetes a mesura que el recorrem,
        // per això va dins d'un try-with-resources que el tanca en acabar.
        try (Stream<Path> arbre = Files.walk(escriptori)) {

            // Convertim el Stream en un iterable per poder fer un for clàssic:
            // així podem sumar a tres variables i capturar errors fitxer a fitxer.
            for (Path p : (Iterable<Path>) arbre::iterator) {

                if (Files.isDirectory(p)) {
                    carpetes++;          // és una carpeta: només la comptem
                } else {
                    fitxers++;
                    bytes += midaSegura(p);   // és un fitxer: el comptem i sumem la mida
                }
            }
        } catch (UncheckedIOException e) {
            // Files.walk embolcalla els errors de lectura en un UncheckedIOException.
            // Passa, per exemple, si troba una carpeta sense permisos o un enllaç trencat.
            System.out.println("El recorregut s'ha aturat: " + e.getMessage());
        }

        // 4) Resultat.
        System.out.println();
        System.out.println("Fitxers:  " + fitxers);
        System.out.println("Carpetes: " + carpetes);
        System.out.println("Ocupen:   " + bytes + " bytes  (" + llegible(bytes) + ")");
    }

    /**
     * Torna la mida d'un fitxer en bytes, o 0 si no es pot llegir.
     * Files.size llança IOException i dins d'un bucle no ens interessa
     * que un sol fitxer protegit aturi tot el recompte.
     */
    private static long midaSegura(Path fitxer) {
        try {
            return Files.size(fitxer);
        } catch (IOException e) {
            System.out.println("  (no puc llegir " + fitxer.getFileName() + ")");
            return 0;
        }
    }

    /** Passa els bytes a KB, MB o GB amb un decimal, perquè es puguin llegir. */
    private static String llegible(long bytes) {
        String[] unitats = { "bytes", "KB", "MB", "GB", "TB" };
        double valor = bytes;
        int i = 0;
        while (valor >= 1024 && i < unitats.length - 1) {
            valor /= 1024;
            i++;
        }
        return String.format("%.1f %s", valor, unitats[i]);
    }
}
