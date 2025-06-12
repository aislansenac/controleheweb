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

    public void inserir(FuncHoraExtra funcHe) {
        String sql = "INSERT INTO funcionarios_horaextras(funcionario_id, horaextra_id) VALUES (?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, funcHe.getFuncId());
            stmt.setInt(2, funcHe.getHeId());
            stmt.execute();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir Funcionario_HoraExtra: " + e.getMessage());
        }
    }

    public List<FuncHoraExtra> getFuncHoraExtraIdFunc(int idFunc) {
        String sql = "SELECT * FROM funcionarios_horaextras WHERE funcionario_id=?";
        List<FuncHoraExtra> listaHe = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFunc);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FuncHoraExtra funcHe = new FuncHoraExtra();
                    funcHe.setId(rs.getInt("id"));
                    funcHe.setHeId(rs.getInt("horaextra_id"));
                    funcHe.setFuncId(idFunc);
                    listaHe.add(funcHe);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar Funcionario_HoraExtra por funcionario_id: " + e.getMessage());
        }

        return listaHe;
    }

    public FuncHoraExtra getFuncHoraExtraIdHoraExtra(int idHe) {
        String sql = "SELECT * FROM funcionarios_horaextras WHERE horaextra_id=?";
        FuncHoraExtra funcHe = null;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idHe);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    funcHe = new FuncHoraExtra();
                    funcHe.setId(rs.getInt("id"));
                    funcHe.setHeId(rs.getInt("horaextra_id"));
                    funcHe.setFuncId(rs.getInt("funcionario_id"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar Funcionario_HoraExtra por horaextra_id: " + e.getMessage());
        }

        return funcHe;
    }

    public List<FuncHoraExtra> getFuncHoraExtra() {
        String sql = "SELECT * FROM funcionarios_horaextras";
        List<FuncHoraExtra> listaHe = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                FuncHoraExtra funcHe = new FuncHoraExtra();
                funcHe.setId(rs.getInt("id"));
                funcHe.setFuncId(rs.getInt("funcionario_id"));
                funcHe.setHeId(rs.getInt("horaextra_id"));
                listaHe.add(funcHe);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar Funcionarios_HoraExtras: " + e.getMessage());
        }

        return listaHe;
    }

    public FuncHoraExtra getFuncHoraExtra(int id) {
        String sql = "SELECT * FROM funcionarios_horaextras WHERE id=?";
        FuncHoraExtra funcHe = null;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    funcHe = new FuncHoraExtra();
                    funcHe.setId(rs.getInt("id"));
                    funcHe.setFuncId(rs.getInt("funcionario_id"));
                    funcHe.setHeId(rs.getInt("horaextra_id"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar Funcionario_HoraExtra por id: " + e.getMessage());
        }

        return funcHe;
    }
}
