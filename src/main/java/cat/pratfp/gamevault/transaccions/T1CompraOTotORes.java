package cat.pratfp.gamevault.transaccions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** T1: una compra són dues sentències que s'han de fer totes dues o cap. */
public class T1CompraOTotORes {

    static final String TREU_ESTOC = """
            UPDATE joc_plataforma SET estoc = estoc - 1
            WHERE joc_id = ? AND plataforma_id = ? AND estoc > 0
            """;
    static final String AFEGEIX_COMPRA = """
            INSERT INTO compra (jugador_id, joc_id, plataforma_id,
                                data, preu_pagat)
            VALUES (?, ?, ?, CURDATE(), ?)
            """;

    public static void main(String[] args) throws SQLException {
        try (Connection con = Connexio.obre()) {
            System.out.println("Estoc d'Hades per a PC: " + estoc(con));
            compra(con, 3, 13, 1);    // laia_gg: existeix
            System.out.println("Estoc d'Hades per a PC: " + estoc(con));
            compra(con, 99, 13, 1);   // el jugador 99 no existeix
            System.out.println("Estoc d'Hades per a PC: " + estoc(con));
        }
    }

    static void compra(Connection con, int jugador, int joc, int plat)
            throws SQLException {
        con.setAutoCommit(false);              // comença la transacció
        try (PreparedStatement treu = con.prepareStatement(TREU_ESTOC);
             PreparedStatement afegeix =
                     con.prepareStatement(AFEGEIX_COMPRA)) {
            treu.setInt(1, joc);
            treu.setInt(2, plat);
            if (treu.executeUpdate() == 0) {
                throw new SQLException("No queda estoc");
            }
            afegeix.setInt(1, jugador);
            afegeix.setInt(2, joc);
            afegeix.setInt(3, plat);
            afegeix.setBigDecimal(4, new java.math.BigDecimal("24.50"));
            afegeix.executeUpdate();
            con.commit();                      // tot bé: es desa tot
            System.out.println("  Compra del jugador " + jugador
                    + ": FETA (commit)");
        } catch (SQLException e) {
            con.rollback();                    // alguna cosa ha fallat
            System.out.println("  Compra del jugador " + jugador
                    + ": DESFETA (rollback) -> " + e.getMessage());
        } finally {
            con.setAutoCommit(true);           // la deixem com estava
        }
    }

    static int estoc(Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                     "SELECT estoc FROM joc_plataforma "
                     + "WHERE joc_id = 13 AND plataforma_id = 1");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }
}
