package cat.pratfp.gamevault.sessio6;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Exemple 5: recórrer un JSON amb JsonNode, sense cap classe Java al darrere. */
public class Ex5JsonSenseModel {

    static final String RESPOSTA_API = """
            {
              "botiga": "GameVault Store",
              "jocs": [
                {"titol": "Hollow Knight", "preu": 14.99, "etiquetes": ["metroidvania", "indie"]},
                {"titol": "Celeste", "preu": 19.99, "etiquetes": ["plataformes"]},
                {"titol": "Gris", "preu": 16.99, "oferta": true}
              ]
            }
            """;

    public static void main(String[] args) throws IOException {
        Path fitxer = Path.of("dades", "import", "botiga.json");
        Files.createDirectories(fitxer.getParent());
        Files.writeString(fitxer, RESPOSTA_API, StandardCharsets.UTF_8);

        // 1. readTree converteix el text en un arbre de nodes.
        JsonNode arrel = new ObjectMapper().readTree(fitxer.toFile());
        System.out.println("Botiga: " + arrel.get("botiga").asText());

        // 2. Recorrem l'array "jocs" node a node.
        double total = 0;
        for (JsonNode joc : arrel.get("jocs")) {
            String titol = joc.get("titol").asText();
            double preu = joc.get("preu").asDouble();
            // path() no peta si la clau no existeix: retorna un node "missing".
            boolean oferta = joc.path("oferta").asBoolean(false);
            int etiquetes = joc.path("etiquetes").size();
            System.out.printf("%-15s %6.2f € %s (%d etiquetes)%n", titol, preu, oferta ? "OFERTA" : "", etiquetes);
            total += preu;
        }
        System.out.printf("Total: %.2f €%n", total);
    }
}
