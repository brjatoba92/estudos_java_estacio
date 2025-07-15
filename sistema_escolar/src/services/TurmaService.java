package services;

import com.google.gson.reflect.TypeToken;
import models.Professor;
import models.Turma;
import util.JsonUtil;
import dao.TurmaDAO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TurmaService {
    private static final String ARQUIVO = "turmas.json";
    private List<Turma> turmas;
    private ProfessorService professorService;

    public TurmaService(ProfessorService professorService) {
        this.professorService = professorService;
        carregar();
    }

    public void cadastrar(Turma turma) {
        turmas.add(turma);
        salvar();
        // Persistência em SQLite
        TurmaDAO dao = new TurmaDAO();
        dao.inserir(turma);
        // Atualizar total de turmas do professor
        atualizarTotalTurmasProfessor(turma.getMatriculaProfessor());
        System.out.println("\u2705 Turma cadastrada com sucesso (JSON e SQLite)!");
    }

    public List<Turma> listarTodas() {
        return turmas;
    }

    public Turma buscarPorCodigo(String codigo) {
        for (Turma t : turmas) {
            if (t.getCodigo().equalsIgnoreCase(codigo)) {
                return t;
            }
        }
        return null;
    }

    public void atualizar(String codigoAntigo, String novoCodigo, String novaSerie, Professor novoProfessor, int novaCargaHoraria) {
        Turma turma = buscarPorCodigo(codigoAntigo);
        if (turma != null) {
            String matriculaAntiga = turma.getMatriculaProfessor();
            turma.setCodigo(novoCodigo);
            turma.setSerie(novaSerie);
            turma.setProfessorResponsavel(novoProfessor);
            turma.setCargaHoraria(novaCargaHoraria);
            salvar();
            // Atualizar no banco de dados
            TurmaDAO dao = new TurmaDAO();
            dao.atualizar(codigoAntigo, novoCodigo, novaSerie, novoProfessor != null ? novoProfessor.getMatricula() : null, novaCargaHoraria);
            // Atualizar total de turmas dos professores antigo e novo
            atualizarTotalTurmasProfessor(matriculaAntiga);
            if (novoProfessor != null) {
                atualizarTotalTurmasProfessor(novoProfessor.getMatricula());
            }
            System.out.println("\u2705 Turma atualizada com sucesso!");
        } else {
            System.out.println("\u26A0 Turma não encontrada!");
        }
    }

    public void remover(String codigo) {
        Turma turma = buscarPorCodigo(codigo);
        if (turma != null) {
            String matriculaProfessor = turma.getMatriculaProfessor();
            turmas.remove(turma);
            salvar();
            // Remover do banco de dados
            TurmaDAO dao = new TurmaDAO();
            dao.remover(codigo);
            // Atualizar total de turmas do professor
            atualizarTotalTurmasProfessor(matriculaProfessor);
            System.out.println("\u2705 Turma removida com sucesso!");
        } else {
            System.out.println("\u26A0 Turma não encontrada!");
        }
    }

    private void salvar() {
        JsonUtil.salvar(turmas, ARQUIVO);
    }

    private void carregar() {
        File file = new File(ARQUIVO);
        if (file.exists()) {
            try {
                List<Turma> turmasList = JsonUtil.carregarLista(ARQUIVO, new com.google.gson.reflect.TypeToken<List<Turma>>(){}.getType());
                turmas = turmasList != null ? turmasList : new ArrayList<>();
                
                // Restaurar referências dos professores
                for (Turma turma : turmas) {
                    if (turma.getMatriculaProfessor() != null) {
                        Professor professor = professorService.buscarPorMatricula(turma.getMatriculaProfessor());
                        if (professor != null) {
                            turma.setProfessorResponsavel(professor);
                            professor.adicionarTurma(turma);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar arquivo de turmas. Criando nova lista.");
                turmas = new ArrayList<>();
                // Remover arquivo corrompido
                file.delete();
            }
        } else {
            turmas = new ArrayList<>();
        }
    }

    private void atualizarTotalTurmasProfessor(String matriculaProfessor) {
        if (matriculaProfessor == null) return;
        Professor professor = professorService.buscarPorMatricula(matriculaProfessor);
        if (professor == null) return;
        // Contar turmas do professor
        long total = turmas.stream()
            .filter(t -> matriculaProfessor.equals(t.getMatriculaProfessor()))
            .count();
        professor.setTotalTurmas((int) total);
        professor.calcularValorTotalSimples();
        professorService.atualizarTotalTurmas(professor);
    }

    public void menuInterativo() {
        Scanner scanner = new Scanner(System.in);
        // ProfessorService professorService = new ProfessorService(); // instanciando o service

        int opcao;
        do {
            System.out.println("\n==== MENU TURMA ====");
            System.out.println("1 - Cadastrar turma");
            System.out.println("2 - Listar turmas");
            System.out.println("3 - Buscar turma por código");
            System.out.println("4 - Atualizar turma");
            System.out.println("5 - Remover turma");
            System.out.println("0 - Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 :
                    System.out.print("Código: ");
                    String codigo = scanner.nextLine();
                    System.out.print("Série: ");
                    String serie = scanner.nextLine();
                    
                    // Selecionar professor
                    Professor professor = selecionarProfessor(scanner);
                    if (professor == null) {
                        break;
                    }
                    
                    System.out.print("Carga horária (horas por semana): ");
                    int cargaHoraria = Integer.parseInt(scanner.nextLine());

                    Turma novaTurma = new Turma(codigo, serie, professor, cargaHoraria);
                    cadastrar(novaTurma);
                    professor.adicionarTurma(novaTurma);
                    professorService.salvar(); // Salvar as mudanças do professor
                    break;
                
                case 2 : 
                    listarTodas().forEach(System.out::println);
                    break;

                case 3 :
                    System.out.print("Código: ");
                    String codigoBusca3 = scanner.nextLine();
                    Turma turma = buscarPorCodigo(codigoBusca3);
                    if (turma != null) {
                        System.out.println(turma);
                    } else {
                        System.out.println("\u26A0 Turma não encontrada!");
                    }
                    break;
                

                case 4 :
                    System.out.print("Código da turma a atualizar: ");
                    String codigoBusca4 = scanner.nextLine();
                    Turma turmaParaAtualizar = buscarPorCodigo(codigoBusca4);
                    if (turmaParaAtualizar == null) {
                        System.out.println("\u26A0 Turma não encontrada!");
                        break;
                    }
                    System.out.print("Novo código (ou Enter para manter: " + turmaParaAtualizar.getCodigo() + "): ");
                    String novoCodigo = scanner.nextLine();
                    if (novoCodigo.trim().isEmpty()) {
                        novoCodigo = turmaParaAtualizar.getCodigo();
                    }
                    System.out.print("Nova série (ou Enter para manter: " + turmaParaAtualizar.getSerie() + "): ");
                    String novaSerie = scanner.nextLine();
                    if (novaSerie.trim().isEmpty()) {
                        novaSerie = turmaParaAtualizar.getSerie();
                    }
                    
                    // Selecionar novo professor
                    System.out.println("Professores disponíveis:");
                    List<Professor> professores = professorService.listarTodos();
                    for (int i = 0; i < professores.size(); i++) {
                        Professor prof = professores.get(i);
                        System.out.println((i+1) + " - " + prof.toString());
                    }
                    System.out.print("Escolha o número do novo professor (ou 0 para manter o atual): ");
                    String escolhaProf = scanner.nextLine();
                    Professor novoProfessor = turmaParaAtualizar.getProfessorResponsavel(); // manter atual por padrão
                    if (!escolhaProf.equals("0")) {
                        try {
                            int escolha = Integer.parseInt(escolhaProf);
                            if (escolha > 0 && escolha <= professores.size()) {
                                novoProfessor = professores.get(escolha - 1);
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Opção inválida, mantendo professor atual.");
                        }
                    }
                    
                    System.out.print("Nova carga horária (ou Enter para manter: " + turmaParaAtualizar.getCargaHoraria() + "h): ");
                    String cargaHorariaStr = scanner.nextLine();
                    int novaCargaHoraria = turmaParaAtualizar.getCargaHoraria();
                    if (!cargaHorariaStr.trim().isEmpty()) {
                        novaCargaHoraria = Integer.parseInt(cargaHorariaStr);
                    }
                    
                    atualizar(codigoBusca4, novoCodigo, novaSerie, novoProfessor, novaCargaHoraria);
                    break;
                

                case 5 :
                    System.out.print("Código: ");
                    String codigoBusca5 = scanner.nextLine();
                    remover(codigoBusca5);
                    break;

                case 0 : 
                    System.out.println("Voltando ao menu principal...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private Professor selecionarProfessor(Scanner scanner) {
        List<Professor> professores = professorService.listarTodos();
        if (professores.isEmpty()) {
            System.out.println("Nenhum professor cadastrado. Cadastre um professor primeiro.");
            return null;
        }
        
        System.out.println("Professores disponíveis:");
        for (int i = 0; i < professores.size(); i++) {
            Professor prof = professores.get(i);
            System.out.println((i+1) + " - " + prof.toString());
        }
        
        System.out.print("Escolha o número do professor (ou digite 0 para buscar por matrícula): ");
        String escolhaStr = scanner.nextLine();
        
        if (escolhaStr.equals("0")) {
            // Buscar por matrícula
            System.out.print("Digite a matrícula do professor: ");
            String matriculaBusca = scanner.nextLine();
            Professor professor = professorService.buscarPorMatricula(matriculaBusca);
            
            if (professor != null) {
                System.out.println("✓ Professor encontrado: " + professor.getNome());
                return professor;
            } else {
                System.out.println("\u26A0 Professor não encontrado!");
                return null;
            }
        } else {
            // Escolher por número
            try {
                int escolha = Integer.parseInt(escolhaStr);
                if (escolha > 0 && escolha <= professores.size()) {
                    Professor professor = professores.get(escolha - 1);
                    System.out.println("✓ Professor selecionado: " + professor.getNome());
                    return professor;
                } else {
                    System.out.println("\u26A0 Opção inválida!");
                    return null;
                }
            } catch (NumberFormatException e) {
                System.out.println("\u26A0 Opção inválida!");
                return null;
            }
        }
    }
}
