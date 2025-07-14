package gui;

// AlunoFrame.java
import javax.swing.*;
import java.awt.*;
import services.AlunoService;
import models.Aluno;

public class AlunoFrame extends JFrame {
    private AlunoService alunoService = new AlunoService();

    public AlunoFrame() {
        setTitle("Alunos");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JButton cadastrarBtn = new JButton("Cadastrar Aluno");
        cadastrarBtn.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog("Nome:");
            String matricula = JOptionPane.showInputDialog("Matrícula:");
            String nascimento = JOptionPane.showInputDialog("Data de nascimento (aaaa-mm-dd):");
            alunoService.cadastrar(new Aluno(nome, matricula, nascimento));
        });

        JButton listarBtn = new JButton("Listar Alunos");
        listarBtn.addActionListener(e -> {
            JTextArea area = new JTextArea();
            for (Aluno a : alunoService.listarTodos()) {
                area.append(a.getMatricula() + " | " + a.getNome() + "\n");
            }
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Alunos Cadastrados", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel painel = new JPanel();
        painel.add(cadastrarBtn);
        painel.add(listarBtn);
        add(painel, BorderLayout.CENTER);
        setVisible(true);
    }
}
