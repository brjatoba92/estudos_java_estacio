package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Sistema Escolar - Menu Principal");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton alunoBtn = new JButton("👨‍🎓 Gerenciar Alunos");
        alunoBtn.addActionListener(e -> new AlunoFrame());

        JButton professorBtn = new JButton("👨‍🏫 Gerenciar Professores");
        professorBtn.addActionListener(e -> new ProfessorFrame());

        JButton turmaBtn = new JButton("🏫 Gerenciar Turmas");
        turmaBtn.addActionListener(e -> new TurmaFrame());

        JButton disciplinaBtn = new JButton("📚 Gerenciar Disciplinas");
        disciplinaBtn.addActionListener(e -> new DisciplinaFrame());

        // Layout 
        JPanel mainPanel = new JPanel(new GridLayout(2,2,10,10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(alunoBtn);
        mainPanel.add(professorBtn);
        mainPanel.add(turmaBtn);
        mainPanel.add(disciplinaBtn);
        
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }
}