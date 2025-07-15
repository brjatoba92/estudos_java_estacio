package gui;

// ProfessorFrame.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import services.ProfessorService;
import services.DisciplinaService;
import models.Professor;
import models.Disciplina;
import java.util.List;

public class ProfessorFrame extends JFrame {
    private ProfessorService professorService = new ProfessorService();
    private DisciplinaService disciplinaService = new DisciplinaService();

    public ProfessorFrame() {
        setTitle("Gerenciamento de Professores");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel principal com GridLayout
        JPanel mainPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Botões para todas as opções do menu
        mainPanel.add(createButton("📝 Cadastrar Professor", e -> cadastrarProfessor()));
        mainPanel.add(createButton("📋 Listar Professores", e -> listarProfessores()));
        mainPanel.add(createButton("🔍 Buscar por Matrícula", e -> buscarProfessor()));
        mainPanel.add(createButton("✏️ Atualizar Professor", e -> atualizarProfessor()));
        mainPanel.add(createButton("🗑️ Remover Professor", e -> removerProfessor()));
        mainPanel.add(createButton("💰 Relatório Financeiro", e -> relatorioFinanceiro()));

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

    private void cadastrarProfessor() {
        // Mostrar professores já cadastrados
        List<Professor> professores = professorService.listarTodos();
        if (!professores.isEmpty()) {
            StringBuilder sb = new StringBuilder("Matrículas já cadastradas:\n");
            for (Professor prof : professores) {
                sb.append("- ").append(prof.getMatricula()).append(" | ").append(prof.getNome()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Professores Existentes", 
                                        JOptionPane.INFORMATION_MESSAGE);
        }

        JTextField nomeField = new JTextField();
        JTextField matriculaField = new JTextField();
        JTextField valorHoraField = new JTextField();

        Object[] message = {
            "Nome:", nomeField,
            "Matrícula:", matriculaField,
            "Valor por hora:", valorHoraField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Cadastrar Professor", 
                                                  JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nome = nomeField.getText().trim();
            String matricula = matriculaField.getText().trim();
            String valorHoraStr = valorHoraField.getText().trim();

            if (nome.isEmpty() || matricula.isEmpty() || valorHoraStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double valorHora = Double.parseDouble(valorHoraStr);
                
                // Selecionar disciplina
                Disciplina disciplina = selecionarDisciplina();
                if (disciplina == null) return;

                professorService.cadastrar(new Professor(nome, matricula, disciplina.getNome(), valorHora));
                JOptionPane.showMessageDialog(this, "✅ Professor cadastrado com sucesso!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Valor por hora deve ser um número válido!", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void listarProfessores() {
        List<Professor> professores = professorService.listarTodos();
        if (professores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum professor cadastrado.", 
                                        "Lista de Professores", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Matrícula | Nome | Disciplina | Valor/Hora\n");
        sb.append("-".repeat(60)).append("\n");
        
        for (Professor p : professores) {
            sb.append(String.format("%s | %s | %s | R$ %.2f/h\n", 
                p.getMatricula(), p.getNome(), p.getDisciplina(), p.getValorHora()));
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Lista de Professores", 
                                    JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscarProfessor() {
        String matricula = JOptionPane.showInputDialog(this, "Digite a matrícula:");
        if (matricula != null && !matricula.trim().isEmpty()) {
            Professor professor = professorService.buscarPorMatricula(matricula.trim());
            if (professor != null) {
                JOptionPane.showMessageDialog(this, 
                    "Nome: " + professor.getNome() + "\nDisciplina: " + professor.getDisciplina(),
                    "Professor Encontrado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Professor não encontrado.", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void atualizarProfessor() {
        String matricula = JOptionPane.showInputDialog(this, "Digite a matrícula:");
        if (matricula == null || matricula.trim().isEmpty()) return;

        Professor professor = professorService.buscarPorMatricula(matricula.trim());
        if (professor == null) {
            JOptionPane.showMessageDialog(this, "Professor não encontrado!", 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField novoNomeField = new JTextField(professor.getNome());
        JTextField novoValorHoraField = new JTextField(String.valueOf(professor.getValorHora()));

        // Mostrar disciplinas disponíveis
        List<Disciplina> disciplinas = disciplinaService.listarTodas();
        if (disciplinas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma disciplina cadastrada. Cadastre uma disciplina primeiro.", 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("Disciplinas disponíveis:\n");
        for (int i = 0; i < disciplinas.size(); i++) {
            sb.append((i+1)).append(" - ").append(disciplinas.get(i).getNome()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Disciplinas Disponíveis", 
                                    JOptionPane.INFORMATION_MESSAGE);

        // Selecionar nova disciplina
        String[] opcoesDisciplinas = disciplinas.stream()
            .map(d -> d.getCodigo() + " - " + d.getNome())
            .toArray(String[]::new);

        String disciplinaSelecionada = (String) JOptionPane.showInputDialog(this, 
            "Selecione a nova disciplina (ou clique Cancelar para manter a atual):", 
            "Selecionar Disciplina", JOptionPane.QUESTION_MESSAGE, null, opcoesDisciplinas, null);

        String novaDisciplina = professor.getDisciplina(); // manter atual por padrão
        if (disciplinaSelecionada != null) {
            String codigoDisciplina = disciplinaSelecionada.split(" - ")[0];
            Disciplina disciplina = disciplinas.stream()
                .filter(d -> d.getCodigo().equals(codigoDisciplina))
                .findFirst().orElse(null);
            if (disciplina != null) {
                novaDisciplina = disciplina.getNome();
            }
        }

        Object[] message = {
            "Novo nome:", novoNomeField,
            "Nova disciplina: " + novaDisciplina,
            "Novo valor por hora:", novoValorHoraField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Atualizar Professor", 
                                                  JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String novoNome = novoNomeField.getText().trim();
            String novoValorHoraStr = novoValorHoraField.getText().trim();

            if (novoNome.isEmpty() || novoValorHoraStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double novoValorHora = Double.parseDouble(novoValorHoraStr);
                professorService.atualizar(matricula.trim(), novoNome, novaDisciplina, novoValorHora);
                JOptionPane.showMessageDialog(this, "✅ Professor atualizado com sucesso!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Valor por hora deve ser um número válido!", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removerProfessor() {
        String matricula = JOptionPane.showInputDialog(this, "Digite a matrícula do professor a remover:");
        if (matricula != null && !matricula.trim().isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja remover o professor?", "Confirmar Remoção", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                professorService.remover(matricula.trim());
                JOptionPane.showMessageDialog(this, "✅ Professor removido com sucesso!");
            }
        }
    }

    private void relatorioFinanceiro() {
        List<Professor> professores = professorService.listarTodos();
        if (professores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum professor cadastrado para gerar relatório.", 
                                        "Relatório Financeiro", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== RELATÓRIO FINANCEIRO GERAL ===\n\n");
        
        double valorTotalGeral = 0.0;
        for (Professor professor : professores) {
            double valorProfessor = professor.calcularValorTotal();
            valorTotalGeral += valorProfessor;
            
            sb.append(professor.getNome()).append(" (").append(professor.getMatricula()).append(")\n");
            sb.append("Disciplina: ").append(professor.getDisciplina()).append("\n");
            sb.append("Valor por hora: R$ ").append(String.format("%.2f", professor.getValorHora())).append("\n");
            sb.append("Total de turmas: ").append(professor.getTurmas().size()).append("\n");
            sb.append("Valor total: R$ ").append(String.format("%.2f", valorProfessor)).append("\n\n");
        }
        
        sb.append("=".repeat(50)).append("\n");
        sb.append("VALOR TOTAL GERAL: R$ ").append(String.format("%.2f", valorTotalGeral));

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Relatório Financeiro", 
                                    JOptionPane.INFORMATION_MESSAGE);
    }

    private Disciplina selecionarDisciplina() {
        List<Disciplina> disciplinas = disciplinaService.listarTodas();
        if (disciplinas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma disciplina cadastrada. Cadastre uma disciplina primeiro.", 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String[] opcoes = disciplinas.stream()
            .map(d -> d.getCodigo() + " - " + d.getNome())
            .toArray(String[]::new);

        String selecionada = (String) JOptionPane.showInputDialog(this, 
            "Selecione a disciplina:", "Selecionar Disciplina", 
            JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);

        if (selecionada == null) return null;

        String codigo = selecionada.split(" - ")[0];
        return disciplinas.stream()
            .filter(d -> d.getCodigo().equals(codigo))
            .findFirst().orElse(null);
    }
}
