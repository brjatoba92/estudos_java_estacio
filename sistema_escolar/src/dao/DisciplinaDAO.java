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
           + "carga_horaria INTEGER,"
           + "ementa TEXT"
           + ");";
        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela de disciplinas: " + e.getMessage());
        }
    }

    public void inserir(Disciplina disciplina) {
        String sql = "INSERT INTO disciplinas (codigo, nome, carga_horaria, ementa) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, disciplina.getCodigo());
            pstmt.setString(2, disciplina.getNome());
            pstmt.setInt(3, disciplina.getCargaHoraria());
            pstmt.setString(4, disciplina.getEmenta());
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
                    rs.getInt("carga_horaria"),
                    rs.getString("ementa")
                );
                disciplinas.add(d);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar disciplinas: " + e.getMessage());
        }
        return disciplinas;
    }

    public void atualizar(String codigo, String novoNome, int novaCargaHoraria, String novaEmenta) {
        String sql = "UPDATE disciplinas SET nome = ?, carga_horaria = ?, ementa = ? WHERE codigo = ?";
        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, novoNome);
            pstmt.setInt(2, novaCargaHoraria);
            pstmt.setString(3, novaEmenta);
            pstmt.setString(4, codigo);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Disciplina atualizada no banco de dados.");
            } else {
                System.out.println("⚠️ Disciplina não encontrada no banco de dados.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar disciplina: " + e.getMessage());
        }
    }
} 