package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Sistema Escolar");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton alunoBtn = new JButton("Gerenciar Alunos");
        alunoBtn.addActionListener(e -> new AlunoFrame());

        JButton professorBtn = new JButton("Gerenciar Professor");
        professorBtn.addActionListener(e -> new ProfessorFrame());

        // Layout 
        setLayout(new GridLayout(3,1));
        add(alunoBtn);
        add(professorBtn);
        add(new JLabel("Versão inicial", SwingConstants.CENTER));

        setVisible(true);
    }
}