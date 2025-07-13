package services;

import com.google.gson.reflect.TypeToken;
import models.Professor;
import util.JsonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProfessorService {
    private static final String ARQUIVO = "professores.json";
    private List<Professor> professores;

    public ProfessorService() {
        carregar();
    }

    public void cadastrar(Professor professor) {
        professores.add(professor);
        salvar();
        System.out.println("\u2705 Professor cadastrado com sucesso!");
    }

    public List<Professor> listarTodos() {
        return professores;
    }

    public Professor buscarPorMatricula(String matricula) {
        for (Professor p : professores) {
            if (p.getMatricula().equalsIgnoreCase(matricula)) {
                return p;
            }
        }
        return null;
    }

    public void atualizar(String matricula, String novoNome, String novaDisciplina, double novoValorHora) {
        Professor professor = buscarPorMatricula(matricula);
        if (professor != null) {
            professor.setNome(novoNome);
            professor.setDisciplina(novaDisciplina);
            professor.setValorHora(novoValorHora);
            salvar();
            System.out.println("\u2705 Professor atualizado com sucesso!");
        } else {
            System.out.println("\u26A0 Professor não encontrado!");
        }
    }

    public void remover(String matricula) {
        Professor professor = buscarPorMatricula(matricula);
        if (professor != null) {
            professores.remove(professor);
            salvar();
            System.out.println("\u2705 Professor removido com sucesso!");
        } else {
            System.out.println("\u26A0 Professor não encontrado!");
        }
    }

    public void salvar() {
        JsonUtil.salvar(professores, ARQUIVO);
    }

    public void exibirRelatorioGeral() {
        System.out.println("\n=== RELATÓRIO FINANCEIRO GERAL ===");
        double valorTotalGeral = 0.0;
        
        for (Professor professor : professores) {
            double valorProfessor = professor.calcularValorTotal();
            valorTotalGeral += valorProfessor;
            
            System.out.println("\n" + professor.getNome() + " (" + professor.getMatricula() + ")");
            System.out.println("Disciplina: " + professor.getDisciplina());
            System.out.println("Valor por hora: R$ " + String.format("%.2f", professor.getValorHora()));
            System.out.println("Total de turmas: " + professor.getTurmas().size());
            System.out.println("Valor total: R$ " + String.format("%.2f", valorProfessor));
        }
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("VALOR TOTAL GERAL: R$ " + String.format("%.2f", valorTotalGeral));
    }

    private void carregar() {
        File file = new File(ARQUIVO);
        if (file.exists()) {
            try {
                professores = JsonUtil.carregarLista(ARQUIVO, new TypeToken<List<Professor>>() {}.getType());
                if (professores == null) {
                    professores = new ArrayList<>();
                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar arquivo de professores. Criando nova lista.");
                professores = new ArrayList<>();
                // Remover arquivo corrompido
                file.delete();
            }
        } else {
            professores = new ArrayList<>();
        }
    }

    public void menuInterativo() {
        Scanner scanner = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("\n==== MENU PROFESSOR ====");
            System.out.println("1 - Cadastrar professor");
            System.out.println("2 - Listar professores");
            System.out.println("3 - Buscar professor por matrícula");
            System.out.println("4 - Atualizar professor");
            System.out.println("5 - Remover professor");
            System.out.println("6 - Relatório financeiro");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 :
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Matrícula: ");
                    String matricula = scanner.nextLine();
                    System.out.print("Disciplina: ");
                    String disciplina = scanner.nextLine();
                    System.out.print("Valor por hora: ");
                    double valorHora = Double.parseDouble(scanner.nextLine());
                    cadastrar(new Professor(nome, matricula, disciplina, valorHora));
                    break;
                
                case 2 : 
                    listarTodos().forEach(p ->
                        System.out.println(p.getMatricula() + " | " + p.getNome() + " | " + p.getDisciplina() + " | R$ " + String.format("%.2f", p.getValorHora()) + "/h"));
                    break;
                case 3 :
                    System.out.print("Matrícula: ");
                    String matBusca = scanner.nextLine();
                    Professor p = buscarPorMatricula(matBusca);
                    if (p != null) {
                        System.out.println("Nome: " + p.getNome() + ", Disciplina: " + p.getDisciplina());
                    } else {
                        System.out.println("\u26A0 Professor não encontrado!");
                    }
                    break;
                
                case 4 :
                    System.out.print("Matrícula: ");
                    String matAtualiza = scanner.nextLine();
                    System.out.print("Novo nome: ");
                    String novoNome = scanner.nextLine();
                    System.out.print("Nova disciplina: ");
                    String disc = scanner.nextLine();
                    System.out.print("Novo valor por hora: ");
                    double novoValorHora = Double.parseDouble(scanner.nextLine());
                    atualizar(matAtualiza, novoNome, disc, novoValorHora);
                    break;
                case 5 :
                    System.out.print("Matrícula: ");
                    String matRemove = scanner.nextLine();
                    remover(matRemove);
                    break;
                
                case 6 :
                    System.out.println("\n=== RELATÓRIOS FINANCEIROS ===");
                    System.out.println("1 - Relatório individual");
                    System.out.println("2 - Relatório geral");
                    System.out.print("Escolha uma opção: ");
                    int opcaoRelatorio = Integer.parseInt(scanner.nextLine());
                    
                    if (opcaoRelatorio == 1) {
                        System.out.print("Matrícula do professor: ");
                        String matRelatorio = scanner.nextLine();
                        Professor profRelatorio = buscarPorMatricula(matRelatorio);
                        if (profRelatorio != null) {
                            profRelatorio.exibirRelatorioFinanceiro();
                        } else {
                            System.out.println("\u26A0 Professor não encontrado!");
                        }
                    } else if (opcaoRelatorio == 2) {
                        exibirRelatorioGeral();
                    } else {
                        System.out.println("Opção inválida!");
                    }
                    break;
                
                case 0 : 
                    System.out.println("Voltando ao menu principal...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }
}
