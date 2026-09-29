package cat.pratfp.gamevault.sessio6;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/** Exemple 1: desar un objecte sencer amb ObjectOutputStream i recuperar-lo amb ObjectInputStream. */
public class Ex1Serialitza {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Path fitxer = Path.of("dades", "biblioteca.ser");

        // 1. CREAR L'OBJECTE en memòria.
        Biblioteca original = new Biblioteca("Ignasi");
        original.afegeix("Hollow Knight", 42.5);
        original.afegeix("Gris", 4.0);
        System.out.println("Abans:   " + original);

        // 2. SERIALITZAR: l'objecte i tot el que penja d'ell (la llista) es converteixen en bytes.
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(fitxer))) {
            out.writeObject(original);
        }
        System.out.println("Desat a " + fitxer + " (" + Files.size(fitxer) + " bytes)");

        // 3. DESSERIALITZAR: es reconstrueix un objecte nou a partir dels bytes.
        Biblioteca recuperada;
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(fitxer))) {
            recuperada = (Biblioteca) in.readObject();
        }
        System.out.println("Després: " + recuperada);
        System.out.println("És el mateix objecte? " + (original == recuperada));
    }
}
