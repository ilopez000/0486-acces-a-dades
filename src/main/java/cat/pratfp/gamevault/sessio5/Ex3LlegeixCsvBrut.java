package cat.pratfp.gamevault.sessio5;

import cat.pratfp.gamevault.model.Joc;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/** Exemple 3: llegir un CSV brut amb OpenCSV sense que el programa peti. */
public class Ex3LlegeixCsvBrut {

    static final String CSV_BRUT = """
            id;titol;plataforma;estudi;dataSortida;horesJugades;nota
            1;Hollow Knight;PC;Team Cherry;2017-02-24;42.5;9.5
            2;Celeste;PC;Maddy Makes Games;2018-01-25;15.0;12.0
            3;Gris;PC
            4;Hades;PC;Supergiant Games;25-09-2020;30.0;9.0
            5;"Baldur's Gate 3; Deluxe";PC;Larian Studios;2023-08-03;120.0;9.8
            6;Metroid Dread;Switch;MercurySteam;2021-10-08;-5.0;9.0
            """;

    public static void main(String[] args) throws IOException {
        Path brut = Path.of("dades", "import", "catalog_brut.csv");
        Files.createDirectories(brut.getParent());
        Files.writeString(brut, CSV_BRUT, StandardCharsets.UTF_8);

        List<Joc> valids = llegeix(brut);
        System.out.println("Jocs vàlids: " + valids.size());
        valids.forEach(j -> System.out.println("  " + j.id() + " · " + j.titol()));
    }

    static List<Joc> llegeix(Path p) throws IOException {
        List<Joc> jocs = new ArrayList<>();
        // El parser sap que el separador és ; i que les cometes protegen el text.
        CSVParser parser = new CSVParserBuilder().withSeparator(';').withQuoteChar('"').build();

        try (Reader r = Files.newBufferedReader(p, StandardCharsets.UTF_8);
             CSVReader csv = new CSVReaderBuilder(r).withCSVParser(parser).withSkipLines(1).build()) {

            String[] c;
            int linia = 1;
            while ((c = csv.readNext()) != null) {
                linia++;
                if (c.length < 7) {
                    System.out.println("Línia " + linia + " descartada: incompleta (" + c.length + " camps)");
                    continue;
                }
                try {
                    jocs.add(new Joc(Integer.parseInt(c[0].trim()), c[1].trim(), c[2].trim(), c[3].trim(),
                            LocalDate.parse(c[4].trim()), Double.parseDouble(c[5].trim()),
                            Double.parseDouble(c[6].trim())));
                } catch (DateTimeParseException e) {
                    System.out.println("Línia " + linia + " descartada: data mal formada '" + c[4] + "'");
                } catch (NumberFormatException e) {
                    System.out.println("Línia " + linia + " descartada: número invàlid");
                } catch (IllegalArgumentException e) {
                    System.out.println("Línia " + linia + " descartada: " + e.getMessage());
                }
            }
        } catch (CsvValidationException e) {
            throw new IOException("CSV mal format a " + p, e);
        }
        return jocs;
    }
}
