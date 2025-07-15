package dao;

import models.Aluno;
import util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    public void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS alunos ("
           + "matricula TEXT PRIMARY KEY,"
           + "nome TEXT NOT NULL,"
           + "data_nascimento TEXT"
           + ");";
        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela: " + e.getMessage());
        }
    }

    public void inserir(Aluno aluno) {
        String sql = "INSERT INTO alunos (matricula, nome, data_nascimento) VALUES (?, ?, ?)";
        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, aluno.getMatricula());
            pstmt.setString(2, aluno.getNome());
            pstmt.setString(3, aluno.getDataNascimento());
            pstmt.executeUpdate();
            System.out.println("✅ Aluno salvo no banco de dados.");
        } catch (SQLException e) {
            System.out.println("Erro ao inserir aluno: " + e.getMessage());
        }
    }

    public List<Aluno> listarTodos() {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM alunos";
        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                alunos.add(new Aluno(
                    rs.getString("nome"),
                    rs.getString("matricula"),
                    rs.getString("data_nascimento")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar alunos: " + e.getMessage());
        }
        return alunos;
    }

    public void atualizar(String matriculaAntiga, String novaMatricula, String novoNome, String novaDataNascimento) {
        String sql = "UPDATE alunos SET matricula = ?, nome = ?, data_nascimento = ? WHERE matricula = ?";
        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, novaMatricula);
            pstmt.setString(2, novoNome);
            pstmt.setString(3, novaDataNascimento);
            pstmt.setString(4, matriculaAntiga);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("\u2705 Aluno atualizado no banco de dados.");
            } else {
                System.out.println("\u26A0 Aluno não encontrado no banco de dados.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar aluno: " + e.getMessage());
        }
    }

    public void remover(String matricula) {
        String sql = "DELETE FROM alunos WHERE matricula = ?";
        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, matricula);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Aluno removido do banco de dados.");
            } else {
                System.out.println("⚠️ Aluno não encontrado no banco de dados.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao remover aluno: " + e.getMessage());
        }
    }
}
