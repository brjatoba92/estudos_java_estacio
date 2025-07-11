package models;

public class Disciplina {
    // Atributos
    private String nome;
    private int cargaHoraria; // em horas

    // Construtor
    public Disciplina(String nome, int cargaHoraria) {
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    // Método para exibir resumo da disciplina
    public void exibirInfo() {
        System.out.println("Disciplina: " + nome);
        System.out.println("Carga Horária: " + cargaHoraria + " h");
    }

    @Override
    public String toString() {
        return nome + " (" + cargaHoraria + "h)";
    }
}