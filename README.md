# MP0486 Accés a dades · DAM2 · PratFP

Exemples de classe del mòdul **0486 Accés a dades** (CFGS DAM, 2n curs).
Tots formen part del projecte **GameVault**, un gestor de catàleg de videojocs que
canvia de manera de guardar les dades a cada bloc del curs.

## Com fer-ho servir

1. Clona el repositori:
   ```
   git clone https://github.com/ilopez000/0486-acces-a-dades.git
   ```
2. Obre la carpeta amb **IntelliJ IDEA** (és un projecte Maven, Java 21).
3. Obre la classe que vulguis i prem el triangle verd ▶ al costat del `main`.

La carpeta `dades/` ha de quedar a l'arrel del projecte, al costat del `pom.xml`:
els exemples hi treballen amb rutes relatives.

## Exemples per sessió

Cada sessió té el seu paquet dins de `src/main/java/cat/pratfp/gamevault/`.

| Sessió | Paquet | Tema | Classes |
|---|---|---|---|
| 2 | `sessio2` | Path i Files: explora el sistema de fitxers | `Ex1Rutes` · `Ex2Informacio` · `Ex3CreaCarpetes` · `Ex4CopiaMouEsborra` · `Ex5Recorre` · `Ex6ResumEscriptori` |
| 5 | `sessio5` | Fluxos, CSV amb OpenCSV i accés aleatori | `Ex1FluxosDeText` · `Ex2FluxosDeBytes` · `Ex3LlegeixCsvBrut` · `Ex4EscriuCsv` · `Ex5AccesAleatori` · `Ex6SequencialVsAleatori` |
| 6 | `sessio6` | Serialització d'objectes i JSON amb Jackson | `Biblioteca` · `Ex1Serialitza` · `Ex2VersioIncompatible` · `Ex3FiltreDeserialitzacio` · `Ex4JsonAmbJackson` · `Ex5JsonSenseModel` |
| 11-12 | `sql/` | Instal·lació de MariaDB i DBeaver · repàs de SQL | `00_usuari_dam2.sql` · `01_gamevault_proves.sql` · `02_institut.sql` |
| 13 | `sessio13` | Consultar i modificar MariaDB des de Java amb JDBC | `BaseDeDades` · `Ex1ProvaConnexio` · `Ex2LlistaJocs` · `Ex3JocsPerGenere` · `Ex4Valoracions` · `Ex5AltaJugador` · `Ex6RebaixaPreus` · `Ex7EsborraJugador` · `Ex8SqliteEncastat` |

## Estructura

```
0486-acces-a-dades/
├── pom.xml
├── dades/
│   └── catalog.csv        ← dades de partida del catàleg
├── sql/                   ← scripts de les bases de dades de proves (sessions 11-13)
└── src/main/
    ├── java/cat/pratfp/gamevault/
    │   ├── model/         ← el record Joc, compartit per totes les sessions
    │   ├── sessio2/       ← un paquet per sessió
    │   ├── sessio5/
    │   ├── sessio6/
    │   └── sessio13/
    └── resources/
        └── db.properties.example   ← copia'l com a db.properties
```

## Bases de dades de proves (sessions 11 a 13)

1. Amb MariaDB instal·lat, executa com a **root** `sql/00_usuari_dam2.sql` (crea l'usuari `dam2`).
2. Executa `sql/01_gamevault_proves.sql` i `sql/02_institut.sql`. Es poden tornar a executar
   sempre que vulguis deixar les dades com al principi.
3. Per als exemples de Java de la sessió 13, copia `src/main/resources/db.properties.example`
   com a `db.properties` i posa-hi la contrasenya de `dam2`. Aquest fitxer **no es puja mai**:
   és al `.gitignore`.

Les carpetes `dades/import`, `dades/export` i `dades/backup`, i els fitxers `.ser`, `.log` i
`.dat`, els creen els exemples en executar-se i no es pugen al repositori.

Les dependències (OpenCSV, Jackson i els drivers JDBC de MariaDB i SQLite) es declaren al
`pom.xml`; IntelliJ les baixa sol la primera vegada que obres el projecte.

---
Docent: Ignasi López Aylagas · PratFP
