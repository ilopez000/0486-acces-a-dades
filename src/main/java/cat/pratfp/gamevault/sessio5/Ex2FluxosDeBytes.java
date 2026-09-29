package cat.pratfp.gamevault.sessio5;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/** Exemple 2: fluxos de bytes: copiar qualsevol fitxer i guardar dades binàries. */
public class Ex2FluxosDeBytes {

    private static final String CAPCALERA = "GAMEVAULT-STATS-v1";

    public static void main(String[] args) throws IOException {
        Path origen = Path.of("dades", "catalog.csv");
        Path copia = Path.of("dades", "export", "catalog.copia");
        Files.createDirectories(copia.getParent());

        // 1. COPIAR BYTE A BYTE (amb memòria intermèdia): serveix per a text, imatges, zips...
        byte[] buffer = new byte[8 * 1024];
        long total = 0;
        try (InputStream in = new BufferedInputStream(Files.newInputStream(origen));
             OutputStream out = new BufferedOutputStream(Files.newOutputStream(copia))) {
            int llegits;
            while ((llegits = in.read(buffer)) != -1) {
                out.write(buffer, 0, llegits);
                total += llegits;
            }
        }
        System.out.println("Copiats " + total + " bytes -> " + copia);

        // 2. ESCRIURE TIPUS PRIMITIUS: DataOutputStream guarda int i double en binari.
        Path stats = Path.of("dades", "export", "stats.bin");
        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(Files.newOutputStream(stats)))) {
            out.writeUTF(CAPCALERA);   // una marca per reconèixer el fitxer
            out.writeInt(5);           // total de jocs
            out.writeDouble(99.0);     // hores totals
        }
        System.out.println("stats.bin ocupa " + Files.size(stats) + " bytes");

        // 3. LLEGIR-LOS EN EL MATEIX ORDRE i validar la capçalera.
        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(Files.newInputStream(stats)))) {
            String marca = in.readUTF();
            if (!marca.equals(CAPCALERA)) {
                throw new IOException("Això no és un fitxer d'estadístiques: " + marca);
            }
            System.out.println("Jocs: " + in.readInt() + " · Hores: " + in.readDouble());
        }
    }
}
