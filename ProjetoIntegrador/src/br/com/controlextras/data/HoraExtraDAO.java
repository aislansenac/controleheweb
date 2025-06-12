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
import java.util.HashMap;
import java.util.Map;

public class HoraExtraDAO {
    // Constantes para as queries SQL
    private static final String SQL_INSERIR_HE = 
        "INSERT INTO horas_extras(criador_id, data, hora_inicio, hora_fim, observacao) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_DELETAR_HE = 
        "DELETE FROM horas_extras WHERE id = ?";
    private static final String SQL_INSCREVER_HE = 
        "UPDATE horas_extras SET inscrito=? WHERE id = ?";
    private static final String SQL_APROVAR_HE = 
        "UPDATE horas_extras SET aprovada=?, verificada=? WHERE id = ?";
    private static final String SQL_BUSCAR_TODAS_HE = 
        "SELECT * FROM horas_extras";
    private static final String SQL_BUSCAR_HE_ID = 
        "SELECT * FROM horas_extras WHERE id=?";

    public void inserir(HoraExtra he) {
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERIR_HE)) {

            stmt.setInt(1, he.getCriador().getId());
            stmt.setDate(2, Date.valueOf(he.getData()));
            stmt.setTime(3, Time.valueOf(he.getHoraInicio()));
            stmt.setTime(4, Time.valueOf(he.getHoraFim()));
            stmt.setString(5, he.getObservacao());
            stmt.execute();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir hora extra: " + e.getMessage());
        }
    }

    public void deletar(int id) {
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETAR_HE)) {
            stmt.setInt(1, id);
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Erro ao deletar hora extra: " + e.getMessage());
        }
    }

    public void inscrever(int id) {
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSCREVER_HE)) {
            stmt.setBoolean(1, true);
            stmt.setInt(2, id);
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Erro ao inscrever hora extra: " + e.getMessage());
        }
    }

    public void aprovar(boolean resp, int idHe) {
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(SQL_APROVAR_HE)) {
            stmt.setBoolean(1, resp);
            stmt.setBoolean(2, true);
            stmt.setInt(3, idHe);
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Erro ao aprovar hora extra: " + e.getMessage());
        }
    }

    public Map<Integer, HoraExtra> getHoraExtra() {
        Map<Integer, HoraExtra> listaHoraExtra = new HashMap<>();
            
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(SQL_BUSCAR_TODAS_HE);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                HoraExtra he = new HoraExtra();
                he.setId(rs.getInt("id"));

                Funcionario func = new Funcionario();
                func.setId(rs.getInt("criador_id"));
                he.setCriador(func);

                he.setData(rs.getDate("data").toLocalDate());
                he.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                he.setHoraFim(rs.getTime("hora_fim").toLocalTime());
                he.setObservacao(rs.getString("observacao"));

                he.setAprovada(rs.getBoolean("aprovada"));
                he.setInscrito(rs.getBoolean("inscrito"));
                he.setVerificada(rs.getBoolean("verificada"));

                listaHoraExtra.put(he.getId(), he);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar lista de horas extras: " + e.getMessage());
        }

        return listaHoraExtra;
    }

    public HoraExtra getHoraExtra(int id) {
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(SQL_BUSCAR_HE_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    HoraExtra he = new HoraExtra();
                    he.setId(rs.getInt("id"));

                    Funcionario func = new Funcionario();
                    func.setId(rs.getInt("criador_id"));
                    he.setCriador(func);

                    he.setData(rs.getDate("data").toLocalDate());
                    he.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                    he.setHoraFim(rs.getTime("hora_fim").toLocalTime());
                    he.setObservacao(rs.getString("observacao"));

                    he.setVerificada(rs.getBoolean("verificada"));
                    he.setAprovada(rs.getBoolean("aprovada"));

                    return he;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar hora extra por ID: " + e.getMessage());
        }
        return null;
    }
}
