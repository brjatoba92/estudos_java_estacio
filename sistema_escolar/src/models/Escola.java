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
        System.out.println("Turmas:");
        for (Turma turma : turmas) {
            System.out.println("- " + turma.getCodigo() + " (" + turma.getSerie() + ")");
        }
    }

    public void listarProfessores() {
        System.out.println("Professores:");
        for (Professor prof : professores) {
            System.out.println("- " + prof.getNome() + " | " + prof.getDisciplina());
        }
    }

    public void listarAlunos() {
        System.out.println("Alunos:");
        for (Aluno aluno : alunos) {
            System.out.println("- " + aluno.getNome() + " | Matrícula: " + aluno.getMatricula());
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