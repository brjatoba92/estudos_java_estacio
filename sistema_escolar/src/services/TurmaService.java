package services;

import com.google.gson.reflect.TypeToken;
import models.Professor;
import models.Turma;
import util.JsonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TurmaService {
    private static final String ARQUIVO = "turmas.json";
    private List<Turma> turmas;

    public TurmaService() {
        carregar();
    }

    public void cadastrar(Turma turma) {
        turmas.add(turma);
        salvar();
        System.out.println("\u2705 Turma cadastrada com sucesso!");
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

    public void atualizar(String codigo, String novaSerie) {
        Turma turma = buscarPorCodigo(codigo);
        if (turma != null) {
            turma = new Turma(codigo, novaSerie, turma.getProfessorResponsavel());
            salvar();
            System.out.println("\u2705 Turma atualizada com sucesso!");
        } else {
            System.out.println("\u26A0 Turma não encontrada!");
        }
    }

    public void remover(String codigo) {
        Turma turma = buscarPorCodigo(codigo);
        if (turma != null) {
            turmas.remove(turma);
            salvar();
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
            List<Turma> turmasList = JsonUtil.carregarLista(ARQUIVO, new com.google.gson.reflect.TypeToken<List<Turma>>(){}.getType());
            turmas = turmasList != null ? turmasList : new ArrayList<>();
        } else {
            turmas = new ArrayList<>();
        }
    }

    public void menuInterativo() {
        Scanner scanner = new Scanner(System.in);
        ProfessorService professorService = new ProfessorService(); // instanciando o service

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
                    System.out.print("Matrícula do Professor Responsável: ");
                    String matriculaProf = scanner.nextLine();

                    Professor professor = professorService.buscarPorMatricula(matriculaProf);
                    if (professor != null) {
                        cadastrar(new Turma(codigo, serie, professor));
                    } else {
                        System.out.println("\u26A0 Professor não encontrado. Turma não cadastrada.");
                    }
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
                    System.out.print("Código: ");
                    String codigoBusca4 = scanner.nextLine();
                    System.out.print("Nova Série: ");
                    String novaSerie = scanner.nextLine();
                    atualizar(codigoBusca4, novaSerie);
                    break;
                

                case 5 :
                    System.out.print("Código: ");
                    String codigoBusca5 = scanner.nextLine();
                    remover(codigoBusca5);
                    break;

                case 0 : 
                    System.out.println("Voltando ao menu principal...");
                    break;

                default : 
                    System.out.println("Opção inválida!");
                    break;
            }
        } while (opcao != 0);
    }
}
