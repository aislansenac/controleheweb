package br.com.controlextras.data;

import br.com.controlextras.conexao.Conexao;
import br.com.controlextras.modelo.FuncHoraExtra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class FuncHoraExtraDAO {
    private Connection conn;
    private Conexao conexao;

    
    public FuncHoraExtraDAO() {
        this.conexao = new Conexao();
        this.conn = conexao.getConexao();
    }
    
    public void inserir(FuncHoraExtra funcHe) {
        String sql = "INSERT INTO funcionarios_horaextras(funcionario_id, horaextra_id) VALUES (?, ?)";
        
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, funcHe.getFuncId());
            stmt.setInt(2, funcHe.getHeId());
            stmt.execute();
        }catch(SQLException e){
            System.out.println("Erro ao inserir Funcionario_HoraExtra: " + e.getMessage());
        }
    }
    
    public List<FuncHoraExtra> getFuncHoraExtraIdFunc(int idFunc) {
        String sql = "SELECT * FROM funcionarios_horaextras WHERE funcionario_id=?";
        
        List<FuncHoraExtra> listaHe = new ArrayList<>();
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idFunc);
            ResultSet rs = stmt.executeQuery();
                
            while(rs.next()) {
                FuncHoraExtra funcHe = new FuncHoraExtra();
                
                funcHe.setId(rs.getInt("id"));
                funcHe.setHeId(rs.getInt("horaextra_id"));
                funcHe.setFuncId(idFunc);

                listaHe.add(funcHe);
            }
            return listaHe;
        }catch(SQLException e){
            System.out.println("Erro ao inserir Funcionario_HoraExtra: " + e.getMessage());
            return null;
        }
    }
    
    public FuncHoraExtra getFuncHoraExtraIdHoraExtra(int idHe) {
        String sql = "SELECT * FROM funcionarios_horaextras WHERE horaextra_id=?";
        
        FuncHoraExtra funcHe = new FuncHoraExtra();
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idHe);
            ResultSet rs = stmt.executeQuery();
                
            if(rs.next()) {

                funcHe.setId(rs.getInt("id"));
                funcHe.setHeId(rs.getInt("horaextra_id"));
                funcHe.setFuncId(rs.getInt("funcionario_id"));
            }
            return funcHe;
        }catch(SQLException e){
            System.out.println("Erro ao pegar Func pelo id Funcionario_HoraExtra: " + e.getMessage());
            return null;
        }
    }
    
    public List<FuncHoraExtra> getFuncHoraExtra() {
        String sql = "SELECT * FROM funcionarios_horaextras";
        
        List<FuncHoraExtra> listaHe = new ArrayList<>();
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                FuncHoraExtra funcHe = new FuncHoraExtra();
                funcHe.setFuncId(rs.getInt("funcionario_id"));
                funcHe.setId(rs.getInt("id"));
                funcHe.setHeId(rs.getInt("horaextra_id"));
                listaHe.add(funcHe);
            }
            return listaHe;
        }catch(SQLException e){
            System.out.println("Erro ao inserir Funcionario_HoraExtra: " + e.getMessage());
            return null;
        }
    }
    
    public FuncHoraExtra getFuncHoraExtra(int id) {
        String sql = "SELECT * FROM funcionarios_horaextras WHERE id=?";
       
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            FuncHoraExtra funcHe = new FuncHoraExtra();
            if(rs.next()) {
                
                funcHe.setFuncId(rs.getInt("funcionario_id"));
                funcHe.setId(rs.getInt("id"));
                funcHe.setHeId(rs.getInt("horaextra_id"));
            }
            return funcHe;
        }catch(SQLException e){
            System.out.println("Erro ao inserir Funcionario_HoraExtra: " + e.getMessage());
            return null;
        }
    }

}
