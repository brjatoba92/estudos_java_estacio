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
    @Expose
    private String ementa;

    // Construtor
    public Disciplina(String codigoCadastro, String nomeCadastro, int cargaHoraria, String ementa) {
        this.codigoCadastro = codigoCadastro;
        this.nomeCadastro = nomeCadastro;
        this.cargaHoraria = cargaHoraria;
        this.ementa = ementa;
    }

    // Construtor antigo para compatibilidade
    public Disciplina(String codigoCadastro, String nomeCadastro, int cargaHoraria) {
        this(codigoCadastro, nomeCadastro, cargaHoraria, "");
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

    public void setCodigo(String codigoCadastro) {
        this.codigoCadastro = codigoCadastro;
    }

    public String getEmenta() {
        return ementa;
    }
    public void setEmenta(String ementa) {
        this.ementa = ementa;
    }

    // Método para exibir resumo da disciplina
    public void exibirInfo() {
        System.out.println("Disciplina: " + nomeCadastro);
        System.out.println("Carga Horária: " + cargaHoraria + " h");
        System.out.println("Ementa: " + (ementa != null ? ementa : "Não informada"));
    }

    @Override
    public String toString() {
        return codigoCadastro +  " - " + nomeCadastro + " (" + cargaHoraria + "h)" +
               (ementa != null && !ementa.isEmpty() ? "\nEmenta: " + ementa : "");
    }
}