package models;

import java.time.LocalDate;

public class Prova {
    private String descricao;
    private Disciplina disciplina;
    private LocalDate data;
    private double peso;

    public Prova(String descricao, Disciplina disciplina, LocalDate data, double peso) {
        this.descricao = descricao;
        this.disciplina = disciplina;
        this.data = data;
        this.peso = peso;
    }

    public String getDescricao() {
        return descricao;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public LocalDate getData() {
        return data;
    }

    public double getPeso() {
        return peso;
    }

    public void exibirDetalhes() {
        System.out.println("Prova: " + descricao);
        System.out.println("Disciplina: " + disciplina.getNome());
        System.out.println("Data: " + data);
        System.out.println("Peso: " + peso);
    }

    @Override
    public String toString() {
        return descricao + " - " + disciplina.getNome() + " | " + data + " | Peso: " + peso;
    }
}
