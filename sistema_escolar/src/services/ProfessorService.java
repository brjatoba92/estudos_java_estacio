package services;

import com.google.gson.reflect.TypeToken;
import models.Professor;
import models.Disciplina;
import services.DisciplinaService;
import util.JsonUtil;
import dao.ProfessorDAO;
import services.TurmaService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class ProfessorService {
    private static final String ARQUIVO = "professores.json";
    private List<Professor> professores;
    public TurmaService turmaService;

    public ProfessorService(TurmaService turmaService) {
        this.turmaService = turmaService;
        carregar();
        // NÃO chamar atualizarTotalTurmasTodosProfessores() aqui para evitar recursão
    }

    public void cadastrar(Professor professor) {
        atualizarValorTotal(professor);
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

    public void atualizar(String matricula, String novoNome, java.util.List<String> novasDisciplinas, double novoValorHora) {
        Professor professor = buscarPorMatricula(matricula);
        if (professor != null) {
            professor.setNome(novoNome);
            professor.setDisciplinas(novasDisciplinas);
            professor.setValorHora(novoValorHora);
            atualizarValorTotal(professor);
            salvar();
            // Atualizar no banco de dados
            ProfessorDAO dao = new ProfessorDAO();
            dao.atualizar(matricula, novoNome, novasDisciplinas, novoValorHora, professor.getTotalTurmas(), professor.getValorTotal(), professor.getHorasTrabalhadasMes(), professor.getHorasPorDisciplinaMes());
            System.out.println("\u2705 Professor atualizado com sucesso (JSON e SQLite)!");
        } else {
            System.out.println("\u26A0 Professor não encontrado!");
        }
    }

    // Para compatibilidade com interface antiga
    public void atualizar(String matricula, String novoNome, String novaDisciplina, double novoValorHora) {
        atualizar(matricula, novoNome, novaDisciplina != null ? java.util.Arrays.asList(novaDisciplina) : new java.util.ArrayList<>(), novoValorHora);
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

    public void atualizarTotalTurmas(Professor professor) {
        // Atualiza no JSON
        atualizarValorTotal(professor);
        salvar();
        // Atualiza no banco de dados
        ProfessorDAO dao = new ProfessorDAO();
        dao.atualizar(professor.getMatricula(), professor.getNome(), professor.getDisciplinas(), professor.getValorHora(), professor.getTotalTurmas(), professor.getValorTotal(), professor.getHorasTrabalhadasMes(), professor.getHorasPorDisciplinaMes());
    }

    public void atualizarTotalTurmasTodosProfessores() {
        if (turmaService == null) turmaService = new TurmaService(this);
        List<Professor> professores = listarTodos();
        List<models.Turma> turmas = turmaService.listarTodas();
        for (Professor professor : professores) {
            long total = turmas.stream()
                .filter(t -> professor.getMatricula().equals(t.getMatriculaProfessor()))
                .count();
            professor.setTotalTurmas((int) total);
            atualizarValorTotal(professor);
        }
        salvar(); // Salva todos os professores com o campo atualizado no JSON
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
            System.out.println("7 - Horas por disciplina no mês");
            System.out.println("8 - Informar horas manualmente por disciplina");
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
                    
                    List<String> novaDisciplina = null;
                    if (escolhaDisciplina.trim().isEmpty() || escolhaDisciplina.equals("0")) {
                        // Manter disciplina atual
                        novaDisciplina = new ArrayList<>();
                        novaDisciplina.add(professor.getDisciplina());
                    } else {
                        try {
                            int escolha = Integer.parseInt(escolhaDisciplina);
                            if (escolha > 0 && escolha <= disciplinas.size()) {
                                novaDisciplina = new ArrayList<>();
                                novaDisciplina.add(disciplinas.get(escolha - 1).getNome());
                            } else {
                                System.out.println("⚠️ Opção inválida, mantendo disciplina atual.");
                                novaDisciplina = new ArrayList<>();
                                novaDisciplina.add(professor.getDisciplina());
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("⚠️ Opção inválida, mantendo disciplina atual.");
                            novaDisciplina = new ArrayList<>();
                            novaDisciplina.add(professor.getDisciplina());
                        }
                    }
                    
                    System.out.print("Novo valor por hora (ou Enter para manter: " + String.format("%.2f", professor.getValorHora()) + "): ");
                    String valorHoraStr = scanner.nextLine();
                    double novoValorHora = professor.getValorHora();
                    if (!valorHoraStr.trim().isEmpty()) {
                        novoValorHora = Double.parseDouble(valorHoraStr);
                    }
                    
                    professor.setNome(novoNome);
                    professor.setDisciplinas(novaDisciplina);
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
                
                case 7 :
                    System.out.print("Matrícula do professor: ");
                    String matHoras = scanner.nextLine();
                    exibirHorasPorDisciplina(matHoras);
                    break;
                
                case 8 :
                    System.out.print("Matrícula do professor: ");
                    String matHorasDisc = scanner.nextLine();
                    informarHorasPorDisciplina(matHorasDisc);
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

    // Atualiza o valorTotal do professor com base nas turmas e cargaHoraria
    public void atualizarValorTotal(Professor professor) {
        if (turmaService == null) return;
        int totalHoras = 0;
        List<models.Turma> turmasDoProfessor = turmaService.listarTurmasPorProfessor(professor.getMatricula());
        for (models.Turma turma : turmasDoProfessor) {
            totalHoras += turma.getCargaHoraria();
        }
        double valorTotal = professor.getValorHora() * totalHoras;
        professor.setHorasTrabalhadasMes(totalHoras);
        professor.setValorTotal(valorTotal);
    }

    public void exibirHorasPorDisciplina(String matriculaProfessor) {
        Professor professor = buscarPorMatricula(matriculaProfessor);
        if (professor == null) {
            System.out.println("Professor não encontrado!");
            return;
        }
        if (turmaService == null) {
            System.out.println("TurmaService não disponível!");
            return;
        }
        List<models.Turma> turmasDoProfessor = turmaService.listarTurmasPorProfessor(matriculaProfessor);
        java.util.Map<String, Integer> horasPorDisciplina = new java.util.HashMap<>();
        for (models.Turma turma : turmasDoProfessor) {
            String codDisc = turma.getCodigoDisciplina();
            int carga = turma.getCargaHoraria();
            horasPorDisciplina.put(codDisc, horasPorDisciplina.getOrDefault(codDisc, 0) + carga);
        }
        System.out.println("\nHoras trabalhadas no mês por disciplina para o professor " + professor.getNome() + ":");
        for (java.util.Map.Entry<String, Integer> entry : horasPorDisciplina.entrySet()) {
            System.out.println("Disciplina: " + entry.getKey() + " | Horas: " + entry.getValue());
        }
    }

    public void informarHorasPorDisciplina(String matricula) {
        Professor professor = buscarPorMatricula(matricula);
        if (professor == null) {
            System.out.println("Professor não encontrado!");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> horasPorDisciplina = new HashMap<>();
        System.out.println("Digite as horas trabalhadas no mês para cada disciplina do professor " + professor.getNome() + ":");
        for (String disciplina : professor.getDisciplinas()) {
            System.out.print("Disciplina: " + disciplina + " | Horas: ");
            int horas = 0;
            try {
                horas = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido, usando 0.");
            }
            horasPorDisciplina.put(disciplina, horas);
        }
        professor.setHorasPorDisciplinaMes(horasPorDisciplina);
        // Atualizar valorTotal com base nas horas informadas
        int totalHoras = horasPorDisciplina.values().stream().mapToInt(Integer::intValue).sum();
        professor.setHorasTrabalhadasMes(totalHoras);
        double valorTotal = professor.getValorHora() * totalHoras;
        professor.setValorTotal(valorTotal);
        salvar();
        // Atualizar no banco de dados
        ProfessorDAO dao = new ProfessorDAO();
        dao.atualizar(professor.getMatricula(), professor.getNome(), professor.getDisciplinas(), professor.getValorHora(), professor.getTotalTurmas(), professor.getValorTotal(), professor.getHorasTrabalhadasMes(), professor.getHorasPorDisciplinaMes());
        System.out.println("✅ Horas por disciplina atualizadas e valor total recalculado!");
    }
}
