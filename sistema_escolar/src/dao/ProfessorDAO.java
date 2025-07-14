package dao;

import models.Professor;
import util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO {
    public void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS professores ("
           + "matricula TEXT PRIMARY KEY,"
           + "nome TEXT NOT NULL,"
           + "disciplina TEXT,"
           + "valor_hora REAL DEFAULT 0.0"
           + ");";
        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela de professores: " + e.getMessage());
        }
    }

    public void inserir(Professor professor) {
        String sql = "INSERT INTO professores (matricula, nome, disciplina, valor_hora) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, professor.getMatricula());
            pstmt.setString(2, professor.getNome());
            pstmt.setString(3, professor.getDisciplina());
            pstmt.setDouble(4, professor.getValorHora());
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
                Professor p = new Professor(
                    rs.getString("nome"),
                    rs.getString("matricula"),
                    rs.getString("disciplina"),
                    rs.getDouble("valor_hora")
                );
                professores.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar professores: " + e.getMessage());
        }
        return professores;
    }
} 