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
import java.util.HashMap;
import java.util.Map;

public class FuncionarioDAO {

    public void inserir(Funcionario funcionario, String senha) {
        String senhaCriptografada = Criptografia.getMD5(senha);
        String sql = "INSERT INTO funcionarios(nome, data_nascimento, cpf, salario, status, senha, cargo_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getNome());
            stmt.setDate(2, Date.valueOf(funcionario.getDataNascimento()));
            stmt.setString(3, funcionario.getCpf());
            stmt.setDouble(4, funcionario.getSalario());
            stmt.setBoolean(5, funcionario.isAtivo());
            stmt.setString(6, senhaCriptografada);
            stmt.setInt(7, funcionario.getCargo().getId());
            stmt.execute();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir funcionario: " + e.getMessage());
        }
    }

    public void editar(Funcionario funcionario, String novaSenha) {
        String sql;
        boolean alterarSenha = (novaSenha != null && !novaSenha.isEmpty());

        if (alterarSenha) {
            sql = "UPDATE funcionarios SET nome=?, data_nascimento=?, salario=?, status=?, senha=?, cargo_id=? WHERE cpf=?";
        } else {
            sql = "UPDATE funcionarios SET nome=?, data_nascimento=?, salario=?, status=?, cargo_id=? WHERE cpf=?";
        }

        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getNome());
            stmt.setDate(2, Date.valueOf(funcionario.getDataNascimento()));
            stmt.setDouble(3, funcionario.getSalario());
            stmt.setBoolean(4, funcionario.isAtivo());

            int index = 5;
            if (alterarSenha) {
                String senhaCriptografada = Criptografia.getMD5(novaSenha);
                stmt.setString(5, senhaCriptografada);
                index++;
            }

            stmt.setInt(index, funcionario.getCargo().getId());
            stmt.setString(index + 1, funcionario.getCpf());
            stmt.execute();

        } catch (SQLException e) {
            System.out.println("Erro ao editar funcionario: " + e.getMessage());
        }
    }

    public Map<String, Funcionario> getFuncionarios() {
        String sql = "SELECT * FROM funcionarios";
        Map<String, Funcionario> listaEmpresa = new HashMap<>();

        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setId(rs.getInt("id"));
                funcionario.setNome(rs.getString("nome"));
                funcionario.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                funcionario.setCpf(rs.getString("cpf"));
                funcionario.setSalario(rs.getDouble("salario"));
                funcionario.setAtivo(rs.getBoolean("status"));
                Cargo cargo = new Cargo();
                cargo.setId(rs.getInt("cargo_id"));
                funcionario.setCargo(cargo);

                listaEmpresa.put(funcionario.getCpf(), funcionario);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar funcionarios: " + e.getMessage());
        }

        return listaEmpresa;
    }

    public Funcionario getFuncionario(int id) {
        String sql = "SELECT * FROM funcionarios WHERE id=?";
        Funcionario funcionario = null;

        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    funcionario = new Funcionario();
                    funcionario.setId(rs.getInt("id"));
                    funcionario.setNome(rs.getString("nome"));
                    funcionario.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                    funcionario.setCpf(rs.getString("cpf"));
                    funcionario.setSalario(rs.getDouble("salario"));
                    funcionario.setAtivo(rs.getBoolean("status"));
                    Cargo cargo = new Cargo();
                    cargo.setId(rs.getInt("cargo_id"));
                    funcionario.setCargo(cargo);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar funcionario: " + e.getMessage());
        }

        return funcionario;
    }

    public String buscarHashSenha(String cpf) {
        String sql = "SELECT senha FROM funcionarios WHERE cpf=?";

        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("senha");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar senha: " + e.getMessage());
        }

        return null;
    }

}
