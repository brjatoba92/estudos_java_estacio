import services.AlunoService;
import services.ProfessorService;
import services.TurmaService;
import services.DisciplinaService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 : alunoService.menuInterativo();
                case 2 : professorService.menuInterativo();
                case 3 : turmaService.menuInterativo();
                case 4 : disciplinaService.menuInterativo();
                case 0 : System.out.println("Encerrando o sistema...");
                default : System.out.println("⚠️ Opção inválida!");
            }
        } while (opcao != 0);
    }
}
