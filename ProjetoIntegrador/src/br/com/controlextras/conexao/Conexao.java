package br.com.controlextras.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String URL = "jdbc:mysql://localhost/pi_bd_aislan_penha";
    private static final String USER = "root";
    private static final String PASSWORD = "ap100267";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // carrega driver uma vez só
        } catch (ClassNotFoundException ex) {
            System.err.println("Driver não encontrado: " + ex.getMessage());
        }
    }

    public static Connection getConexao() {
    try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }catch(SQLException e) {
            return null;
        }
    }
}
