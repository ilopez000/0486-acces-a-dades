package cat.pratfp.gamevault.transaccions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;

/** T2: una comanda de tres jocs. Si un falla, només es desfà aquell. */
public class T2ComandaAmbSavepoint {

    // {joc, plataforma}: Stardew Valley PC, Gris mòbil, Celeste PC
    static final int[][] COMANDA = {{19, 1}, {15, 7}, {12, 1}};

    public static void main(String[] args) throws SQLException {
        int jugador = 7;                                   // cozygamer
        try (Connection con = Connexio.obre()) {
            con.setAutoCommit(false);
            int fetes = 0;
            for (int[] v : COMANDA) {
                Savepoint sp = con.setSavepoint();         // punt de retorn
                try {
                    afegeixLinia(con, jugador, v[0], v[1]);
                    fetes++;
                    System.out.println("Joc " + v[0] + ": afegit");
                } catch (SQLException e) {
                    con.rollback(sp);                      // només aquest
                    System.out.println("Joc " + v[0] + ": descartat ("
                            + e.getMessage() + ")");
                }
            }
            con.commit();                                  // la resta, sí
            System.out.println("Comanda tancada amb " + fetes
                    + " jocs. Compres de cozygamer: " + compres(con, 7));
        }
    }

    static void afegeixLinia(Connection con, int jugador, int joc, int plat)
            throws SQLException {
        try (PreparedStatement treu = con.prepareStatement(
                     "UPDATE joc_plataforma SET estoc = estoc - 1 "
                     + "WHERE joc_id = ? AND plataforma_id = ? "
                     + "AND estoc > 0");
             PreparedStatement afegeix = con.prepareStatement(
                     "INSERT INTO compra (jugador_id, joc_id, "
                     + "plataforma_id, data, preu_pagat) "
                     + "SELECT ?, joc_id, plataforma_id, CURDATE(), preu "
                     + "FROM joc_plataforma "
                     + "WHERE joc_id = ? AND plataforma_id = ?")) {
            treu.setInt(1, joc);
            treu.setInt(2, plat);
            if (treu.executeUpdate() == 0) {
                throw new SQLException("sense estoc");
            }
            afegeix.setInt(1, jugador);
            afegeix.setInt(2, joc);
            afegeix.setInt(3, plat);
            afegeix.executeUpdate();
        }
    }

    static int compres(Connection con, int jugador) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT COUNT(*) FROM compra WHERE jugador_id = ?")) {
            ps.setInt(1, jugador);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}
