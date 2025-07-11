package models;

import java.util.ArrayList;
import java.util.List;

public class Turma {
    private String codigo;
    private String serie;
    private Professor professorResponsavel;
    private List<Aluno> alunos;

    public Turma(String codigo, String serie, Professor professorResponsavel) {
        this.codigo = codigo;
        this.serie = serie;
        this.professorResponsavel = professorResponsavel;
        this.alunos = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getSerie() {
        return serie;
    }

    public Professor getProfessorResponsavel() {
        return professorResponsavel;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void adicionarAluno(Aluno aluno) {
        if (!alunos.contains(aluno)) {
            alunos.add(aluno);
            aluno.setTurma(this);
            System.out.println("Aluno " + aluno.getNome() + " adicionado à turma " + codigo);
        } else {
            System.out.println("Aluno já está na turma.");
        }
    }

    public void listarAlunos() {
        System.out.println("Alunos da Turma " + codigo + " - " + serie);
        for (Aluno aluno : alunos) {
            System.out.println("- " + aluno.getNome() + " (Matrícula: " + aluno.getMatricula() + ")");
        }
    }
}
