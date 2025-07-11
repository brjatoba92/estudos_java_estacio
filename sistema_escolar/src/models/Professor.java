package models;

import java.util.ArrayList;
import java.util.List;

public class Professor {
    // Atributos
    private String nome;
    private String disciplina;
    private List<Turma> turmas;

    // Construtor
    public Professor(String nome, String disciplina) {
        this.nome = nome;
        this.disciplina = disciplina;
        this.turmas = new ArrayList<>();
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public List<Turma> getTurmas() {
        return turmas;
    }

    // Metodos
    public void adcionarTurma(Turma turma) {
        if (!turmas.contains(turma)) {
            turmas.add(turma);
            System.out.println("Turma " +  turma.getCodigo() + " atribuída ao professor " +  nome);
        }
    }

    public void listarTurmas() {
        System.out.println("Turmas do Professor " + nome + " (" + disciplina + "):")
        for (Turma turma : turmas) {
            System.out.println("- " + turma.getCodigo() + " - " + turma.getSerie())
        }
    }
}