package models;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;

public class Professor {
    @Expose
    private String nome;
    @Expose
    private String disciplina;
    @Expose
    private String matricula;
    @Expose
    private double valorHora;
    private transient List<Turma> turmas;

    // ✅ Construtor vazio exigido pelo Gson
    public Professor() {
        this.turmas = new ArrayList<>();
    }

    // Construtor principal
    public Professor(String nome, String matricula, String disciplina) {
        this.nome = nome;
        this.matricula = matricula;
        this.disciplina = disciplina;
        this.valorHora = 0.0;
        this.turmas = new ArrayList<>();
    }

    // Construtor com valor hora
    public Professor(String nome, String matricula, String disciplina, double valorHora) {
        this.nome = nome;
        this.matricula = matricula;
        this.disciplina = disciplina;
        this.valorHora = valorHora;
        this.turmas = new ArrayList<>();
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public List<Turma> getTurmas() {
        return turmas;
    }

    public String getMatricula() {
        return this.matricula;
    }

    public double getValorHora() {
        return valorHora;
    }

    public void setValorHora(double valorHora) {
        this.valorHora = valorHora;
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

    public double calcularValorTotal() {
        double valorTotal = 0.0;
        for (Turma turma : turmas) {
            valorTotal += turma.getCargaHoraria() * valorHora;
        }
        return valorTotal;
    }

    public void exibirRelatorioFinanceiro() {
        System.out.println("\n=== RELATÓRIO FINANCEIRO ===");
        System.out.println("Professor: " + nome + " (" + matricula + ")");
        System.out.println("Disciplina: " + disciplina);
        System.out.println("Valor por hora: R$ " + String.format("%.2f", valorHora));
        System.out.println("\nTurmas e valores:");
        
        double valorTotal = 0.0;
        for (Turma turma : turmas) {
            double valorTurma = turma.getCargaHoraria() * valorHora;
            valorTotal += valorTurma;
            System.out.println("- " + turma.getCodigo() + " (" + turma.getSerie() + "): " + 
                             turma.getCargaHoraria() + "h × R$ " + String.format("%.2f", valorHora) + 
                             " = R$ " + String.format("%.2f", valorTurma));
        }
        
        System.out.println("\nVALOR TOTAL: R$ " + String.format("%.2f", valorTotal));
    }

    @Override
    public String toString() {
        String nomeProf = nome != null ? nome : "Nome não informado";
        String matriculaProf = matricula != null ? matricula : "Matrícula não informada";
        String disciplinaProf = disciplina != null ? disciplina : "Disciplina não informada";
        return "Prof. " + nomeProf + " (" + matriculaProf + ") - " + disciplinaProf + 
               " | R$ " + String.format("%.2f", valorHora) + "/h" +
               " | Turmas: " + (turmas != null ? turmas.size() : 0);
    }
}
