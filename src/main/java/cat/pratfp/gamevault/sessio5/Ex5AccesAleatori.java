package cat.pratfp.gamevault.sessio5;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

/** Exemple 5: registres de mida fixa amb RandomAccessFile i seek. */
public class Ex5AccesAleatori {

    // Cada registre: id (4 bytes) + títol de 40 chars (80 bytes) + hores (8 bytes) = 92 bytes.
    static final int LONG_TITOL = 40;
    static final int MIDA_REGISTRE = Integer.BYTES + LONG_TITOL * Character.BYTES + Double.BYTES;

    public static void main(String[] args) throws IOException {
        Path fitxer = Path.of("dades", "export", "hores.dat");
        Files.createDirectories(fitxer.getParent());
        Files.deleteIfExists(fitxer);

        String[] titols = {"Hollow Knight", "Blasphemous 2", "Metroid Dread", "Gris", "Rise of the Third Power"};
        double[] hores = {42.5, 18.0, 12.5, 4.0, 22.0};

        // 1. ESCRIURE 5 REGISTRES, un darrere l'altre.
        try (RandomAccessFile raf = new RandomAccessFile(fitxer.toFile(), "rw")) {
            for (int i = 0; i < titols.length; i++) {
                escriu(raf, i, i + 1, titols[i], hores[i]);
            }
        }
        mostra(fitxer, "Abans");

        // 2. ACTUALITZAR NOMÉS LES HORES DEL REGISTRE 2: seek directe al camp.
        try (RandomAccessFile raf = new RandomAccessFile(fitxer.toFile(), "rw")) {
            long posicioHores = 2L * MIDA_REGISTRE + Integer.BYTES + LONG_TITOL * Character.BYTES;
            raf.seek(posicioHores);
            raf.writeDouble(12.5 + 3.0);   // hem jugat 3 hores més
        }
        mostra(fitxer, "Després");
    }

    static void escriu(RandomAccessFile raf, int posicio, int id, String titol, double hores) throws IOException {
        raf.seek((long) posicio * MIDA_REGISTRE);
        raf.writeInt(id);
        // El títol s'omple (o es talla) fins a 40 caràcters perquè tots els registres pesin igual.
        String fix = String.format("%-" + LONG_TITOL + "s", titol).substring(0, LONG_TITOL);
        raf.writeChars(fix);
        raf.writeDouble(hores);
    }

    static void mostra(Path fitxer, String etiqueta) throws IOException {
        System.out.println("--- " + etiqueta + " (" + Files.size(fitxer) / MIDA_REGISTRE + " registres) ---");
        try (RandomAccessFile raf = new RandomAccessFile(fitxer.toFile(), "r")) {
            long registres = raf.length() / MIDA_REGISTRE;
            for (int i = 0; i < registres; i++) {
                raf.seek((long) i * MIDA_REGISTRE);
                int id = raf.readInt();
                StringBuilder titol = new StringBuilder();
                for (int c = 0; c < LONG_TITOL; c++) titol.append(raf.readChar());
                double h = raf.readDouble();
                System.out.printf("%d | %-25s | %5.1f h%n", id, titol.toString().strip(), h);
            }
        }
    }
}
