package dao;

import models.Professor;
import util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Map;

public class ProfessorDAO {
    public void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS professores ("
           + "matricula TEXT PRIMARY KEY,"
           + "nome TEXT NOT NULL,"
           + "disciplinas TEXT,"
           + "valor_hora REAL DEFAULT 0.0,"
           + "total_turmas INTEGER DEFAULT 0,"
           + "horas_trabalhadas_mes INTEGER DEFAULT 0,"
           + "valor_total REAL DEFAULT 0.0,"
           + "horas_por_disciplina_mes TEXT"
           + ");";
        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela de professores: " + e.getMessage());
        }
    }

    public void inserir(Professor professor) {
        String sql = "INSERT INTO professores (matricula, nome, disciplinas, valor_hora, total_turmas, horas_trabalhadas_mes, valor_total, horas_por_disciplina_mes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, professor.getMatricula());
            pstmt.setString(2, professor.getNome());
            pstmt.setString(3, String.join(",", professor.getDisciplinas()));
            pstmt.setDouble(4, professor.getValorHora());
            pstmt.setInt(5, professor.getTotalTurmas());
            pstmt.setInt(6, professor.getHorasTrabalhadasMes());
            pstmt.setDouble(7, professor.getValorTotal());
            pstmt.setString(8, new com.google.gson.Gson().toJson(professor.getHorasPorDisciplinaMes()));
            pstmt.executeUpdate();
            System.out.println("✅ Professor salvo no banco de dados.");
        } catch (SQLException e) {
            System.out.println("Erro ao inserir professor: " + e.getMessage());
        }
    }

    public List<Professor> listarTodos() {
        List<Professor> professores = new ArrayList<>();
        String sql = "SELECT * FROM professores";
        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String disciplinasStr = rs.getString("disciplinas");
                List<String> disciplinas = disciplinasStr != null && !disciplinasStr.isEmpty() ? Arrays.asList(disciplinasStr.split(",")) : new ArrayList<>();
                Professor p = new Professor(
                    rs.getString("nome"),
                    rs.getString("matricula"),
                    disciplinas,
                    rs.getDouble("valor_hora")
                );
                p.setTotalTurmas(rs.getInt("total_turmas"));
                p.setHorasTrabalhadasMes(rs.getInt("horas_trabalhadas_mes"));
                p.setValorTotal(rs.getDouble("valor_total"));
                String horasDiscJson = rs.getString("horas_por_disciplina_mes");
                if (horasDiscJson != null && !horasDiscJson.isEmpty()) {
                    java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Map<String, Integer>>(){}.getType();
                    Map<String, Integer> map = new com.google.gson.Gson().fromJson(horasDiscJson, type);
                    p.setHorasPorDisciplinaMes(map);
                }
                professores.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar professores: " + e.getMessage());
        }
        return professores;
    }

    public void atualizar(String matricula, String novoNome, List<String> novasDisciplinas, double novoValorHora, int novoTotalTurmas, double novoValorTotal, int novasHorasTrabalhadasMes, Map<String, Integer> novasHorasPorDisciplinaMes) {
        String sql = "UPDATE professores SET nome = ?, disciplinas = ?, valor_hora = ?, total_turmas = ?, valor_total = ?, horas_trabalhadas_mes = ?, horas_por_disciplina_mes = ? WHERE matricula = ?";
        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, novoNome);
            pstmt.setString(2, String.join(",", novasDisciplinas));
            pstmt.setDouble(3, novoValorHora);
            pstmt.setInt(4, novoTotalTurmas);
            pstmt.setDouble(5, novoValorTotal);
            pstmt.setInt(6, novasHorasTrabalhadasMes);
            pstmt.setString(7, new com.google.gson.Gson().toJson(novasHorasPorDisciplinaMes));
            pstmt.setString(8, matricula);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Professor atualizado no banco de dados.");
            } else {
                System.out.println("⚠️ Professor não encontrado no banco de dados.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar professor: " + e.getMessage());
        }
    }

    public void remover(String matricula) {
        String sql = "DELETE FROM professores WHERE matricula = ?";
        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, matricula);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Professor removido do banco de dados.");
            } else {
                System.out.println("⚠️ Professor não encontrado no banco de dados.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao remover professor: " + e.getMessage());
        }
    }
} 