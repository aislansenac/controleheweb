package br.com.controlextras.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private Connection con;
    
    public Connection getConexao() {
     try{
         Class.forName("com.mysql.cj.jdbc.Driver"); 
         con = DriverManager.getConnection("jdbc:mysql://localhost/pi_bd_aislan_penha", "root", "ap100267");
         return con;
     }catch(ClassNotFoundException | SQLException ex){
         System.out.println(ex.getMessage());
         return null;
     }
    }
}
