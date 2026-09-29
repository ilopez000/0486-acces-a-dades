package cat.pratfp.gamevault.sessio6;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

/** Exemple 2: què passa quan la classe canvia després d'haver desat el fitxer. */
public class Ex2VersioIncompatible {

    /** Versió 1 de la classe, tal com es va desar «l'any passat». */
    static class PerfilV1 implements Serializable {
        private static final long serialVersionUID = 1L;
        String nom = "Ignasi";
    }

    /** Versió 2: hem afegit un camp i hem canviat el número de versió. */
    static class PerfilV2 implements Serializable {
        private static final long serialVersionUID = 2L;
        String nom = "Ignasi";
        int nivell = 7;
    }

    public static void main(String[] args) throws IOException {
        Path fitxer = Path.of("dades", "perfil.ser");

        // 1. Desem amb la versió 1...
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(fitxer))) {
            out.writeObject(new PerfilV1());
        }

        // 2. ...i fem trampa: reescrivim el nom de la classe dins del fitxer perquè apunti a la V2.
        byte[] bytes = Files.readAllBytes(fitxer);
        String contingut = new String(bytes, java.nio.charset.StandardCharsets.ISO_8859_1);
        contingut = contingut.replace("PerfilV1", "PerfilV2");
        Files.write(fitxer, contingut.getBytes(java.nio.charset.StandardCharsets.ISO_8859_1));

        // 3. Intentem llegir-lo amb la classe nova.
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(fitxer))) {
            Object o = in.readObject();
            System.out.println("Llegit: " + o);
        } catch (InvalidClassException e) {
            System.out.println("InvalidClassException: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Classe desconeguda: " + e.getMessage());
        }
    }
}
