import models.*;

public class Main {
    public static void main(String[] args) {
        Professor prof = new Professor("João Silva", "Matematica");
        Turma turma1 = new Turma("1A", "1° Ano", prof);
        Turma turma2 = new Turma("2B", "2° Ano", prof);

        Aluno aluno1 - new Aluno("Maria Souza", "A001", "2024-06-01");
        Aluno aluno2 - new Aluno("Carlos Silva", "A002", "2024-08-15");

        Disciplina matematica = new Disciplina("Matematica", 60);
        Disciplina fisica = new Disciplina("Fisica", 40);

        // Vincular alunos a turmas
        turma.adcionarAluno(aluno1);
        turma.adcionarAluno(aluno2);

        turma.listarAlunos();
        
        // Vincular as turmas ao professor
        prof.adcionarTurma(turma1);
        prof.adcionarTurma(turma2);

        prof.listarTurmas();

        // Exibir informações das disciplinas
        matematica.exibirInfo();
        fisica.exibirInfo();
    }
}