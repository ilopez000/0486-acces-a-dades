package cat.pratfp.gamevault.javapasapas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/** Pas 12: dues sentències que s'han de fer totes dues o cap. */
public class Ex12CompraAmbTransaccio {

    static final String TREU_ESTOC = """
            UPDATE joc_plataforma SET estoc = estoc - 1
            WHERE joc_id = ? AND plataforma_id = ? AND estoc > 0
            """;

    static final String REGISTRA_COMPRA = """
            INSERT INTO compra (jugador_id, joc_id, plataforma_id,
                                data, preu_pagat)
            SELECT ?, joc_id, plataforma_id, CURDATE(), preu
            FROM joc_plataforma
            WHERE joc_id = ? AND plataforma_id = ?
            """;

    public static void main(String[] args) throws SQLException {
        try (Connection con = Connexio.obre()) {
            compra(con, 12, 14, 1);   // newbie26 compra Hades II per a PC
            compra(con, 12, 15, 7);   // newbie26 vol Gris per a mòbil
        }
    }

    static void compra(Connection con, int jugador, int joc, int plataforma)
            throws SQLException {
        con.setAutoCommit(false);
        try (PreparedStatement estoc = con.prepareStatement(TREU_ESTOC);
             PreparedStatement registre =
                     con.prepareStatement(REGISTRA_COMPRA)) {

            estoc.setInt(1, joc);
            estoc.setInt(2, plataforma);
            if (estoc.executeUpdate() == 0) {
                con.rollback();
                System.out.println("Joc " + joc + ": sense estoc, "
                        + "compra anul·lada");
                return;
            }

            registre.setInt(1, jugador);
            registre.setInt(2, joc);
            registre.setInt(3, plataforma);
            registre.executeUpdate();

            con.commit();
            System.out.println("Joc " + joc + ": compra feta");
        } catch (SQLException e) {
            con.rollback();
            System.out.println("Error, es desfà tot: " + e.getMessage());
        } finally {
            con.setAutoCommit(true);
        }
    }
}
