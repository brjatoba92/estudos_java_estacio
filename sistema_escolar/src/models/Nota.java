package models;

import com.google.gson.annotations.Expose;

public class Nota {
    @Expose
    private double valor;
    @Expose
    private Prova prova;

    // Construtor
    public Nota(double valor, Prova prova) {
        this.valor = valor;
        this.prova = prova;
    }

    // Getters e Setters
    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Prova getProva() {
        return prova;
    }

    public Disciplina getDisciplina() {
        return prova.getDisciplina();
    }

    public void exibirNota() {
        System.out.println("Nota: " + valor +  " | Disciplina: " + prova.getDisciplina().getNome() +
                           " | Prova: " + prova.getDescricao());
    }
}