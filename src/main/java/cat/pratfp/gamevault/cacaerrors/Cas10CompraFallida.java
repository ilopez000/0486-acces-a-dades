package cat.pratfp.gamevault.cacaerrors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Cas10CompraFallida {

    public static void main(String[] args) throws SQLException {
        try (Connection con = Connexio.obre()) {
            System.out.println("Estoc abans: " + estoc(con));
            try (PreparedStatement treu = con.prepareStatement(
                         "UPDATE joc_plataforma SET estoc = estoc - 1 "
                         + "WHERE joc_id = 13 AND plataforma_id = 1");
                 PreparedStatement afegeix = con.prepareStatement(
                         "INSERT INTO compra (jugador_id, joc_id, "
                         + "plataforma_id, data, preu_pagat) "
                         + "VALUES (99, 13, 1, CURDATE(), 24.50)")) {
                treu.executeUpdate();
                afegeix.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error, es desfà tot: " + e.getMessage());
            }
            System.out.println("Estoc després: " + estoc(con));
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
