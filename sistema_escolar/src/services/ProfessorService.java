package services;

import com.google.gson.reflect.TypeToken;
import models.Professor;
import models.Disciplina;
import services.DisciplinaService;
import util.JsonUtil;
import dao.ProfessorDAO;

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
        // Persistência em SQLite
        ProfessorDAO dao = new ProfessorDAO();
        dao.inserir(professor);
        System.out.println("\u2705 Professor cadastrado com sucesso (JSON e SQLite)!");
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
            // Atualizar no banco de dados
            ProfessorDAO dao = new ProfessorDAO();
            dao.atualizar(matricula, novoNome, novaDisciplina, novoValorHora);
            System.out.println("\u2705 Professor atualizado com sucesso (JSON e SQLite)!");
        } else {
            System.out.println("\u26A0 Professor não encontrado!");
        }
    }

    public void remover(String matricula) {
        Professor professor = buscarPorMatricula(matricula);
        if (professor != null) {
            professores.remove(professor);
            salvar();
            // Remover do banco de dados
            ProfessorDAO dao = new ProfessorDAO();
            dao.remover(matricula);
            System.out.println("\u2705 Professor removido com sucesso (JSON e SQLite)!");
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
                    System.out.println("Matrículas já cadastradas:");
                    for (Professor prof : listarTodos()) {
                        System.out.println("- " + prof.getMatricula() + " | " + prof.getNome());
                    }
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Matrícula: ");
                    String matricula = scanner.nextLine();
                    Disciplina disciplinaSelecionada = selecionarDisciplina(scanner);
                    if (disciplinaSelecionada == null) {
                        break;
                    }
                    System.out.print("Valor por hora: ");
                    double valorHora = Double.parseDouble(scanner.nextLine());
                    cadastrar(new Professor(nome, matricula, disciplinaSelecionada.getNome(), valorHora));
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
                    Professor professor = buscarPorMatricula(matAtualiza);
                    if (professor == null) {
                        System.out.println("\u26A0 Professor não encontrado!");
                        break;
                    }
                    System.out.print("Novo nome (ou Enter para manter: " + professor.getNome() + "): ");
                    String novoNome = scanner.nextLine();
                    if (novoNome.trim().isEmpty()) novoNome = professor.getNome();
                    
                    System.out.println("Disciplinas disponíveis:");
                    DisciplinaService disciplinaService = new DisciplinaService();
                    List<Disciplina> disciplinas = disciplinaService.listarTodas();
                    if (disciplinas.isEmpty()) {
                        System.out.println("⚠️ Nenhuma disciplina cadastrada. Cadastre uma disciplina primeiro.");
                        break;
                    }
                    
                    for (int i = 0; i < disciplinas.size(); i++) {
                        Disciplina d = disciplinas.get(i);
                        System.out.println((i+1) + " - " + d.getCodigo() + " | " + d.getNome() + " | " + d.getCargaHoraria() + "h");
                    }
                    
                    System.out.print("Escolha o número da nova disciplina (ou 0 para manter: " + professor.getDisciplina() + "): ");
                    String escolhaDisciplina = scanner.nextLine();
                    
                    Disciplina novaDisciplina = null;
                    if (escolhaDisciplina.trim().isEmpty() || escolhaDisciplina.equals("0")) {
                        // Manter disciplina atual
                        novaDisciplina = new Disciplina("", professor.getDisciplina(), 0);
                    } else {
                        try {
                            int escolha = Integer.parseInt(escolhaDisciplina);
                            if (escolha > 0 && escolha <= disciplinas.size()) {
                                novaDisciplina = disciplinas.get(escolha - 1);
                            } else {
                                System.out.println("⚠️ Opção inválida, mantendo disciplina atual.");
                                novaDisciplina = new Disciplina("", professor.getDisciplina(), 0);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("⚠️ Opção inválida, mantendo disciplina atual.");
                            novaDisciplina = new Disciplina("", professor.getDisciplina(), 0);
                        }
                    }
                    
                    System.out.print("Novo valor por hora (ou Enter para manter: " + String.format("%.2f", professor.getValorHora()) + "): ");
                    String valorHoraStr = scanner.nextLine();
                    double novoValorHora = professor.getValorHora();
                    if (!valorHoraStr.trim().isEmpty()) {
                        novoValorHora = Double.parseDouble(valorHoraStr);
                    }
                    
                    professor.setNome(novoNome);
                    professor.setDisciplina(novaDisciplina.getNome());
                    professor.setValorHora(novoValorHora);
                    salvar();
                    System.out.println("\u2705 Professor atualizado com sucesso!");
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

    private Disciplina selecionarDisciplina(Scanner scanner) {
        DisciplinaService disciplinaService = new DisciplinaService();
        List<Disciplina> disciplinas = disciplinaService.listarTodas();
        if (disciplinas.isEmpty()) {
            System.out.println("Nenhuma disciplina cadastrada. Cadastre uma disciplina primeiro.");
            return null;
        }
        System.out.println("Disciplinas disponíveis:");
        for (int i = 0; i < disciplinas.size(); i++) {
            System.out.println((i+1) + " - " + disciplinas.get(i));
        }
        System.out.print("Escolha o número da disciplina: ");
        int escolha = Integer.parseInt(scanner.nextLine());
        Disciplina disciplina = disciplinas.get(escolha - 1);
        return disciplina;
    }

    private List<String> selecionarMultiplasDisciplinas(Scanner scanner) {
        DisciplinaService disciplinaService = new DisciplinaService();
        List<Disciplina> disciplinas = disciplinaService.listarTodas();
        List<String> selecionadas = new ArrayList<>();
        if (disciplinas.isEmpty()) {
            System.out.println("Nenhuma disciplina cadastrada. Cadastre uma disciplina primeiro.");
            return selecionadas;
        }
        System.out.println("Disciplinas disponíveis:");
        for (int i = 0; i < disciplinas.size(); i++) {
            System.out.println((i+1) + " - " + disciplinas.get(i));
        }
        System.out.print("Digite os números das disciplinas separadas por vírgula (ex: 1,3,4): ");
        String entrada = scanner.nextLine();
        String[] indices = entrada.split(",");
        for (String idx : indices) {
            try {
                int i = Integer.parseInt(idx.trim()) - 1;
                if (i >= 0 && i < disciplinas.size()) {
                    selecionadas.add(disciplinas.get(i).getNome());
                }
            } catch (NumberFormatException e) {
                // Ignorar entradas inválidas
            }
        }
        return selecionadas;
    }
}
