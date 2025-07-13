package services;

import com.google.gson.reflect.TypeToken;
import models.Aluno;
import models.Nota;
import models.Disciplina;
import models.Prova;

import util.JsonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
        System.out.println("\u2705 Aluno cadastrado com sucesso!");
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

    public void atualizar(String matricula, String novoNome, String novaDataNascimento) {
        Aluno aluno = buscarPorMatricula(matricula);
        if (aluno != null) {
            aluno.setNome(novoNome);
            aluno.setDataNascimento(novaDataNascimento);
            salvar();
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
            System.out.println("6. Adicionar nota");
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
                    for (Aluno a : listarTodos()) {
                        System.out.println(a.getMatricula() + " | " + a.getNome() + " | " + a.getDataNascimento());
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
                    System.out.print("Matrícula: ");
                    String matAtualizar = scanner.nextLine();
                    System.out.print("Novo nome: ");
                    String novoNome = scanner.nextLine();
                    System.out.print("Nova data de nascimento: ");
                    String novaData = scanner.nextLine();
                    atualizar(matAtualizar, novoNome, novaData);
                    break;

                case 5:
                    System.out.print("Matrícula: ");
                    String matRemover = scanner.nextLine();
                    remover(matRemover);
                    break;
                
                case 6:
                    System.out.print("Matrícula do aluno: ");
                    String matAluno = scanner.nextLine();
                    aluno = buscarPorMatricula(matAluno);

                    if (aluno == null) {
                        System.out.println("\u26A0 Aluno nao encontrado!");
                        break;
                    }
                    System.out.print("Código da disciplina: ");
                    String codDisciplina = scanner.nextLine();
                    System.out.print("Nome da disciplina : ");
                    String nomeDisciplina = scanner.nextLine();
                    System.out.print("Carga horaria: ");
                    int cargaHoraria = Integer.parseInt(scanner.nextLine());

                    Disciplina disciplina = new Disciplina(codDisciplina, nomeDisciplina, cargaHoraria);

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