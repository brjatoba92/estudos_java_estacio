package models;

import java.util.ArrayList;
import java.util.List;

public class Escola {
    private String nome;
    private String cnpj;

    private List<Turma> turmas;
    private List<Professor> professores;
    private List<Aluno> alunos;

    public Escola(String nome, String cnpj) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.turmas = new ArrayList<>();
        this.professores = new ArrayList<>();
        this.alunos = new ArrayList<>();
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public List<Turma> getTurmas() {
        return turmas;
    }

    public List<Professor> getProfessores() {
        return professores;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    // Metodos de cadastro
    public void adicionarTurma(Turma turma) {
        turmas.add(turma);
    }

    public void adicionarProfessor(Professor professor) {
        professores.add(professor);
    }

    public void adicionarAluno(Aluno aluno) {
        alunos.add(aluno);
    }

    public void listarTurmas() {
        System.out.println("Turmas da Escola" + nome + ":");
        for (Turma turma : turmas) {
            System.out.println("- " + turma.getCodigo() + " (" + turma.getSerie() + ")");
        }
    }

    public void listarProfessores() {
        System.out.println("Professores da Escola" + nome + ":");
        for (Professor professor : professores) {
            System.out.println("- " + professor.getNome() + " (" + professor.getDisciplina() + ")");
        }
    }

    public void listarAlunos() {
        System.out.println("Alunos da Escola" + nome + ":");
        for (Aluno aluno : alunos) {
            System.out.println("- " + aluno.getNome() + " (Matrícula: " + aluno.getMatricula() + ")");
        }
    }

    public Aluno buscarAlunoPorMatricula(String matricula) {
        for (Aluno aluno : alunos) {
            if (aluno.getMatricula().equals(matricula)) {
                return aluno;
            }
        }
        return null;
    }
}