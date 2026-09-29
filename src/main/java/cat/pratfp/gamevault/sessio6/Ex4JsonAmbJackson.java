package cat.pratfp.gamevault.sessio6;

import cat.pratfp.gamevault.model.Joc;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

/** Exemple 4: escriure i llegir una llista de Joc en JSON amb Jackson. */
public class Ex4JsonAmbJackson {

    public static void main(String[] args) throws IOException {
        Path json = Path.of("dades", "export", "jocs.json");
        Files.createDirectories(json.getParent());

        // 1. L'ObjectMapper és la peça central. El configurem un sol cop.
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())                      // entén LocalDate
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)    // "2017-02-24" i no [2017,2,24]
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // tolera camps de més

        List<Joc> jocs = List.of(
                new Joc(1, "Hollow Knight", "PC", "Team Cherry", LocalDate.of(2017, 2, 24), 42.5, 9.5),
                new Joc(4, "Gris", "PC", "Nomada Studio", LocalDate.of(2018, 12, 13), 4.0, 8.0));

        // 2. ESCRIURE: de la llista d'objectes a text JSON indentat.
        mapper.writerWithDefaultPrettyPrinter().writeValue(json.toFile(), jocs);
        System.out.println(Files.readString(json, StandardCharsets.UTF_8));

        // 3. LLEGIR: del text JSON a una List<Joc>. TypeReference conserva el tipus de la llista.
        List<Joc> llegits = mapper.readValue(json.toFile(), new TypeReference<List<Joc>>() {});
        System.out.println("Llegits " + llegits.size() + " jocs. El primer: " + llegits.get(0).titol()
                + " (" + llegits.get(0).dataSortida().getYear() + ")");
        System.out.println("Iguals als originals? " + llegits.equals(jocs));
    }
}
