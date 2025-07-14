package models;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Aluno {
    @Expose
    private String nome;
    @Expose
    private String matricula;
    @Expose
    private String dataNascimento;
    private transient Turma turma;
    @Expose
    private List<Nota> notas;
    @Expose
    private double mediaGeral;
    @Expose
    private Map<String, Double> mediasPorDisciplina;
    @Expose
    private String statusAprovacao;

    public Aluno(String nome, String matricula, String dataNascimento) {
        this.nome = nome;
        this.matricula = matricula;
        this.dataNascimento = dataNascimento;
        this.notas = new ArrayList<>();
        this.mediasPorDisciplina = new HashMap<>();
        this.mediaGeral = 0.0;
        this.statusAprovacao = "SEM NOTAS";
    }

    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public List<Nota> getNotas() {
        return notas;
    }

    public double getMediaGeral() {
        return mediaGeral;
    }

    public void setMediaGeral(double mediaGeral) {
        this.mediaGeral = mediaGeral;
    }

    public Map<String, Double> getMediasPorDisciplina() {
        return mediasPorDisciplina;
    }

    public void setMediasPorDisciplina(Map<String, Double> mediasPorDisciplina) {
        this.mediasPorDisciplina = mediasPorDisciplina;
    }

    public String getStatusAprovacao() {
        return statusAprovacao;
    }

    public void setStatusAprovacao(String statusAprovacao) {
        this.statusAprovacao = statusAprovacao;
    }

    public void setNome(String nome) {
    this.nome = nome;
}

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void adicionarNota(Nota nota) {
        this.notas.add(nota);
        atualizarMedias();
    }

    public void atualizarMedias() {
        // Atualizar média geral
        this.mediaGeral = calcularMedia();
        
        // Atualizar status de aprovação
        this.statusAprovacao = calcularStatusAprovacao();
        
        // Atualizar médias por disciplina
        this.mediasPorDisciplina.clear();
        Map<String, List<Double>> notasPorDisciplina = new HashMap<>();
        
        for (Nota nota : notas) {
            String codigoDisciplina = nota.getDisciplina().getCodigo();
            notasPorDisciplina.computeIfAbsent(codigoDisciplina, k -> new ArrayList<>()).add(nota.getValor());
        }
        
        for (Map.Entry<String, List<Double>> entry : notasPorDisciplina.entrySet()) {
            String codigoDisciplina = entry.getKey();
            double media = calcularMediaDisciplina(codigoDisciplina);
            mediasPorDisciplina.put(codigoDisciplina, media);
        }
    }

    public double calcularMedia() {
        if (notas.isEmpty()) return 0.0;

        double somaPonderada = 0.0;
        double somaPesos = 0.0;
        
        for (Nota nota : notas) {
            double peso = nota.getProva().getPeso();
            somaPonderada += nota.getValor() * peso;
            somaPesos += peso;
        }
        
        return somaPesos > 0 ? somaPonderada / somaPesos : 0.0;
    }

    public double calcularMediaDisciplina(String codigoDisciplina) {
        if (notas.isEmpty()) return 0.0;

        double somaPonderada = 0.0;
        double somaPesos = 0.0;
        
        for (Nota nota : notas) {
            if (nota.getDisciplina().getCodigo().equalsIgnoreCase(codigoDisciplina)) {
                double peso = nota.getProva().getPeso();
                somaPonderada += nota.getValor() * peso;
                somaPesos += peso;
            }
        }
        
        return somaPesos > 0 ? somaPonderada / somaPesos : 0.0;
    }

    public String calcularStatusAprovacao() {
        if (notas.isEmpty()) {
            return "SEM NOTAS";
        }
        
        double media = calcularMedia();
        if (media >= 7.0) {
            return "APROVADO";
        } else if (media >= 5.0) {
            return "RECUPERAÇÃO";
        } else {
            return "REPROVADO";
        }
    }

    public String getStatusAprovacaoSalvo() {
        return statusAprovacao;
    }

    public String getStatusAprovacaoDisciplina(String codigoDisciplina) {
        double media = calcularMediaDisciplina(codigoDisciplina);
        if (media >= 7.0) {
            return "APROVADO";
        } else if (media >= 5.0) {
            return "RECUPERAÇÃO";
        } else {
            return "REPROVADO";
        }
    }

    public void exibirBoletim() {
        System.out.println("Boletim do aluno: " + nome);
        for (Nota nota : notas) {
            System.out.println("Disciplina: " + nota.getDisciplina().getNome() +
                               " | Prova: " + nota.getProva().getDescricao() +
                               " | Nota: " + nota.getValor());
        }
        System.out.printf("Média geral:  %.2f\n", calcularMedia());
    }

    @Override
    public String toString() {
        String nomeAluno = nome != null ? nome : "Nome não informado";
        String matriculaAluno = matricula != null ? matricula : "Matrícula não informada";
        String statusAluno = statusAprovacao != null ? statusAprovacao : "Status não informado";
        
        return nomeAluno + " (" + matriculaAluno + ") - Média: " + String.format("%.2f", mediaGeral) + 
               " | Status: " + statusAluno + " | Notas: " + (notas != null ? notas.size() : 0);
    }
}
