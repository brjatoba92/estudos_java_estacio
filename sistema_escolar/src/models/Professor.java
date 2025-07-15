package models;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Professor {
    @Expose
    private String nome;
    @Expose
    private List<String> disciplinas;
    @Expose
    private String matricula;
    @Expose
    private double valorHora;
    @Expose
    private int totalTurmas;
    @Expose
    private double valorTotal;
    private transient List<Turma> turmas;

    // ✅ Construtor vazio exigido pelo Gson
    public Professor() {
        this.turmas = new ArrayList<>();
        this.totalTurmas = 0;
        this.disciplinas = new ArrayList<>();
        this.valorTotal = 0.0;
    }

    // Construtor principal
    public Professor(String nome, String matricula, List<String> disciplinas) {
        this.nome = nome;
        this.matricula = matricula;
        this.disciplinas = disciplinas;
        this.valorHora = 0.0;
        this.turmas = new ArrayList<>();
        this.totalTurmas = 0;
        this.valorTotal = 0.0;
    }

    // Construtor com valor hora
    public Professor(String nome, String matricula, List<String> disciplinas, double valorHora) {
        this.nome = nome;
        this.matricula = matricula;
        this.disciplinas = disciplinas;
        this.valorHora = valorHora;
        this.turmas = new ArrayList<>();
        this.totalTurmas = 0;
        this.valorTotal = 0.0;
    }

    // Construtor de compatibilidade para String disciplina
    public Professor(String nome, String matricula, String disciplina) {
        this(nome, matricula, disciplina != null ? Arrays.asList(disciplina) : new ArrayList<>());
    }
    public Professor(String nome, String matricula, String disciplina, double valorHora) {
        this(nome, matricula, disciplina != null ? Arrays.asList(disciplina) : new ArrayList<>(), valorHora);
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getDisciplinas() {
        return disciplinas;
    }
    public void setDisciplinas(List<String> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public String getDisciplina() {
        // Para compatibilidade, retorna a primeira disciplina ou vazio
        return (disciplinas != null && !disciplinas.isEmpty()) ? disciplinas.get(0) : "";
    }
    public void setDisciplina(String disciplina) {
        // Para compatibilidade, substitui a lista por uma única disciplina
        this.disciplinas = disciplina != null ? Arrays.asList(disciplina) : new ArrayList<>();
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

    public int getTotalTurmas() {
        return totalTurmas;
    }
    public void setTotalTurmas(int totalTurmas) {
        this.totalTurmas = totalTurmas;
    }

    public double getValorTotal() {
        return valorTotal;
    }
    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void adicionarTurma(Turma turma) {
        if (!turmas.contains(turma)) {
            turmas.add(turma);
            System.out.println("Turma " + turma.getCodigo() + " atribuída ao professor " + nome);
        }
    }

    public void listarTurmas() {
        System.out.println("Turmas do Professor " + nome + " (" + (disciplinas != null && !disciplinas.isEmpty() ? String.join(", ", disciplinas) : "Disciplinas não informadas") + "):");
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

    public double calcularValorTotalSimples() {
        this.valorTotal = valorHora * totalTurmas;
        return valorTotal;
    }

    public void exibirRelatorioFinanceiro() {
        System.out.println("\n=== RELATÓRIO FINANCEIRO ===");
        System.out.println("Professor: " + nome + " (" + matricula + ")");
        System.out.println("Disciplinas: " + (disciplinas != null && !disciplinas.isEmpty() ? String.join(", ", disciplinas) : "Disciplinas não informadas"));
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
        String disciplinasProf = (disciplinas != null && !disciplinas.isEmpty()) ? String.join(", ", disciplinas) : "Disciplinas não informadas";
        return "Prof. " + nomeProf + " (" + matriculaProf + ") - " + disciplinasProf + 
               " | R$ " + String.format("%.2f", valorHora) + "/h" +
               " | Turmas: " + (turmas != null ? turmas.size() : 0);
    }
}
