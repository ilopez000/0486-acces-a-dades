package cat.pratfp.gamevault.sessio5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Exemple 6: quant costa arribar a l'últim registre, seqüencialment i amb seek. */
public class Ex6SequencialVsAleatori {

    static final int N = 50_000;
    static final int MIDA_REGISTRE = Integer.BYTES + Double.BYTES;   // id + hores

    public static void main(String[] args) throws IOException {
        Path csv = Path.of("dades", "export", "gran.csv");
        Path bin = Path.of("dades", "export", "gran.dat");
        Files.createDirectories(csv.getParent());

        // 1. GENERAR 50.000 registres sintètics en els dos formats.
        try (BufferedWriter w = Files.newBufferedWriter(csv, StandardCharsets.UTF_8);
             RandomAccessFile raf = new RandomAccessFile(bin.toFile(), "rw")) {
            raf.setLength(0);
            for (int i = 0; i < N; i++) {
                w.write(i + ";Joc " + i + ";" + (i % 100) + ".0");
                w.newLine();
                raf.writeInt(i);
                raf.writeDouble(i % 100);
            }
        }

        // 2. SEQÜENCIAL: cal passar per les 49.999 línies anteriors.
        long t0 = System.nanoTime();
        String ultima = null;
        try (BufferedReader r = Files.newBufferedReader(csv, StandardCharsets.UTF_8)) {
            String l;
            while ((l = r.readLine()) != null) ultima = l;
        }
        long tSeq = System.nanoTime() - t0;

        // 3. ALEATORI: un sol salt calculat.
        t0 = System.nanoTime();
        int id;
        double hores;
        try (RandomAccessFile raf = new RandomAccessFile(bin.toFile(), "r")) {
            raf.seek((long) (N - 1) * MIDA_REGISTRE);
            id = raf.readInt();
            hores = raf.readDouble();
        }
        long tAle = System.nanoTime() - t0;

        System.out.println("Seqüencial: " + ultima + "  en " + tSeq / 1_000 + " µs");
        System.out.println("Aleatori:   " + id + ";" + hores + "  en " + tAle / 1_000 + " µs");
        System.out.println("El salt directe ha estat " + (tSeq / Math.max(tAle, 1)) + " vegades més ràpid");
    }
}
