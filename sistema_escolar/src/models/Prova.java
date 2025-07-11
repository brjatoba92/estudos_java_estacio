package models;

import java.time.LocalDate;

public class Prova {
    private String descricao;
    private LocalDate data;
    private Disciplina disciplina;
    private double peso; // Ex: 2, 1.5

    // Construtor
    public Prova(String descricao, LocalDate data, Disciplina disciplina, double peso) {
        this.descricao = descricao;
        this.data = data;
        this.disciplina = disciplina;
        this.peso = peso;
    }

    publiic Disciplina getDescricao() {
        return descricao;
    }

    public LocalDate getData() {
        return data;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public double getPeso() {
        return peso;
    }

    // Método auxiliar
    public void exibirInfo() {
        System.out.println("Prova: " + descricao);
        System.out.println("Data: " + data);
        System.out.println("Disciplina: " + disciplina.getNome());
        System.out.println("Peso: " + peso);
    }

    @Override
    public String toString() {
        return descricao + " - " + disciplina.getNome() + " | " + data + " | Peso: " + peso;
    }
}