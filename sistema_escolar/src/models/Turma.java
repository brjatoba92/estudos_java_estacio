package models;

import java.util.ArrayList;
import java.util.List;

public class Turma {
    // Atributos
    private String codigo; //Ex: 1A, 3B, ...
    private String serie; //Ex: 1o Ano, 3o Ano
    private Professor professorResponsavel;
    private List<Aluno> alunos;

    // Construtor
    public Turma(String codigo, String serie, Professor professorResponsavel) {
        this.codigo = codigo;
        this.serie = serie;
        this.professorResponsavel = professorResponsavel;
        this.alunos = new ArrayList<>();
    }

    // Getters e Setters
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

    // Métodos
    public void adcionarAluno(Aluno aluno) {
        if(!alunos.contains(aluno)) {
            alunos.add(aluno);
            aluno.setTurma(this); // vincula a turma ao aluno também
            System.out.println("Aluno " + aluno.getNome() + " adcionado à turma " + codigo)
        } else {
            System.out.println("Aluno já está na turma. ")
        }
    }
    public void listarAlunos() {
        System.out.println("Alunos da Turma " + codigo + " - " + serie);
        for (Aluno aluno : alunos) {
            System.out.println("- " + aluno.getNome() + " (Matrícula: " + aluno.getMatricula() + ")");
        }
    }
}