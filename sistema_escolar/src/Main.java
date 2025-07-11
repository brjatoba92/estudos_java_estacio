import models.*;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // Criando professores
        Professor prof = new Professor("João Silva", "Matemática");

        // Criando turmas
        Turma turma = new Turma("1A", "1º Ano", prof);

        // Criando alunos
        Aluno aluno1 = new Aluno("Maria Souza", "A001", "2024-06-01");
        Aluno aluno2 = new Aluno("Carlos Silva", "A002", "2024-08-15");

        // Criando disciplinas
        Disciplina matematica = new Disciplina("Matemática", 60);
        Disciplina fisica = new Disciplina("Física", 45);

        // Criando prova
        Prova prova1 = new Prova("Prova Bimestral", matematica, LocalDate.of(2025, 8, 15), 2.0);

        // Criar uma nota e adicionar ao aluno
        Nota nota1 = new Nota(8.5, prova1);
        aluno1.adicionarNota(nota1);

        // Vincular alunos à turma
        turma.adicionarAluno(aluno1);
        turma.adicionarAluno(aluno2);
        
        // Listar alunos da turma
        turma.listarAlunos();

        // Mostra notas e media
        aluno1.exibirBoletim();
    }
}
