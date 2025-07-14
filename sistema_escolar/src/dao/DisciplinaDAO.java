package dao;

import models.Disciplina;
import util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaDAO {
    public void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS disciplinas ("
           + "codigo TEXT PRIMARY KEY,"
           + "nome TEXT NOT NULL,"
           + "carga_horaria INTEGER"
           + ");";
        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela de disciplinas: " + e.getMessage());
        }
    }

    public void inserir(Disciplina disciplina) {
        String sql = "INSERT INTO disciplinas (codigo, nome, carga_horaria) VALUES (?, ?, ?)";
        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, disciplina.getCodigo());
            pstmt.setString(2, disciplina.getNome());
            pstmt.setInt(3, disciplina.getCargaHoraria());
            pstmt.executeUpdate();
            System.out.println("✅ Disciplina salva no banco de dados.");
        } catch (SQLException e) {
            System.out.println("Erro ao inserir disciplina: " + e.getMessage());
        }
    }

    public List<Disciplina> listarTodos() {
        List<Disciplina> disciplinas = new ArrayList<>();
        String sql = "SELECT * FROM disciplinas";
        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Disciplina d = new Disciplina(
                    rs.getString("codigo"),
                    rs.getString("nome"),
                    rs.getInt("carga_horaria")
                );
                disciplinas.add(d);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar disciplinas: " + e.getMessage());
        }
        return disciplinas;
    }
} 