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
        Professor prof = buscarPorMatricula(matricula);
        if(prof != null) {
            prof.setNome(novoNome);
            prof.setDisciplina(novaDisciplina);
            salvar();
            System.out.println("\u2705 Professor atualizado com sucesso!");
        } else {
            System.out.println("\u26A0 Professor nao encontrado!");
        }
    }

    public void remover(String matricula) {
        Professor prof = buscarPorMatricula(matricula);
        if(prof != null) {
            professores.remove(prof);
            salvar();
            System.out.println("\u2705 Professor removido com sucesso!");
        } else {
            System.out.println("\u26A0 Professor nao encontrado!");
        }
    }

    private void salvar() {
        JsonUtil.salvar(professores, ARQUIVO);
    }

    private void carregar() {
        File file = new File(ARQUIVO);
        if (file.exists()) {
            professores = JsonUtil.carregarLista(ARQUIVO, ArrayList.class);
        } else {
            professores = new ArrayList<>();
        }
    }

    public void menuInterativo() {
        Scanner scanner = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("\n==== MENU PROFESSOR ====");
            System.out.println("1 - Cadastrar Professor");
            System.out.println("2 - Listar Professores");
            System.out.println("3 - Buscar por matrícula");
            System.out.println("4 - Atualizar professor");
            System.out.println("5 - Remover professor");
            System.out.println("0 - Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // consumir quebra de linha pendente

            switch (opcao) {
                case 1:
                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Disciplina: ");
                    String disciplina = scanner.nextLine();
                    System.out.print("Matricula: ");
                    String matricula = scanner.nextLine();
                    cadastrar(new Professor(nome, disciplina, matricula));
                    break;

                case 2:
                    for (Professor p : listarTodos()) {
                        System.out.println(p.getMatricula() + " | " + p.getNome() + " | " + p.getDisciplina());
                    }
                    break;

                case 3:
                    System.out.print("Matrícula: ");
                    String matBusca = scanner.nextLine();
                    Professor p = buscarPorMatricula(matBusca);
                    if (p != null) {
                        System.out.println("Nome: " + p.getNome() + ", Disciplina: " + p.getDisciplina());
                    } else {
                        System.out.println("Professor não encontrado.");
                    }
                    break;

                case 4:
                    System.out.print("Matrícula: ");
                    String matAtualiza = scanner.nextLine();
                    System.out.print("Novo nome: ");
                    String nomeAtualiza = scanner.nextLine();
                    System.out.print("Nova disciplina: ");
                    String disciplinaAtualiza = scanner.nextLine();
                    atualizar(matAtualiza, nomeAtualiza, disciplinaAtualiza);
                    break;

                case 5:
                    System.out.print("Matrícula: ");
                    String matRemove = scanner.nextLine();
                    remover(matRemove);
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