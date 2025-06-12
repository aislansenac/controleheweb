
package br.com.controlextras.data;

import br.com.controlextras.conexao.Conexao;
import br.com.controlextras.modelo.Cargo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;


public class CargoDAO {
    private Connection conn;
    private Conexao conexao;

    public CargoDAO() {
        this.conexao = new Conexao();
        this.conn = conexao.getConexao();
    }
    
    public List<Cargo> getCargo() {
        String sql = "SELECT * FROM cargos";
        List<Cargo> lista = new ArrayList<>();
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();            

            while (rs.next()) {
                Cargo cargo = new Cargo();
                
                cargo.setId(rs.getInt("id"));
                cargo.setTipo(rs.getString("tipo"));

                lista.add(cargo);    
            }
            return lista;
                    
        } catch (SQLException e) {
            System.out.println("Erro a listar cargo getCargo(): " + e.getMessage());
            return null;
        }
    }
    
    public Cargo getCargo(int id) {
        String sql = "SELECT * FROM cargos WHERE id=?";
        Cargo cargo = new Cargo();
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();            
            
            if(rs.next()) {
                cargo.setId(rs.getInt("id"));
                cargo.setTipo(rs.getString("tipo"));
    
            }
            return cargo;
                    
        } catch (SQLException e) {
            System.out.println("Erro a listar cargo getCargo(int id)" + e.getMessage());
            return null;
        }
    }
}
