package gui;

// ProfessorFrame.java
import javax.swing.*;
import java.awt.*;
import services.ProfessorService;
import models.Professor;

public class ProfessorFrame extends JFrame {
    private ProfessorService professorService = new ProfessorService();

    public ProfessorFrame() {
        setTitle("Professores");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JButton cadastrarBtn = new JButton("Cadastrar Professor");
        cadastrarBtn.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog("Nome:");
            String matricula = JOptionPane.showInputDialog("Matrícula:");
            String disciplina = JOptionPane.showInputDialog("Disciplina:");
            professorService.cadastrar(new Professor(nome, matricula, disciplina));
        });

        JButton listarBtn = new JButton("Listar Professores");
        listarBtn.addActionListener(e -> {
            JTextArea area = new JTextArea();
            for (Professor p : professorService.listarTodos()) {
                area.append(p.getMatricula() + " | " + p.getNome() + " | " + p.getDisciplina() + "\n");
            }
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Professores Cadastrados", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel painel = new JPanel();
        painel.add(cadastrarBtn);
        painel.add(listarBtn);
        add(painel, BorderLayout.CENTER);
        setVisible(true);
    }
}
