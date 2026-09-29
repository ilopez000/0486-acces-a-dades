package cat.pratfp.gamevault.sessio6;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

/** Exemple 3: el risc de desserialitzar qualsevol cosa, i com posar-hi un filtre. */
public class Ex3FiltreDeserialitzacio {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Path fitxer = Path.of("dades", "sospitos.ser");

        // 1. Algú ens envia un fitxer .ser. Diu que és una Biblioteca... però hi ha un HashMap.
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(fitxer))) {
            out.writeObject(new HashMap<String, String>());
        }

        // 2. SENSE FILTRE: readObject crea el que hi hagi dins, sigui el que sigui.
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(fitxer))) {
            Object o = in.readObject();
            System.out.println("Sense filtre s'ha creat un " + o.getClass().getName());
        }

        // 3. AMB FILTRE: només acceptem les classes del nostre paquet; la resta es rebutja.
        ObjectInputFilter filtre = ObjectInputFilter.Config.createFilter(
                "cat.pratfp.gamevault.**;java.util.ArrayList;java.lang.*;!*");
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(fitxer))) {
            in.setObjectInputFilter(filtre);
            in.readObject();
        } catch (InvalidClassException e) {
            System.out.println("Amb filtre: rebutjat -> " + e.getMessage());
        }
    }
}
