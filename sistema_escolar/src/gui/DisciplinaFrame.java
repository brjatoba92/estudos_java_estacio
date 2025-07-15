package gui;

// DisciplinaFrame.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import services.DisciplinaService;
import models.Disciplina;
import java.util.List;

public class DisciplinaFrame extends JFrame {
    private DisciplinaService disciplinaService = new DisciplinaService();

    public DisciplinaFrame() {
        setTitle("Gerenciamento de Disciplinas");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel principal com GridLayout
        JPanel mainPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Botões para todas as opções do menu
        mainPanel.add(createButton("📝 Cadastrar Disciplina", e -> cadastrarDisciplina()));
        mainPanel.add(createButton("📋 Listar Disciplinas", e -> listarDisciplinas()));
        mainPanel.add(createButton("🔍 Buscar por Código", e -> buscarDisciplina()));
        mainPanel.add(createButton("✏️ Atualizar Disciplina", e -> atualizarDisciplina()));
        mainPanel.add(createButton("🗑️ Remover Disciplina", e -> removerDisciplina()));

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(200, 50));
        button.addActionListener(action);
        return button;
    }

    private void cadastrarDisciplina() {
        JTextField codigoField = new JTextField();
        JTextField nomeField = new JTextField();
        JTextField cargaHorariaField = new JTextField();
        JTextArea ementaArea = new JTextArea(5, 30);
        JScrollPane ementaScroll = new JScrollPane(ementaArea);

        Object[] message = {
            "Código:", codigoField,
            "Nome:", nomeField,
            "Carga horária (horas):", cargaHorariaField,
            "Ementa:", ementaScroll
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Cadastrar Disciplina", 
                                                  JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String codigo = codigoField.getText().trim();
            String nome = nomeField.getText().trim();
            String cargaHorariaStr = cargaHorariaField.getText().trim();
            String ementa = ementaArea.getText().trim();

            if (codigo.isEmpty() || nome.isEmpty() || cargaHorariaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int cargaHoraria = Integer.parseInt(cargaHorariaStr);
                disciplinaService.cadastrar(new Disciplina(codigo, nome, cargaHoraria, ementa));
                JOptionPane.showMessageDialog(this, "✅ Disciplina cadastrada com sucesso!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Carga horária deve ser um número válido!", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void listarDisciplinas() {
        List<Disciplina> disciplinas = disciplinaService.listarTodas();
        if (disciplinas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma disciplina cadastrada.", 
                                        "Lista de Disciplinas", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Código | Nome | Carga Horária | Ementa\n");
        sb.append("-".repeat(80)).append("\n");
        for (Disciplina d : disciplinas) {
            sb.append(String.format("%s | %s | %dh\nEmenta: %s\n\n", 
                d.getCodigo(), d.getNome(), d.getCargaHoraria(), d.getEmenta() != null ? d.getEmenta() : ""));
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        JOptionPane.showMessageDialog(this, scrollPane, "Lista de Disciplinas", 
                                    JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscarDisciplina() {
        String codigo = JOptionPane.showInputDialog(this, "Digite o código da disciplina:");
        if (codigo != null && !codigo.trim().isEmpty()) {
            Disciplina disciplina = disciplinaService.buscarPorCodigo(codigo.trim());
            if (disciplina != null) {
                JOptionPane.showMessageDialog(this, 
                    "Código: " + disciplina.getCodigo() + "\n" +
                    "Nome: " + disciplina.getNome() + "\n" +
                    "Carga Horária: " + disciplina.getCargaHoraria() + "h\n" +
                    "Ementa: " + (disciplina.getEmenta() != null ? disciplina.getEmenta() : ""),
                    "Disciplina Encontrada", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Disciplina não encontrada.", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void atualizarDisciplina() {
        // Mostrar disciplinas cadastradas
        List<Disciplina> disciplinas = disciplinaService.listarTodas();
        if (disciplinas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma disciplina cadastrada para atualizar.", 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("Disciplinas cadastradas:\n");
        for (Disciplina disc : disciplinas) {
            sb.append("- ").append(disc.getCodigo()).append(" | ")
              .append(disc.getNome()).append(" | ")
              .append(disc.getCargaHoraria()).append("h\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Disciplinas Disponíveis", 
                                    JOptionPane.INFORMATION_MESSAGE);

        String codigo = JOptionPane.showInputDialog(this, "Digite o código da disciplina a atualizar:");
        if (codigo == null || codigo.trim().isEmpty()) return;

        Disciplina disciplina = disciplinaService.buscarPorCodigo(codigo.trim());
        if (disciplina == null) {
            JOptionPane.showMessageDialog(this, "Disciplina não encontrada!", 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField novoCodigoField = new JTextField(disciplina.getCodigo());
        JTextField novoNomeField = new JTextField(disciplina.getNome());
        JTextField novaCargaHorariaField = new JTextField(String.valueOf(disciplina.getCargaHoraria()));
        JTextArea novaEmentaArea = new JTextArea(disciplina.getEmenta() != null ? disciplina.getEmenta() : "", 5, 30);
        JScrollPane novaEmentaScroll = new JScrollPane(novaEmentaArea);

        Object[] message = {
            "Novo código:", novoCodigoField,
            "Novo nome:", novoNomeField,
            "Nova carga horária:", novaCargaHorariaField,
            "Nova ementa:", novaEmentaScroll
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Atualizar Disciplina", 
                                                  JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String novoCodigo = novoCodigoField.getText().trim();
            String novoNome = novoNomeField.getText().trim();
            String novaCargaHorariaStr = novaCargaHorariaField.getText().trim();
            String novaEmenta = novaEmentaArea.getText().trim();

            if (novoCodigo.isEmpty() || novoNome.isEmpty() || novaCargaHorariaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int novaCargaHoraria = Integer.parseInt(novaCargaHorariaStr);
                disciplinaService.atualizar(codigo.trim(), novoCodigo, novoNome, novaCargaHoraria, novaEmenta);
                JOptionPane.showMessageDialog(this, "✅ Disciplina atualizada com sucesso!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Carga horária deve ser um número válido!", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removerDisciplina() {
        String codigo = JOptionPane.showInputDialog(this, "Digite o código da disciplina a remover:");
        if (codigo != null && !codigo.trim().isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja remover a disciplina?", "Confirmar Remoção", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                disciplinaService.remover(codigo.trim());
                JOptionPane.showMessageDialog(this, "✅ Disciplina removida com sucesso!");
            }
        }
    }
} 