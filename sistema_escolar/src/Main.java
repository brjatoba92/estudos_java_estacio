import models.*;
import util.JsonUtil;
import services.AlunoService;
import services.ProfessorService;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Escola escola = new Escola("Colégio Java", "12.345.678/0001-90");

        // Ajuste os parâmetros conforme o construtor da classe Professor
        Professor prof = new Professor("João Silva", "Matemática", "123456"); // Exemplo: adicionando um registro ou ID se necessário
        escola.adicionarProfessor(prof);

        Turma turma = new Turma("1A", "1º Ano", null); // Evita referência cíclica
        escola.adicionarTurma(turma);

        Aluno aluno1 = new Aluno("Maria Souza", "A001", "2008-06-01");
        Aluno aluno2 = new Aluno("Carlos Silva", "A002", "2009-08-15");

        escola.adicionarAluno(aluno1);
        escola.adicionarAluno(aluno2);

        turma.adicionarAluno(aluno1);
        turma.adicionarAluno(aluno2);

        Disciplina matematica = new Disciplina("Matemática", 60);
        Prova prova1 = new Prova("Prova Semestral 1", matematica, LocalDate.of(2025, 8, 15), 2.0);
        Prova prova2 = new Prova("Prova Semestral 2", matematica, LocalDate.of(2025, 12, 15), 3.0);

        AlunoService alunoService = new AlunoService();
        ProfessorService professorService = new ProfessorService();

        aluno1.adicionarNota(new Nota(8.5, prova1));
        aluno1.adicionarNota(new Nota(7.0, prova2));
        aluno2.adicionarNota(new Nota(6.0, prova1));
        aluno2.adicionarNota(new Nota(8.0, prova2));

        alunoService.menuInterativo();
        professorService.menuInterativo();

        // Salvar dados
        JsonUtil.salvar(escola, "escola.json");

        // Carregar e exibir
        Escola escolaCarregada = JsonUtil.carregarLista("escola.json", Escola.class);
        if (escolaCarregada != null) {
            escolaCarregada.listarAlunos();
        }
    }
}
