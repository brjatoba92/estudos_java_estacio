package services;

import com.google.gson.reflect.TypeToken;
import models.Aluno;
import models.Nota;
import models.Disciplina;
import models.Prova;
import models.Turma;
import services.TurmaService;
import services.DisciplinaService;

import util.JsonUtil;
import dao.AlunoDAO;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDate;

public class AlunoService {
    private static final String ARQUIVO = "alunos.json";
    private List<Aluno> alunos;

    public AlunoService() {
        carregar();
    }

    public void cadastrar(Aluno aluno) {
        alunos.add(aluno);
        salvar();
        // Persistência em SQLite
        AlunoDAO dao = new AlunoDAO();
        dao.inserir(aluno);
        System.out.println("\u2705 Aluno cadastrado com sucesso (JSON e SQLite)!");
    }

    public List<Aluno> listarTodos() {
        return alunos;
    }

    private List<Nota> notas = new ArrayList<>();

    public Aluno buscarPorMatricula(String matricula) {
        for (Aluno aluno : alunos) {
            if (aluno.getMatricula().equalsIgnoreCase(matricula)) {
                return aluno;
            }
        }
        return null;
    }

    public void atualizar(String matriculaAntiga, String novaMatricula, String novoNome, String novaDataNascimento) {
        Aluno aluno = buscarPorMatricula(matriculaAntiga);
        if (aluno != null) {
            aluno.setMatricula(novaMatricula);
            aluno.setNome(novoNome);
            aluno.setDataNascimento(novaDataNascimento);
            salvar();
            // Atualizar no banco de dados
            AlunoDAO dao = new AlunoDAO();
            dao.atualizar(matriculaAntiga, novaMatricula, novoNome, novaDataNascimento);
            System.out.println("\u2705 Aluno atualizado com sucesso!");
        } else {
            System.out.println("\u26A0 Aluno não encontrado!");
        }
    }

    private void remover(String matricula) {
        Aluno aluno = buscarPorMatricula(matricula);
        if (aluno != null) {
            alunos.remove(aluno);
            salvar();
            System.out.println("\u2705 Aluno removido com sucesso!");
        } else {
            System.out.println("\u26A0 Aluno não encontrado!");
        }
    }

    private void salvar() {
        JsonUtil.salvar(alunos, ARQUIVO);
    }

    private void carregar() {
        File file = new File(ARQUIVO);
        if (file.exists()) {
            try {
                TypeToken<List<Aluno>> token = new TypeToken<List<Aluno>>() {};
                alunos = JsonUtil.carregarLista(ARQUIVO, token.getType());
                if (alunos == null) {
                    alunos = new ArrayList<>();
                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar arquivo de alunos. Criando nova lista.");
                alunos = new ArrayList<>();
                // Remover arquivo corrompido
                file.delete();
            }
        } else {
            alunos = new ArrayList<>();
        }
    }

    public void adicionarNota(Nota nota) {
        notas.add(nota);
    }

    public void menuInterativo() {
        Scanner scanner = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("\n==== MENU ALUNO ====");
            System.out.println("1 - Cadastrar aluno");
            System.out.println("2 - Listar alunos");
            System.out.println("3 - Buscar aluno por matrícula");
            System.out.println("4 - Atualizar aluno");
            System.out.println("5 - Remover aluno");
            System.out.println("6 - Adicionar nota");
            System.out.println("7 - Calcular médias");
            System.out.println("8 - Atualizar médias de todos os alunos");
            System.out.println("9 - Listar alunos do banco de dados");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // consumir quebra de linha pendente

            switch (opcao) {
                case 1:
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Matrícula: ");
                    String matricula = scanner.nextLine();
                    System.out.print("Data de nascimento (yyyy-mm-dd): ");
                    String data = scanner.nextLine();
                    cadastrar(new Aluno(nome, matricula, data));
                    break;

                case 2:
                    System.out.println("\n=== LISTA DE ALUNOS ===");
                    System.out.println("Matrícula | Nome | Data Nascimento | Média Geral | Status");
                    System.out.println("-".repeat(80));
                    for (Aluno a : listarTodos()) {
                        System.out.println(a.getMatricula() + " | " + a.getNome() + " | " + 
                                         a.getDataNascimento() + " | " + 
                                         String.format("%.2f", a.getMediaGeral()) + " | " + 
                                         a.getStatusAprovacaoSalvo());
                    }
                    break;

                case 3:
                    System.out.print("Matrícula: ");
                    String mat = scanner.nextLine();
                    Aluno aluno = buscarPorMatricula(mat);
                    if (aluno != null) {
                        System.out.println("Nome: " + aluno.getNome() + ", Nascimento: " + aluno.getDataNascimento());
                    } else {
                        System.out.println("Aluno não encontrado.");
                    }
                    break;

                case 4:
                    System.out.print("Matrícula atual: ");
                    String matAtualizar = scanner.nextLine();
                    Aluno alunoAtualizar = buscarPorMatricula(matAtualizar);
                    if (alunoAtualizar == null) {
                        System.out.println("\u26A0 Aluno não encontrado!");
                        break;
                    }
                    System.out.print("Nova matrícula (ou Enter para manter: " + alunoAtualizar.getMatricula() + "): ");
                    String novaMatricula = scanner.nextLine();
                    if (novaMatricula.trim().isEmpty()) novaMatricula = alunoAtualizar.getMatricula();
                    System.out.print("Novo nome (ou Enter para manter: " + alunoAtualizar.getNome() + "): ");
                    String novoNome = scanner.nextLine();
                    if (novoNome.trim().isEmpty()) novoNome = alunoAtualizar.getNome();
                    System.out.print("Nova data de nascimento (ou Enter para manter: " + alunoAtualizar.getDataNascimento() + "): ");
                    String novaData = scanner.nextLine();
                    if (novaData.trim().isEmpty()) novaData = alunoAtualizar.getDataNascimento();
                    atualizar(matAtualizar, novaMatricula, novoNome, novaData);
                    break;

                case 5:
                    System.out.print("Matrícula: ");
                    String matRemover = scanner.nextLine();
                    remover(matRemover);
                    break;
                
                case 6:
                    System.out.println("Matrículas dos alunos cadastrados:");
                    for (Aluno a : listarTodos()) {
                        System.out.println("- " + a.getMatricula() + " | " + a.getNome());
                    }
                    System.out.print("Matrícula do aluno: ");
                    String matAluno = scanner.nextLine();
                    aluno = buscarPorMatricula(matAluno);

                    if (aluno == null) {
                        System.out.println("\u26A0 Aluno nao encontrado!");
                        break;
                    }
                    
                    // Selecionar disciplina
                    Disciplina disciplina = selecionarDisciplina(scanner);
                    if (disciplina == null) {
                        break;
                    }

                    System.out.print("Descrição da prova: ");
                    String descricao = scanner.nextLine();
                    System.out.print("Data da prova (yyyy-mm-dd): ");
                    LocalDate dataProva = LocalDate.parse(scanner.nextLine());
                    System.out.print("Peso da prova: ");
                    double peso = Double.parseDouble(scanner.nextLine());

                    Prova prova = new Prova(descricao, disciplina, dataProva, peso);

                    System.out.print("Nota obtida: ");
                    double notaValor = scanner.nextDouble();
                    
                    Nota nota = new Nota(notaValor, prova);
                    aluno.adicionarNota(nota);
                    salvar();
                    System.out.println("\u2705 Nota adicionada ao aluno com sucesso!");
                    System.out.println("Média atualizada: " + String.format("%.2f", aluno.getMediaGeral()) + 
                                     " - Status: " + aluno.getStatusAprovacaoSalvo());
                    break;
                
                case 7:
                    System.out.println("\n=== CÁLCULO DE MÉDIAS ===");
                    System.out.println("1 - Média individual do aluno");
                    System.out.println("2 - Média geral da turma");
                    System.out.println("3 - Média por disciplina");
                    System.out.print("Escolha uma opção: ");
                    int opcaoMedia = Integer.parseInt(scanner.nextLine());
                    
                    switch (opcaoMedia) {
                        case 1:
                            calcularMediaIndividual(scanner);
                            break;
                        case 2:
                            calcularMediaTurma(scanner);
                            break;
                        case 3:
                            calcularMediaDisciplina(scanner);
                            break;
                        default:
                            System.out.println("Opção inválida!");
                    }
                    break;
                
                case 8:
                    atualizarMediasTodosAlunos();
                    break;
                
                case 9:
                    listarAlunosDoBanco();
                    break;
                
                case 0 : 
                    System.out.println("Voltando ao menu principal...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private void calcularMediaIndividual(Scanner scanner) {
        System.out.print("Matrícula do aluno: ");
        String matricula = scanner.nextLine();
        Aluno aluno = buscarPorMatricula(matricula);
        
        if (aluno == null) {
            System.out.println("\u26A0 Aluno não encontrado!");
            return;
        }
        
        System.out.println("\n=== MÉDIA INDIVIDUAL ===");
        System.out.println("Aluno: " + aluno.getNome() + " (" + matricula + ")");
        
        if (aluno.getNotas().isEmpty()) {
            System.out.println("Nenhuma nota registrada para este aluno.");
            return;
        }
        
        // Média geral
        System.out.println("Média geral: " + String.format("%.2f", aluno.getMediaGeral()));
        System.out.println("Status: " + aluno.getStatusAprovacaoSalvo());
        
        // Média por disciplina
        System.out.println("\nMédias por disciplina:");
        if (aluno.getMediasPorDisciplina().isEmpty()) {
            System.out.println("Nenhuma média por disciplina calculada.");
        } else {
            for (Map.Entry<String, Double> entry : aluno.getMediasPorDisciplina().entrySet()) {
                String codigoDisciplina = entry.getKey();
                Double media = entry.getValue();
                
                // Buscar nome da disciplina
                String nomeDisciplina = "Desconhecida";
                for (Nota nota : aluno.getNotas()) {
                    if (nota.getDisciplina().getCodigo().equals(codigoDisciplina)) {
                        nomeDisciplina = nota.getDisciplina().getNome();
                        break;
                    }
                }
                
                System.out.println("- " + nomeDisciplina + " (" + codigoDisciplina + "): " + 
                                 String.format("%.2f", media) + " - " + 
                                 aluno.getStatusAprovacaoDisciplina(codigoDisciplina));
            }
        }
        
        // Detalhamento das notas
        System.out.println("\nDetalhamento das notas:");
        for (Nota nota : aluno.getNotas()) {
            System.out.println("- " + nota.getDisciplina().getNome() + ": " + 
                             String.format("%.2f", nota.getValor()) + 
                             " (Prova: " + nota.getProva().getDescricao() + 
                             ", Peso: " + nota.getProva().getPeso() + ")");
        }
    }

    private void calcularMediaTurma(Scanner scanner) {
        System.out.print("Código da turma: ");
        String codigoTurma = scanner.nextLine();
        
        // Buscar turma (precisamos acessar o TurmaService)
        TurmaService turmaService = new TurmaService();
        Turma turma = turmaService.buscarPorCodigo(codigoTurma);
        
        if (turma == null) {
            System.out.println("\u26A0 Turma não encontrada!");
            return;
        }
        
        System.out.println("\n=== MÉDIA DA TURMA ===");
        System.out.println("Turma: " + turma.getCodigo() + " - " + turma.getSerie());
        
        List<Aluno> alunosTurma = turma.getAlunos();
        if (alunosTurma.isEmpty()) {
            System.out.println("Nenhum aluno matriculado nesta turma.");
            return;
        }
        
        double mediaTurma = 0.0;
        int totalAlunos = 0;
        
        System.out.println("\nMédias individuais:");
        for (Aluno aluno : alunosTurma) {
            double mediaAluno = aluno.calcularMedia();
            mediaTurma += mediaAluno;
            totalAlunos++;
            System.out.println("- " + aluno.getNome() + ": " + String.format("%.2f", mediaAluno));
        }
        
        if (totalAlunos > 0) {
            mediaTurma /= totalAlunos;
            System.out.println("\nMédia geral da turma: " + String.format("%.2f", mediaTurma));
        }
    }

    private void calcularMediaDisciplina(Scanner scanner) {
        System.out.print("Código da disciplina: ");
        String codigoDisciplina = scanner.nextLine();
        
        System.out.println("\n=== MÉDIA POR DISCIPLINA ===");
        System.out.println("Disciplina: " + codigoDisciplina);
        
        double mediaDisciplina = 0.0;
        int totalNotas = 0;
        List<Aluno> alunosComNotas = new ArrayList<>();
        
        for (Aluno aluno : alunos) {
            double somaNotasDisciplina = 0.0;
            int contadorNotas = 0;
            
            for (Nota nota : aluno.getNotas()) {
                if (nota.getDisciplina().getCodigo().equalsIgnoreCase(codigoDisciplina)) {
                    somaNotasDisciplina += nota.getValor();
                    contadorNotas++;
                }
            }
            
            if (contadorNotas > 0) {
                double mediaAluno = somaNotasDisciplina / contadorNotas;
                mediaDisciplina += mediaAluno;
                totalNotas++;
                alunosComNotas.add(aluno);
                System.out.println("- " + aluno.getNome() + ": " + String.format("%.2f", mediaAluno));
            }
        }
        
        if (totalNotas > 0) {
            mediaDisciplina /= totalNotas;
            System.out.println("\nMédia geral da disciplina: " + String.format("%.2f", mediaDisciplina));
            System.out.println("Total de alunos com notas: " + totalNotas);
        } else {
            System.out.println("Nenhuma nota encontrada para esta disciplina.");
        }
    }

    private void calcularMediaPorDisciplina(Aluno aluno) {
        Map<String, String> nomesDisciplinas = new HashMap<>();
        
        for (Nota nota : aluno.getNotas()) {
            String codigoDisciplina = nota.getDisciplina().getCodigo();
            String nomeDisciplina = nota.getDisciplina().getNome();
            nomesDisciplinas.put(codigoDisciplina, nomeDisciplina);
        }
        
        for (Map.Entry<String, String> entry : nomesDisciplinas.entrySet()) {
            String codigoDisciplina = entry.getKey();
            String nomeDisciplina = entry.getValue();
            
            double media = aluno.calcularMediaDisciplina(codigoDisciplina);
            int quantidadeNotas = (int) aluno.getNotas().stream()
                .filter(nota -> nota.getDisciplina().getCodigo().equalsIgnoreCase(codigoDisciplina))
                .count();
            
            System.out.println("- " + nomeDisciplina + " (" + codigoDisciplina + "): " + 
                             String.format("%.2f", media) + " (" + quantidadeNotas + " nota(s)) - " + 
                             aluno.getStatusAprovacaoDisciplina(codigoDisciplina));
        }
    }

    private void atualizarMediasTodosAlunos() {
        System.out.println("\n=== ATUALIZANDO MÉDIAS DE TODOS OS ALUNOS ===");
        int alunosAtualizados = 0;
        
        for (Aluno aluno : alunos) {
            if (!aluno.getNotas().isEmpty()) {
                aluno.atualizarMedias();
                alunosAtualizados++;
                System.out.println("✓ " + aluno.getNome() + " - Média: " + 
                                 String.format("%.2f", aluno.getMediaGeral()) + 
                                 " - Status: " + aluno.getStatusAprovacaoSalvo());
            }
        }
        
        if (alunosAtualizados > 0) {
            salvar();
            System.out.println("\n✅ " + alunosAtualizados + " aluno(s) atualizado(s) com sucesso!");
        } else {
            System.out.println("\n⚠️ Nenhum aluno com notas encontrado para atualizar.");
        }
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

    public void listarAlunosDoBanco() {
        System.out.println("\n=== ALUNOS NO BANCO DE DADOS ===");
        AlunoDAO dao = new AlunoDAO();
        List<Aluno> alunosBanco = dao.listarTodos();
        if (alunosBanco.isEmpty()) {
            System.out.println("Nenhum aluno encontrado no banco de dados.");
        } else {
            for (Aluno aluno : alunosBanco) {
                System.out.println("- " + aluno.getMatricula() + " | " + aluno.getNome() + " | " + aluno.getDataNascimento());
            }
        }
    }
}