package models;

import java.util.ArrayList;
import java.util.List;

public class Professor {
    private String nome;
    private String disciplina;
    private String matricula;
    private List<Turma> turmas;

    public Professor(String nome, String disciplina, String matricula) {
        this.nome = nome;
        this.disciplina = disciplina;
        this.matricula = matricula;
        this.turmas = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public List<Turma> getTurmas() {
        return turmas;
    }

    public String getMatricula() {
        return this.matricula;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void adicionarTurma(Turma turma) {
        if (!turmas.contains(turma)) {
            turmas.add(turma);
            System.out.println("Turma " + turma.getCodigo() + " atribuída ao professor " + nome);
        }
    }

    public void listarTurmas() {
        System.out.println("Turmas do Professor " + nome + " (" + disciplina + "):");
        for (Turma turma : turmas) {
            System.out.println("- " + turma.getCodigo() + " - " + turma.getSerie());
        }
    }
}
