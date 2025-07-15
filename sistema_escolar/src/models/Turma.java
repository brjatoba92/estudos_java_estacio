package models;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;

public class Turma {
    @Expose
    private String codigo;
    @Expose
    private String serie;
    @Expose
    private String matriculaProfessor;
    @Expose
    private int cargaHoraria;
    @Expose
    private String codigoDisciplina;
    private transient Professor professorResponsavel;
    private transient List<Aluno> alunos;

    public Turma(String codigo, String serie, Professor professorResponsavel, String codigoDisciplina) {
        this.codigo = codigo;
        this.serie = serie;
        this.professorResponsavel = professorResponsavel;
        this.matriculaProfessor = professorResponsavel != null ? professorResponsavel.getMatricula() : null;
        this.cargaHoraria = 0;
        this.codigoDisciplina = codigoDisciplina;
        this.alunos = new ArrayList<>();
    }

    public Turma(String codigo, String serie, Professor professorResponsavel, int cargaHoraria, String codigoDisciplina) {
        this.codigo = codigo;
        this.serie = serie;
        this.professorResponsavel = professorResponsavel;
        this.matriculaProfessor = professorResponsavel != null ? professorResponsavel.getMatricula() : null;
        this.cargaHoraria = cargaHoraria;
        this.codigoDisciplina = codigoDisciplina;
        this.alunos = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getSerie() {
        return serie;
    }

    public Professor getProfessorResponsavel() {
        return professorResponsavel;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getMatriculaProfessor() {
        return matriculaProfessor;
    }

    public void setProfessorResponsavel(Professor professor) {
        this.professorResponsavel = professor;
        this.matriculaProfessor = professor != null ? professor.getMatricula() : null;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void adicionarAluno(Aluno aluno) {
        if (!alunos.contains(aluno)) {
            alunos.add(aluno);
            aluno.setTurma(this);
            System.out.println("Aluno " + aluno.getNome() + " adicionado à turma " + codigo);
        } else {
            System.out.println("Aluno já está na turma.");
        }
    }

    public void listarAlunos() {
        System.out.println("Alunos da Turma " + codigo + " - " + serie);
        for (Aluno aluno : alunos) {
            System.out.println("- " + aluno.getNome() + " (Matrícula: " + aluno.getMatricula() + ")");
        }
    }

    public String getCodigoDisciplina() {
        return codigoDisciplina;
    }
    public void setCodigoDisciplina(String codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
    }

    @Override
    public String toString() {
        String codigoTurma = codigo != null ? codigo : "Código não informado";
        String serieTurma = serie != null ? serie : "Série não informada";
        String professorNome = "Não atribuído";
        
        if (professorResponsavel != null && professorResponsavel.getNome() != null) {
            professorNome = professorResponsavel.getNome();
        }
        
        return "Turma " + codigoTurma + " - " + serieTurma + 
               " | Professor: " + professorNome + 
               " | Carga Horária: " + cargaHoraria + "h" +
               " | Alunos: " + (alunos != null ? alunos.size() : 0);
    }
}
