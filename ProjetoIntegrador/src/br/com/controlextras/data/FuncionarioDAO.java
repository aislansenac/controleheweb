package br.com.controlextras.data;

import br.com.controlextras.conexao.Conexao;
import br.com.controlextras.funcao.Criptografia;
import br.com.controlextras.modelo.Cargo;
import br.com.controlextras.modelo.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class FuncionarioDAO {
    private Connection conn;
    private Conexao conexao;

    public FuncionarioDAO() {
        this.conexao = new Conexao();
        this.conn = conexao.getConexao();
    }
    
    public void inserir(Funcionario funcionario) {
        funcionario.setSenha(Criptografia.getMD5(funcionario.getSenha()));
        String sql = "INSERT INTO funcionarios(nome, data_nascimento, cpf, salario, status, senha, cargo_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, funcionario.getNome());
            Date sqlDate = Date.valueOf(funcionario.getDataNascimento());
            stmt.setDate(2, sqlDate);
            stmt.setString(3, funcionario.getCpf());
            stmt.setDouble(4, funcionario.getSalario());
            stmt.setBoolean(5, funcionario.isAtivo());
            stmt.setString(6, funcionario.getSenha());
            stmt.setInt(7, funcionario.getCargo().getId());
            stmt.execute();
        }catch(SQLException e){
            System.out.println("Erro ao inserir funcionario: " + e.getMessage());
        }
    }
    public void editar(Funcionario funcionario) {
        funcionario.setSenha(Criptografia.getMD5(funcionario.getSenha()));
        String sql = "UPDATE funcionarios SET nome=?, data_nascimento=?, salario=?, status=?, senha=?, cargo_id=? WHERE cpf=?";
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, funcionario.getNome());
            Date sqlDate = Date.valueOf(funcionario.getDataNascimento());
            stmt.setDate(2, sqlDate);
            stmt.setDouble(3, funcionario.getSalario());
            stmt.setBoolean(4, funcionario.isAtivo());
            stmt.setString(5, funcionario.getSenha());
            stmt.setInt(6, funcionario.getCargo().getId());
            stmt.setString(7, funcionario.getCpf());
            stmt.execute();
        }catch(SQLException e){
            System.out.println("Erro ao editar funcionario: " + e.getMessage());
        }
    }
    
    public Map<String, Funcionario> getFuncionario() {
        String sql = "SELECT * FROM funcionarios";

        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();            
            
            Map<String, Funcionario> listaEmpresa = new HashMap<>();

            while (rs.next()) { 
                Funcionario funcionario = new Funcionario();
                funcionario.setId(rs.getInt("id"));
                funcionario.setNome(rs.getString("nome"));
                LocalDate localDate = rs.getDate("data_nascimento").toLocalDate();
                funcionario.setDataNascimento(localDate);
                funcionario.setCpf(rs.getString("cpf"));
                funcionario.setSalario(rs.getDouble("salario"));
                funcionario.setAtivo(rs.getBoolean("status"));
                funcionario.setSenha(rs.getString("senha"));
                Cargo cargo = new Cargo();
                cargo.setId(rs.getInt("cargo_id"));
                funcionario.setCargo(cargo);
                
                listaEmpresa.put(funcionario.getCpf(), funcionario);
            }
            return listaEmpresa;
                    
        } catch (SQLException e) {
            return null;
        }
    }
    
    public Funcionario getFuncionario(int id) {
        String sql = "SELECT * FROM funcionarios WHERE id=?";

        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();            
            
            Funcionario funcionario = new Funcionario();
            while (rs.next()) { 
                funcionario = new Funcionario();
                funcionario.setId(rs.getInt("id"));
                funcionario.setNome(rs.getString("nome"));
                LocalDate localDate = rs.getDate("data_nascimento").toLocalDate();
                funcionario.setDataNascimento(localDate);
                funcionario.setCpf(rs.getString("cpf"));
                funcionario.setSalario(rs.getDouble("salario"));
                funcionario.setAtivo(rs.getBoolean("status"));
                funcionario.setSenha(rs.getString("senha"));
                Cargo cargo = new Cargo();
                cargo.setId(rs.getInt("cargo_id"));
                funcionario.setCargo(cargo);
            }
            return funcionario;
                    
        } catch (SQLException e) {
            return null;
        }
    }
}
