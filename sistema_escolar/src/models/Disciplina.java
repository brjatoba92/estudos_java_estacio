package models;

import com.google.gson.annotations.Expose;

public class Disciplina {
    // Atributos
    @Expose
    private String codigoCadastro;
    @Expose
    private String nomeCadastro;
    @Expose
    private int cargaHoraria; // em horas

    // Construtor
    public Disciplina(String codigoCadastro, String nomeCadastro, int cargaHoraria) {
        this.codigoCadastro = codigoCadastro;
        this.nomeCadastro = nomeCadastro;
        this.cargaHoraria = cargaHoraria;
    }

    // Getters e Setters

    public String getCodigo() {
        return codigoCadastro;
    }
    public String getNome() {
        return nomeCadastro;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setNome(String nomeCadastro) {
        this.nomeCadastro = nomeCadastro;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    // Método para exibir resumo da disciplina
    public void exibirInfo() {
        System.out.println("Disciplina: " + nomeCadastro);
        System.out.println("Carga Horária: " + cargaHoraria + " h");
    }

    @Override
    public String toString() {
        return codigoCadastro +  " - " + nomeCadastro + " (" + cargaHoraria + "h)";
    }
}