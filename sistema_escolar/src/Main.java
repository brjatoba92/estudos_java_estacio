import services.AlunoService;
import services.ProfessorService;
import services.TurmaService;
import services.DisciplinaService;
import dao.AlunoDAO;
import dao.ProfessorDAO;
import dao.DisciplinaDAO;
import dao.TurmaDAO;
import gui.MainFrame;

import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Executar interface gráfica
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainFrame();
        });
        
        // Inicializar tabelas do banco de dados
        System.out.println("Inicializando banco de dados...");
        AlunoDAO alunoDAO = new AlunoDAO();
        alunoDAO.criarTabela();
        ProfessorDAO professorDAO = new ProfessorDAO();
        professorDAO.criarTabela();
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        disciplinaDAO.criarTabela();
        TurmaDAO turmaDAO = new TurmaDAO();
        turmaDAO.criarTabela();
        System.out.println("✅ Banco de dados inicializado!");

        Scanner scanner = new Scanner(System.in);

        AlunoService alunoService = new AlunoService();
        ProfessorService professorService = new ProfessorService();
        TurmaService turmaService = new TurmaService();
        DisciplinaService disciplinaService = new DisciplinaService();

        int opcao;
        do {
            System.out.println("\n==== SISTEMA ESCOLAR ====");
            System.out.println("1 - Menu de Alunos");
            System.out.println("2 - Menu de Professores");
            System.out.println("3 - Menu de Turmas");
            System.out.println("4 - Menu de Disciplinas");
            System.out.println("5 - Verificar dados do banco");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 : 
                    alunoService.menuInterativo();
                    break;
                case 2 : 
                    professorService.menuInterativo();
                    break;
                case 3 : 
                    turmaService.menuInterativo();
                    break;
                case 4 : 
                    disciplinaService.menuInterativo();
                    break;
                case 5:
                    verificarDadosDoBanco();
                    break;
                case 0 : 
                    System.out.println("Encerrando o sistema...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void verificarDadosDoBanco() {
        System.out.println("\n=== DADOS NO BANCO DE DADOS ===");
        
        // Verificar alunos
        System.out.println("\n--- ALUNOS ---");
        AlunoDAO alunoDAO = new AlunoDAO();
        List<models.Aluno> alunos = alunoDAO.listarTodos();
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno no banco.");
        } else {
            for (models.Aluno aluno : alunos) {
                System.out.println("- " + aluno.getMatricula() + " | " + aluno.getNome());
            }
        }
        
        // Verificar professores
        System.out.println("\n--- PROFESSORES ---");
        ProfessorDAO professorDAO = new ProfessorDAO();
        List<models.Professor> professores = professorDAO.listarTodos();
        if (professores.isEmpty()) {
            System.out.println("Nenhum professor no banco.");
        } else {
            for (models.Professor prof : professores) {
                System.out.println("- " + prof.getMatricula() + " | " + prof.getNome() + " | " + prof.getDisciplina());
            }
        }
        
        // Verificar disciplinas
        System.out.println("\n--- DISCIPLINAS ---");
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO();
        List<models.Disciplina> disciplinas = disciplinaDAO.listarTodos();
        if (disciplinas.isEmpty()) {
            System.out.println("Nenhuma disciplina no banco.");
        } else {
            for (models.Disciplina disc : disciplinas) {
                System.out.println("- " + disc.getCodigo() + " | " + disc.getNome() + " | " + disc.getCargaHoraria() + "h");
            }
        }
        
        // Verificar turmas
        System.out.println("\n--- TURMAS ---");
        TurmaDAO turmaDAO = new TurmaDAO();
        List<models.Turma> turmas = turmaDAO.listarTodos();
        if (turmas.isEmpty()) {
            System.out.println("Nenhuma turma no banco.");
        } else {
            for (models.Turma turma : turmas) {
                System.out.println("- " + turma.getCodigo() + " | " + turma.getSerie() + " | " + turma.getCargaHoraria() + "h");
            }
        }
        
        System.out.println("\n" + "=".repeat(50));
    }
}
