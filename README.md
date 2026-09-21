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
| 2 | `sessio2` | Path i Files: explora el sistema de fitxers | `Ex1Rutes` · `Ex2Informacio` · `Ex3CreaCarpetes` · `Ex4CopiaMouEsborra` · `Ex5Recorre` |

## Estructura

```
0486-acces-a-dades/
├── pom.xml
├── dades/
│   └── catalog.csv        ← dades de partida del catàleg
└── src/main/java/cat/pratfp/gamevault/
    └── sessio2/           ← un paquet per sessió
```

Les carpetes `dades/import`, `dades/export` i `dades/backup` les creen els exemples
en executar-se i no es pugen al repositori.

---
Docent: Ignasi López Aylagas · PratFP
