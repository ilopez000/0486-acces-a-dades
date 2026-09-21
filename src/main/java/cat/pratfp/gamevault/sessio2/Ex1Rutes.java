package cat.pratfp.gamevault.sessio2;

import java.nio.file.Files;
import java.nio.file.Path;

/** Exemple 1: un Path és una adreça, no el fitxer. */
public class Ex1Rutes {

    public static void main(String[] args) {
        // Ruta construïda per trams: Java hi posa el separador
        // correcte (\ a Windows, / a Linux).
        Path cataleg = Path.of("dades", "catalog.csv");

        // Tal com l'hem escrita: relativa a la carpeta del projecte.
        System.out.println("Ruta relativa : " + cataleg);

        // La mateixa ruta, completa des de l'arrel del disc.
        System.out.println("Ruta absoluta : " + cataleg.toAbsolutePath());

        // L'últim tram de la ruta: el nom del fitxer.
        System.out.println("Nom           : " + cataleg.getFileName());

        // Tot menys l'últim tram: la carpeta que el conté.
        System.out.println("Carpeta pare  : " + cataleg.getParent());

        // resolve() enganxa un tram nou al final d'una ruta.
        Path copia = Path.of("dades").resolve("backup")
                                     .resolve("catalog.csv");
        System.out.println("Ruta nova     : " + copia);

        // Crear un Path no toca el disc. Per saber si el fitxer
        // hi és, cal preguntar-ho a Files.
        System.out.println("Existeix el catàleg? " + Files.exists(cataleg));
        System.out.println("Existeix la còpia?   " + Files.exists(copia));
    }
}
