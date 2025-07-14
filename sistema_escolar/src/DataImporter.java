import java.sql.*;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;

public class DataImporter {
    private static final String DB_URL = "jdbc:sqlite:bin/escola.db";
    private static final Gson gson = new Gson();
    
    public static void main(String[] args) {
        try {
            // Importar dados
            importarDisciplinas();
            importarProfessores();
            importarAlunos();
            importarTurmas();
            
            System.out.println("✅ Todos os dados foram importados com sucesso!");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao importar dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void importarDisciplinas() throws Exception {
        System.out.println("📚 Importando disciplinas...");
        
        // Ler arquivo JSON
        Type listType = new TypeToken<List<Disciplina>>(){}.getType();
        List<Disciplina> disciplinas = gson.fromJson(new FileReader("bin/disciplinas.json"), listType);
        
        // Inserir no banco
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "INSERT OR REPLACE INTO disciplinas (codigo, nome, carga_horaria) VALUES (?, ?, ?)";
            
            for (Disciplina disciplina : disciplinas) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, disciplina.getCodigoCadastro());
                    pstmt.setString(2, disciplina.getNomeCadastro());
                    pstmt.setInt(3, disciplina.getCargaHoraria());
                    pstmt.executeUpdate();
                }
            }
        }
        
        System.out.println("   ✅ " + disciplinas.size() + " disciplinas importadas");
    }
    
    private static void importarProfessores() throws Exception {
        System.out.println("👨‍🏫 Importando professores...");
        
        // Ler arquivo JSON
        Type listType = new TypeToken<List<Professor>>(){}.getType();
        List<Professor> professores = gson.fromJson(new FileReader("bin/professores.json"), listType);
        
        // Inserir no banco
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "INSERT OR REPLACE INTO professores (matricula, nome, disciplina, valor_hora) VALUES (?, ?, ?, ?)";
            
            for (Professor professor : professores) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, professor.getMatricula());
                    pstmt.setString(2, professor.getNome());
                    pstmt.setString(3, professor.getDisciplina());
                    pstmt.setDouble(4, professor.getValorHora());
                    pstmt.executeUpdate();
                }
            }
        }
        
        System.out.println("   ✅ " + professores.size() + " professores importados");
    }
    
    private static void importarAlunos() throws Exception {
        System.out.println("👨‍🎓 Importando alunos...");
        
        // Ler arquivo JSON
        Type listType = new TypeToken<List<Aluno>>(){}.getType();
        List<Aluno> alunos = gson.fromJson(new FileReader("bin/alunos.json"), listType);
        
        // Inserir no banco
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "INSERT OR REPLACE INTO alunos (matricula, nome, data_nascimento) VALUES (?, ?, ?)";
            
            for (Aluno aluno : alunos) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, aluno.getMatricula());
                    pstmt.setString(2, aluno.getNome());
                    pstmt.setString(3, aluno.getDataNascimento());
                    pstmt.executeUpdate();
                }
            }
        }
        
        System.out.println("   ✅ " + alunos.size() + " alunos importados");
    }
    
    private static void importarTurmas() throws Exception {
        System.out.println("🏫 Importando turmas...");
        
        // Ler arquivo JSON
        Type listType = new TypeToken<List<Turma>>(){}.getType();
        List<Turma> turmas = gson.fromJson(new FileReader("bin/turmas.json"), listType);
        
        // Inserir no banco
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "INSERT OR REPLACE INTO turmas (codigo, serie, matricula_professor, carga_horaria) VALUES (?, ?, ?, ?)";
            
            for (Turma turma : turmas) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, turma.getCodigo());
                    pstmt.setString(2, turma.getSerie());
                    pstmt.setString(3, turma.getMatriculaProfessor());
                    pstmt.setInt(4, turma.getCargaHoraria());
                    pstmt.executeUpdate();
                }
            }
        }
        
        System.out.println("   ✅ " + turmas.size() + " turmas importadas");
    }
    
    // Classes auxiliares para deserialização JSON
    static class Disciplina {
        private String codigoCadastro;
        private String nomeCadastro;
        private int cargaHoraria;
        
        public String getCodigoCadastro() { return codigoCadastro; }
        public String getNomeCadastro() { return nomeCadastro; }
        public int getCargaHoraria() { return cargaHoraria; }
    }
    
    static class Professor {
        private String nome;
        private String disciplina;
        private String matricula;
        private double valorHora;
        
        public String getNome() { return nome; }
        public String getDisciplina() { return disciplina; }
        public String getMatricula() { return matricula; }
        public double getValorHora() { return valorHora; }
    }
    
    static class Aluno {
        private String nome;
        private String matricula;
        private String dataNascimento;
        
        public String getNome() { return nome; }
        public String getMatricula() { return matricula; }
        public String getDataNascimento() { return dataNascimento; }
    }
    
    static class Turma {
        private String codigo;
        private String serie;
        private String matriculaProfessor;
        private int cargaHoraria;
        
        public String getCodigo() { return codigo; }
        public String getSerie() { return serie; }
        public String getMatriculaProfessor() { return matriculaProfessor; }
        public int getCargaHoraria() { return cargaHoraria; }
    }
} 