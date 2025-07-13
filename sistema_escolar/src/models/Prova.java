package models;

import com.google.gson.annotations.Expose;
import java.time.LocalDate;

public class Prova {
    @Expose
    private String descricao;
    @Expose
    private Disciplina disciplina;
    @Expose
    private LocalDate data;
    @Expose
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
