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
import dao.TurmaDAO;
import java.util.stream.Collectors;

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
        mainPanel.add(createButton("💵 Quanto Ganhou?", e -> quantoGanhou()));

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
        JTextField nomeField = new JTextField();
        JTextField matriculaField = new JTextField();
        JTextField valorHoraField = new JTextField();

        // Seleção múltipla de disciplinas
        java.util.List<Disciplina> disciplinas = disciplinaService.listarTodas();
        if (disciplinas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma disciplina cadastrada. Cadastre uma disciplina primeiro.",
                                        "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] opcoesDisciplinas = disciplinas.stream()
            .map(d -> d.getCodigo() + " - " + d.getNome())
            .toArray(String[]::new);
        JList<String> listaDisciplinas = new JList<>(opcoesDisciplinas);
        listaDisciplinas.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollDisciplinas = new JScrollPane(listaDisciplinas);
        scrollDisciplinas.setPreferredSize(new Dimension(300, 80));

        Object[] message = {
            "Nome:", nomeField,
            "Matrícula:", matriculaField,
            "Valor por hora:", valorHoraField,
            "Selecione as disciplinas:", scrollDisciplinas
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Cadastrar Professor",
                                                  JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nome = nomeField.getText().trim();
            String matricula = matriculaField.getText().trim();
            String valorHoraStr = valorHoraField.getText().trim();
            java.util.List<String> disciplinasSelecionadas = new java.util.ArrayList<>();
            for (int idx : listaDisciplinas.getSelectedIndices()) {
                String nomeDisc = disciplinas.get(idx).getNome();
                disciplinasSelecionadas.add(nomeDisc);
            }

            if (nome.isEmpty() || matricula.isEmpty() || valorHoraStr.isEmpty() || disciplinasSelecionadas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios e pelo menos uma disciplina deve ser selecionada!",
                                            "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double valorHora = Double.parseDouble(valorHoraStr);
                professorService.cadastrar(new Professor(nome, matricula, disciplinasSelecionadas, valorHora));
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
        sb.append("Matrícula | Nome | Disciplinas | Valor/Hora | Total de Turmas | Valor Total\n");
        sb.append("-".repeat(120)).append("\n");

        for (Professor p : professores) {
            sb.append(String.format("%s | %s | %s | R$ %.2f/h | %d | R$ %.2f\n",
                p.getMatricula(), p.getNome(), String.join(", ", p.getDisciplinas()), p.getValorHora(), p.getTotalTurmas(), p.calcularValorTotalSimples()));
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "Lista de Professores",
                                    JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscarProfessor() {
        String matricula = JOptionPane.showInputDialog(this, "Digite a matrícula:");
        if (matricula != null && !matricula.trim().isEmpty()) {
            Professor professor = professorService.buscarPorMatricula(matricula.trim());
            if (professor != null) {
                JOptionPane.showMessageDialog(this,
                    "Nome: " + professor.getNome() +
                    "\nDisciplinas: " + String.join(", ", professor.getDisciplinas()) +
                    "\nValor/Hora: R$ " + String.format("%.2f", professor.getValorHora()) +
                    "\nTotal de turmas: " + professor.getTotalTurmas() +
                    "\nValor total: R$ " + String.format("%.2f", professor.calcularValorTotalSimples()),
                    "Professor Encontrado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Professor não encontrado.",
                                            "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void atualizarProfessor() {
        java.util.List<Professor> professores = professorService.listarTodos();
        if (professores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum professor cadastrado.",
                                        "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] opcoes = professores.stream()
            .map(p -> p.getMatricula() + " - " + p.getNome() + " (" + String.join(", ", p.getDisciplinas()) + ")")
            .toArray(String[]::new);
        String selecionado = (String) JOptionPane.showInputDialog(this,
            "Selecione o professor para atualizar:", "Atualizar Professor",
            JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (selecionado == null) return;
        String matricula = selecionado.split(" - ")[0];

        Professor professor = professorService.buscarPorMatricula(matricula.trim());
        if (professor == null) {
            JOptionPane.showMessageDialog(this, "Professor não encontrado!",
                                        "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField novoNomeField = new JTextField(professor.getNome());
        JTextField novoValorHoraField = new JTextField(String.valueOf(professor.getValorHora()));

        // Seleção múltipla de disciplinas
        java.util.List<Disciplina> disciplinas = disciplinaService.listarTodas();
        String[] opcoesDisciplinas = disciplinas.stream()
            .map(d -> d.getCodigo() + " - " + d.getNome())
            .toArray(String[]::new);
        JList<String> listaDisciplinas = new JList<>(opcoesDisciplinas);
        listaDisciplinas.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // Selecionar as disciplinas já associadas
        java.util.List<String> nomesDisciplinasProf = professor.getDisciplinas();
        java.util.List<Integer> indicesSelecionados = new java.util.ArrayList<>();
        for (int i = 0; i < disciplinas.size(); i++) {
            if (nomesDisciplinasProf.contains(disciplinas.get(i).getNome())) {
                indicesSelecionados.add(i);
            }
        }
        int[] indicesArray = indicesSelecionados.stream().mapToInt(Integer::intValue).toArray();
        listaDisciplinas.setSelectedIndices(indicesArray);
        JScrollPane scrollDisciplinas = new JScrollPane(listaDisciplinas);
        scrollDisciplinas.setPreferredSize(new Dimension(300, 80));

        Object[] message = {
            "Novo nome:", novoNomeField,
            "Novo valor por hora:", novoValorHoraField,
            "Selecione as disciplinas:", scrollDisciplinas
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Atualizar Professor",
                                                  JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String novoNome = novoNomeField.getText().trim();
            String novoValorHoraStr = novoValorHoraField.getText().trim();
            java.util.List<String> disciplinasSelecionadas = new java.util.ArrayList<>();
            for (int idx : listaDisciplinas.getSelectedIndices()) {
                String nomeDisc = disciplinas.get(idx).getNome();
                disciplinasSelecionadas.add(nomeDisc);
            }

            if (novoNome.isEmpty() || novoValorHoraStr.isEmpty() || disciplinasSelecionadas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios e pelo menos uma disciplina deve ser selecionada!",
                                            "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double novoValorHora = Double.parseDouble(novoValorHoraStr);
                professorService.atualizar(matricula.trim(), novoNome, disciplinasSelecionadas, novoValorHora);
                JOptionPane.showMessageDialog(this, "✅ Professor atualizado com sucesso!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Valor por hora deve ser um número válido!",
                                            "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removerProfessor() {
        java.util.List<Professor> professores = professorService.listarTodos();
        if (professores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum professor cadastrado.",
                                        "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] opcoes = professores.stream()
            .map(p -> p.getMatricula() + " - " + p.getNome() + " (" + p.getDisciplina() + ")")
            .toArray(String[]::new);
        String selecionado = (String) JOptionPane.showInputDialog(this,
            "Selecione o professor para remover:", "Remover Professor",
            JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (selecionado == null) return;
        String matricula = selecionado.split(" - ")[0];

        int confirm = JOptionPane.showConfirmDialog(this,
            "Tem certeza que deseja remover o professor?", "Confirmar Remoção",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            professorService.remover(matricula.trim());
            JOptionPane.showMessageDialog(this, "✅ Professor removido com sucesso!");
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
            sb.append("Disciplinas: ").append(String.join(", ", professor.getDisciplinas())).append("\n");
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

    private void quantoGanhou() {
        // Garante que as turmas sejam associadas aos professores
        // new services.TurmaService(); // Não é mais necessário
        java.util.List<models.Professor> professores = professorService.listarTodos();
        if (professores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum professor cadastrado.",
                                        "Quanto Ganhou", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] opcoes = professores.stream()
            .map(p -> p.getMatricula() + " - " + p.getNome() + " (" + p.getDisciplina() + ")")
            .toArray(String[]::new);
        String selecionado = (String) JOptionPane.showInputDialog(this,
            "Selecione o professor:", "Quanto Ganhou",
            JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (selecionado == null) return;
        String matricula = selecionado.split(" - ")[0];
        models.Professor professor = professores.stream()
            .filter(p -> p.getMatricula().equals(matricula))
            .findFirst().orElse(null);
        if (professor == null) {
            JOptionPane.showMessageDialog(this, "Professor não encontrado!",
                                        "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Buscar turmas do professor diretamente do banco
        TurmaDAO turmaDAO = new TurmaDAO();
        java.util.List<models.Turma> turmasDoProfessor = turmaDAO.listarTodos().stream()
            .filter(t -> t.getMatriculaProfessor() != null && t.getMatriculaProfessor().equals(professor.getMatricula()))
            .collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        sb.append("Professor: ").append(professor.getNome()).append(" (" + professor.getMatricula() + ")\n");
        sb.append("Disciplina: ").append(professor.getDisciplina()).append("\n");
        sb.append("Valor por hora: R$ ").append(String.format("%.2f", professor.getValorHora())).append("\n\n");
        sb.append("Turmas:");
        double total = 0.0;
        for (models.Turma turma : turmasDoProfessor) {
            double valorTurma = turma.getCargaHoraria() * professor.getValorHora();
            sb.append("\n- ").append(turma.getCodigo()).append(" (" + turma.getSerie() + ")");
            sb.append(", Carga Horária: ").append(turma.getCargaHoraria()).append("h");
            sb.append(", Valor: R$ ").append(String.format("%.2f", valorTurma));
            total += valorTurma;
        }
        sb.append("\n\nTotal de turmas: ").append(turmasDoProfessor.size());
        sb.append("\n\nTOTAL RECEBIDO: R$ ").append(String.format("%.2f", total));
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));
        JOptionPane.showMessageDialog(this, scrollPane, "Quanto Ganhou", JOptionPane.INFORMATION_MESSAGE);
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
