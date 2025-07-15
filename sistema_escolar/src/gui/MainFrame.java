package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final services.ProfessorService professorService;
    private final services.TurmaService turmaService;
    public MainFrame(services.ProfessorService professorService, services.TurmaService turmaService) {
        this.professorService = professorService;
        this.turmaService = turmaService;
        setTitle(" Gestão Universitaria - Menu Principal");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton alunoBtn = new JButton("👨‍🎓 Gerenciar Alunos");
        alunoBtn.addActionListener(e -> new AlunoFrame());

        JButton professorBtn = new JButton("👨‍🏫 Gerenciar Professores");
        professorBtn.addActionListener(e -> new ProfessorFrame(professorService));

        JButton turmaBtn = new JButton("🏫 Gerenciar Turmas");
        turmaBtn.addActionListener(e -> new TurmaFrame(professorService));

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