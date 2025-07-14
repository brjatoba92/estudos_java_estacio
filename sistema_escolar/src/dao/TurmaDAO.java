package dao;

import models.Turma;
import util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TurmaDAO {
    public void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS turmas ("
           + "codigo TEXT PRIMARY KEY,"
           + "serie TEXT,"
           + "matricula_professor TEXT,"
           + "carga_horaria INTEGER DEFAULT 0"
           + ");";
        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela de turmas: " + e.getMessage());
        }
    }

    public void inserir(Turma turma) {
        String sql = "INSERT INTO turmas (codigo, serie, matricula_professor, carga_horaria) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, turma.getCodigo());
            pstmt.setString(2, turma.getSerie());
            pstmt.setString(3, turma.getMatriculaProfessor());
            pstmt.setInt(4, turma.getCargaHoraria());
            pstmt.executeUpdate();
            System.out.println("✅ Turma salva no banco de dados.");
        } catch (SQLException e) {
            System.out.println("Erro ao inserir turma: " + e.getMessage());
        }
    }

    public List<Turma> listarTodos() {
        List<Turma> turmas = new ArrayList<>();
        String sql = "SELECT * FROM turmas";
        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Turma t = new Turma(
                    rs.getString("codigo"),
                    rs.getString("serie"),
                    null, // Professor será setado depois se necessário
                    rs.getInt("carga_horaria")
                );
                turmas.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar turmas: " + e.getMessage());
        }
        return turmas;
    }
} 