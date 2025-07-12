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

    public void atualizar(String matricula, String novoNome, String novaDisciplina) {
        Professor professor = buscarPorMatricula(matricula);
        if (professor != null) {
            professor.setNome(novoNome);
            professor.setDisciplina(novaDisciplina);
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

    private void salvar() {
        JsonUtil.salvar(professores, ARQUIVO);
    }

    private void carregar() {
        File file = new File(ARQUIVO);
        if (file.exists()) {
            professores = JsonUtil.carregarLista(ARQUIVO, new TypeToken<List<Professor>>() {}.getType());
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
                    cadastrar(new Professor(nome, matricula, disciplina));
                    break;
                
                case 2 : listarTodos().forEach(p ->
                        System.out.println(p.getMatricula() + " | " + p.getNome() + " | " + p.getDisciplina()));
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
                    atualizar(matAtualiza, novoNome, disc);
                    break;
                case 5 :
                    System.out.print("Matrícula: ");
                    String matRemove = scanner.nextLine();
                    remover(matRemove);
                    break;
                
                case 0 : System.out.println("Retornando...");
                default : System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }
}
