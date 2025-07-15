package services;

import com.google.gson.reflect.TypeToken;
import models.Disciplina;
import util.JsonUtil;
import dao.DisciplinaDAO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import models.Turma;

public class DisciplinaService {
    private static final String ARQUIVO = "disciplinas.json";
    private List<Disciplina> disciplinas;

    public DisciplinaService() {
        carregar();
    }

    public void cadastrar(Disciplina disciplina) {
        disciplinas.add(disciplina);
        salvar();
        // Persistência em SQLite
        DisciplinaDAO dao = new DisciplinaDAO();
        dao.inserir(disciplina);
        System.out.println("✅ Disciplina cadastrada com sucesso (JSON e SQLite)!");
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

    public void atualizar(String codigo, String novoCodigo, String novoNome, int novaCargaHoraria, String novaEmenta) {
        Disciplina disciplina = buscarPorCodigo(codigo);
        if (disciplina != null) {
            // Verificar se o novo código já existe (se for diferente do atual)
            if (!codigo.equalsIgnoreCase(novoCodigo)) {
                Disciplina disciplinaExistente = buscarPorCodigo(novoCodigo);
                if (disciplinaExistente != null) {
                    System.out.println("⚠️ Já existe uma disciplina com o código " + novoCodigo);
                    return;
                }
            }
            
            // Verificar se há turmas que usam esta disciplina
            ProfessorService professorService2 = new ProfessorService(null); // Passa null pois DisciplinaService não gerencia turmas
            TurmaService turmaService = new TurmaService(professorService2);
            List<Turma> turmasAfetadas = new ArrayList<>();
            for (Turma turma : turmaService.listarTodas()) {
                // Aqui você pode adicionar lógica para verificar se a turma usa esta disciplina
                // Por enquanto, vamos apenas notificar sobre a atualização
            }
            
            if (!turmasAfetadas.isEmpty()) {
                System.out.println("⚠️ Atenção: " + turmasAfetadas.size() + " turma(s) podem ser afetadas por esta atualização.");
                System.out.print("Deseja continuar? (s/n): ");
                Scanner scanner = new Scanner(System.in);
                String resposta = scanner.nextLine().toLowerCase();
                if (!resposta.equals("s") && !resposta.equals("sim")) {
                    System.out.println("Atualização cancelada.");
                    return;
                }
            }
            
            disciplina.setCodigo(novoCodigo);
            disciplina.setNome(novoNome);
            disciplina.setCargaHoraria(novaCargaHoraria);
            disciplina.setEmenta(novaEmenta);
            salvar();
            // Atualizar no banco de dados
            DisciplinaDAO dao = new DisciplinaDAO();
            dao.atualizar(codigo, novoNome, novaCargaHoraria, novaEmenta);
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
            try {
                disciplinas = JsonUtil.carregarLista(ARQUIVO, new TypeToken<List<Disciplina>>() {}.getType());
                if (disciplinas == null) {
                    disciplinas = new ArrayList<>();
                }
            } catch (Exception e) {
                System.err.println("Erro ao carregar arquivo de disciplinas. Criando nova lista.");
                disciplinas = new ArrayList<>();
                // Remover arquivo corrompido
                file.delete();
            }
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
                    System.out.println("Disciplinas já cadastradas:");
                    for (Disciplina disc : listarTodas()) {
                        System.out.println("- " + disc.getCodigo() + " | " + disc.getNome() + " | " + disc.getCargaHoraria() + "h");
                    }
                    System.out.print("Código: ");
                    String codigoCadastro = scanner.nextLine();
                    System.out.print("Nome da disciplina: ");
                    String nomeCadastro = scanner.nextLine();
                    System.out.print("Carga Horaria: ");
                    int cargaHoraria = Integer.parseInt(scanner.nextLine());
                    System.out.print("Ementa: ");
                    String ementa = scanner.nextLine();

                    cadastrar(new Disciplina(codigoCadastro, nomeCadastro, cargaHoraria, ementa));
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
                    System.out.println("Disciplinas cadastradas:");
                    for (Disciplina disc : listarTodas()) {
                        System.out.println("- " + disc.getCodigo() + " | " + disc.getNome() + " | " + disc.getCargaHoraria() + "h");
                    }
                    System.out.print("Código da disciplina a atualizar: ");
                    String codigoAtualiza = scanner.nextLine();
                    Disciplina disciplinaParaAtualizar = buscarPorCodigo(codigoAtualiza);
                    if (disciplinaParaAtualizar != null) {
                        System.out.println("Disciplina encontrada: " + disciplinaParaAtualizar);
                        System.out.print("Novo código (ou pressione Enter para manter o atual): ");
                        String novoCodigo = scanner.nextLine();
                        if (novoCodigo.trim().isEmpty()) {
                            novoCodigo = codigoAtualiza;
                        }
                        System.out.print("Novo nome (ou Enter para manter: " + disciplinaParaAtualizar.getNome() + "): ");
                        String nomeAtualiza = scanner.nextLine();
                        if (nomeAtualiza.trim().isEmpty()) {
                            nomeAtualiza = disciplinaParaAtualizar.getNome();
                        }
                        System.out.print("Nova carga horária (ou Enter para manter: " + disciplinaParaAtualizar.getCargaHoraria() + "h): ");
                        String cargaHorariaStr = scanner.nextLine();
                        int novaCargaHoraria = disciplinaParaAtualizar.getCargaHoraria();
                        if (!cargaHorariaStr.trim().isEmpty()) {
                            novaCargaHoraria = Integer.parseInt(cargaHorariaStr);
                        }
                        System.out.print("Nova ementa (ou Enter para manter: " + disciplinaParaAtualizar.getEmenta() + "): ");
                        String novaEmenta = scanner.nextLine();
                        if (novaEmenta.trim().isEmpty()) {
                            novaEmenta = disciplinaParaAtualizar.getEmenta();
                        }
                        atualizar(codigoAtualiza, novoCodigo, nomeAtualiza, novaCargaHoraria, novaEmenta);
                    } else {
                        System.out.println("⚠️ Disciplina não encontrada.");
                    }
                    break;
                
                case 5 :
                    System.out.print("Código: ");
                    String codigoRemove = scanner.nextLine();
                    remover(codigoRemove);
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
