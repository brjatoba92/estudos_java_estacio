package models;

import java.util.ArrayList;
import java.util.List;

public class Aluno {
    // atributos
    private String nome;
    private String matricula;
    private String dataNascimento;
    private Turma turma;
    private List<Nota> notas;

    // construtor
    public Aluno (String nome, String matricula, String dataNascimento) {
        this.nome = nome;
        this.matricula = matricula;
        this.dataNascimento = dataNascimento
        this.notas = new ArrayList<>();
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public Turma getTurma(Turma turma) {
        return turma;
    }

    public List<Nota> getNotas() {
        return notas;
    }

    //Métodos
    public void adcionarNota(Nota nota) {
        this.notas.add(nota)
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
        for (Nota nota: notas) {
            System.out.println("Disciplina: " + nota.getDisciplina().getNome() + 
                                " | Prova: " + nota.getProva().getDescricao() +
                                " | Nota: " + nota.getValor())
        }
        System.out.println("Média geral:  %.2f\n", calcularMedia())
    }