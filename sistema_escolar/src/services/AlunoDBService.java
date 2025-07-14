package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.DatabaseUtil;

public class AlunoDBService {
    public static void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS alunos ("
                   + "matricula TEXT PRIMARY KEY,"
                   + "nome TEXT NOT NULL,"
                   + "dataNascimento TEXT NOT NULL"
                   + ");";
        
        try(Connection conn = DatabaseUtil.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.execute();
                System.out.println("Tabela de alunos criada/verificada com sucesso!");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void inserirAluno(String matricula, String nome, String dataNascimento) {
        String sql = "INSERT INTO alunos (matricula, nome, dataNascimento) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, matricula);
            stmt.setString(2, nome);
            stmt.setString(3, dataNascimento);
            stmt.executeUpdate();
            System.out.println("Aluno inserido com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void listarAlunos() {
        String sql = "SELECT * FROM alunos";
        try (Connection conn = DatabaseUtil.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                System.out.println(rs.getString("matricula") + " | " + rs.getString("nome") + " | " + rs.getString("dataNascimento"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}