package services;

import com.google.gson.reflect.TypeToken;
import models.Disciplina;
import util.JsonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DisciplinaService {
    private static final String ARQUIVO = "disciplinas.json";
    private List<Disciplina> disciplinas;

    public DisciplinaService() {
        carregar();
    }

    public void cadastrar(Disciplina disciplina) {
        disciplinas.add(disciplina);
        salvar();
        System.out.println("✅ Disciplina cadastrada com sucesso!");
    }

    public List<Disciplina> listarTodas() {
        return disciplinas;
    }

    public Disciplina buscarPorCodigo(String codigo) {
        for (Disciplina d : disciplinas) {
            if (d.getCodigo().equalsIgnoreCase(codigo)) {
                return d;
            }
        }
        return null;
    }

    public void atualizar(String codigo, String novoNome) {
        Disciplina disciplina = buscarPorCodigo(codigo);
        if (disciplina != null) {
            disciplina.setNome(novoNome);
            salvar();
            System.out.println("✅ Disciplina atualizada com sucesso!");
        } else {
            System.out.println("⚠️ Disciplina não encontrada.");
        }
    }

    public void remover(String codigo) {
        Disciplina disciplina = buscarPorCodigo(codigo);
        if (disciplina != null) {
            disciplinas.remove(disciplina);
            salvar();
            System.out.println("✅ Disciplina removida com sucesso!");
        } else {
            System.out.println("⚠️ Disciplina não encontrada.");
        }
    }

    private void salvar() {
        JsonUtil.salvar(disciplinas, ARQUIVO);
    }

    private void carregar() {
        File file = new File(ARQUIVO);
        if (file.exists()) {
            disciplinas = JsonUtil.carregarLista(ARQUIVO, new TypeToken<List<Disciplina>>() {}.getType());
        } else {
            disciplinas = new ArrayList<>();
        }
    }

    public void menuInterativo() {
        Scanner scanner = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("\n==== MENU DISCIPLINA ====");
            System.out.println("1 - Cadastrar disciplina");
            System.out.println("2 - Listar disciplinas");
            System.out.println("3 - Buscar disciplina por código");
            System.out.println("4 - Atualizar disciplina");
            System.out.println("5 - Remover disciplina");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 :
                    System.out.print("Código: ");
                    String codigoCadastro = scanner.nextLine();
                    System.out.print("Nome da disciplina: ");
                    String nomeCadastro = scanner.nextLine();
                    System.out.print("Carga Horaria: ");
                    int cargaHoraria = Integer.parseInt(scanner.nextLine());

                    cadastrar(new Disciplina(codigoCadastro, nomeCadastro, cargaHoraria));
                    break;
                
                case 2 :
                    listarTodas().forEach(System.out::println);
                    break;
                
                case 3 :
                    System.out.print("Código: ");
                    String codigoBusca = scanner.nextLine();
                    Disciplina d = buscarPorCodigo(codigoBusca);
                    if (d != null) {
                        System.out.println("Nome: " + d.getNome());
                    } else {
                        System.out.println("⚠️ Disciplina não encontrada.");
                    }
                    break;
                
                case 4 :
                    System.out.print("Código: ");
                    String codigoAtualiza = scanner.nextLine();
                    System.out.print("Novo nome: ");
                    String nomeAtualiza = scanner.nextLine();
                    atualizar(codigoAtualiza, nomeAtualiza);
                    break;
                
                case 5 :
                    System.out.print("Código: ");
                    String codigoRemove = scanner.nextLine();
                    remover(codigoRemove);
                    break;
                case 0 : System.out.println("Voltando..."); break;
                default : System.out.println("⚠️ Opção inválida!");
            }
        } while (opcao != 0);
    }
}
