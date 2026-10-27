package cat.pratfp.gamevault.javapasapas;

import java.math.BigDecimal;

/** Una versió d'un joc: el joc en una plataforma concreta. */
public record Versio(String titol, String plataforma,
                     BigDecimal preu, int estoc) { }
