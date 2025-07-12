package services;

// import com.google.gson.reflect.TypeToken;
import models.Aluno;
import util.JsonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
            alunos = JsonUtil.carregarLista(ARQUIVO, ArrayList.class);
        } else {
            alunos = new ArrayList<>();
        }
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

                case 0:
                    System.out.println("Encerrando...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }
}
