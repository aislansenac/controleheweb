package br.com.controlextras.data;

import br.com.controlextras.conexao.Conexao;
import br.com.controlextras.modelo.Funcionario;
import br.com.controlextras.modelo.HoraExtra;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import java.time.LocalTime;

import java.util.HashMap;
import java.util.Map;

public class HoraExtraDAO {
    private Connection conn;
    private Conexao conexao;

    public HoraExtraDAO() {
        this.conexao = new Conexao();
        this.conn = conexao.getConexao();
    }
    
    public void inserir(HoraExtra he) {
        String sql = "INSERT INTO horas_extras(criador_id, data, hora_inicio, hora_fim, observacao) VALUE (?, ?, ?, ?, ?)";

        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, he.getCriador().getId());
            Date sqlDate = Date.valueOf(he.getData());
            stmt.setDate(2, sqlDate);
            Time sqlTimeInicio = Time.valueOf(he.getHoraInicio());
            stmt.setTime(3, sqlTimeInicio);
            Time sqlTimeTermino = Time.valueOf(he.getHoraFim());
            stmt.setTime(4, sqlTimeTermino);
            stmt.setString(5, he.getObservacao());
            stmt.execute();
        }catch(SQLException e){
            System.out.println("Erro ao inserir hora extra: " + e.getMessage());
        }
    }
    
    public void deletar(int id) {
        String sql = "DELETE FROM horas_extras WHERE id = ?";
        
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            stmt.execute();
        }catch(SQLException e){
            System.out.println("Erro ao deletar hora extra: " + e.getMessage());
        }
    }
    
    public void inscrever(int id) {
        String sql = "UPDATE horas_extras SET inscrito=? WHERE id = ?";
        
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, true);
            stmt.setInt(2, id);
            
            stmt.execute();
        }catch(SQLException e){
            System.out.println("Erro ao inscrever hora extra: " + e.getMessage());
        }
    }
    
    public void aprovar(boolean resp, int idHe){
        String sql = "UPDATE horas_extras SET aprovada=?, verificada=? WHERE id = ?";
        
        try{
            PreparedStatement stmt = conn.prepareStatement(sql); 
            stmt.setBoolean(1, resp);
            stmt.setBoolean(2, true);
            stmt.setInt(3, idHe);
            stmt.execute();
        }catch(SQLException e){
            System.out.println("Erro ao aprovar hora extra: " + e.getMessage());
        }
    }
    public Map<Integer, HoraExtra> getHoraExtra() {
        String sql = "SELECT * FROM horas_extras";
        Map<Integer, HoraExtra> listaHoraExtra = new HashMap<>();
        
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();            
            
            while (rs.next()) { 
                HoraExtra he = new HoraExtra();
                he.setId(rs.getInt("id"));
                Funcionario func = new Funcionario();
                func.setId(rs.getInt("criador_id"));
                he.setCriador(func);
                
                LocalDate localDate = rs.getDate("data").toLocalDate();
                he.setData(localDate);
                
                LocalTime localTimeInicio = rs.getTime("hora_inicio").toLocalTime();
                he.setHoraInicio(localTimeInicio);
                
                LocalTime localTimeFim = rs.getTime("hora_fim").toLocalTime();
                he.setHoraFim(localTimeFim);
                he.setObservacao(rs.getString("observacao"));
                
                he.setAprovada(rs.getBoolean("aprovada"));
                he.setInscrito(rs.getBoolean("inscrito"));
                he.setVerificada(rs.getBoolean("verificada"));
                listaHoraExtra.put(he.getId(), he);
            }
            return listaHoraExtra;
                    
        } catch (SQLException e) {
            return null;
        }
    }
    
    public HoraExtra getHoraExtra(int id) {
        String sql = "SELECT * FROM horas_extras WHERE id=?";
        HoraExtra he = new HoraExtra();
        
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();            
            
            if(rs.next()) { 
                he.setId(rs.getInt("id"));
                Funcionario func = new Funcionario();
                func.setId(rs.getInt("criador_id"));
                he.setCriador(func);
                
                LocalDate localDate = rs.getDate("data").toLocalDate();
                he.setData(localDate);
                
                LocalTime localTimeInicio = rs.getTime("hora_inicio").toLocalTime();
                he.setHoraInicio(localTimeInicio);
                
                LocalTime localTimeFim = rs.getTime("hora_fim").toLocalTime();
                he.setHoraFim(localTimeFim);
                he.setObservacao(rs.getString("observacao"));
                he.setVerificada(rs.getBoolean("verificada"));
                he.setAprovada(rs.getBoolean("aprovada"));
            }
            return he;
                    
        } catch (SQLException e) {
            return null;
        }
    }
    
}
