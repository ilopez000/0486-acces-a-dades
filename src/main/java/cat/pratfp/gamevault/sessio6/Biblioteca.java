package cat.pratfp.gamevault.sessio6;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** La biblioteca d'un jugador. Es pot serialitzar: es converteix en bytes i es recupera sencera. */
public class Biblioteca implements Serializable {

    // Número de versió de la classe. Si canvia, els fitxers antics deixen de ser compatibles.
    private static final long serialVersionUID = 1L;

    private final String propietari;
    private final List<String> titols = new ArrayList<>();
    // transient: aquest camp NO es guarda al fitxer. Es recalcula quan cal.
    private transient double horesCalculades;

    public Biblioteca(String propietari) {
        this.propietari = propietari;
    }

    public void afegeix(String titol, double hores) {
        titols.add(titol);
        horesCalculades += hores;
    }

    public String getPropietari() { return propietari; }
    public List<String> getTitols() { return titols; }
    public double getHoresCalculades() { return horesCalculades; }

    @Override
    public String toString() {
        return propietari + " té " + titols + " (" + horesCalculades + " h)";
    }
}
