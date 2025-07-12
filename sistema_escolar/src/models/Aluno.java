package models;

import java.util.ArrayList;
import java.util.List;

public class Aluno {
    private String nome;
    private String matricula;
    private String dataNascimento;
    private transient Turma turma;
    private List<Nota> notas;

    public Aluno(String nome, String matricula, String dataNascimento) {
        this.nome = nome;
        this.matricula = matricula;
        this.dataNascimento = dataNascimento;
        this.notas = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public List<Nota> getNotas() {
        return notas;
    }

    public void setNome(String nome) {
    this.nome = nome;
}

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void adicionarNota(Nota nota) {
        this.notas.add(nota);
    }

    public double calcularMedia() {
        if (notas.isEmpty()) return 0.0;

        double soma = 0;
        for (Nota nota : notas) {
            soma += nota.getValor();
        }
        return soma / notas.size();
    }

    public void exibirBoletim() {
        System.out.println("Boletim do aluno: " + nome);
        for (Nota nota : notas) {
            System.out.println("Disciplina: " + nota.getDisciplina().getNome() +
                               " | Prova: " + nota.getProva().getDescricao() +
                               " | Nota: " + nota.getValor());
        }
        System.out.printf("Média geral:  %.2f\n", calcularMedia());
    }
}
