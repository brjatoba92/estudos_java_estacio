package gui;

// TurmaFrame.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import services.TurmaService;
import services.ProfessorService;
import models.Turma;
import models.Professor;
import java.util.List;
import services.DisciplinaService;

public class TurmaFrame extends JFrame {
    private TurmaService turmaService;
    private ProfessorService professorService;
    private DisciplinaService disciplinaService = new DisciplinaService();

    public TurmaFrame(ProfessorService professorService) {
        this.professorService = professorService;
        this.turmaService = new TurmaService(professorService);
        setTitle("Gerenciamento de Turmas");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel principal com GridLayout
        JPanel mainPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Botões para todas as opções do menu
        mainPanel.add(createButton("📝 Cadastrar Turma", e -> cadastrarTurma()));
        mainPanel.add(createButton("📋 Listar Turmas", e -> listarTurmas()));
        mainPanel.add(createButton("🔍 Buscar por Código", e -> buscarTurma()));
        mainPanel.add(createButton("✏️ Atualizar Turma", e -> atualizarTurma()));
        mainPanel.add(createButton("🗑️ Remover Turma", e -> removerTurma()));

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

    private void cadastrarTurma() {
        JTextField codigoField = new JTextField();
        JTextField serieField = new JTextField();
        JTextField cargaHorariaField = new JTextField();

        // Seleção de disciplina
        java.util.List<models.Disciplina> disciplinas = disciplinaService.listarTodas();
        if (disciplinas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma disciplina cadastrada. Cadastre uma disciplina primeiro.",
                                        "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String[] opcoesDisciplinas = disciplinas.stream()
            .map(d -> d.getCodigo() + " - " + d.getNome())
            .toArray(String[]::new);
        JComboBox<String> comboDisciplinas = new JComboBox<>(opcoesDisciplinas);

        Object[] message = {
            "Código:", codigoField,
            "Série:", serieField,
            "Carga horária (horas por semana):", cargaHorariaField,
            "Disciplina:", comboDisciplinas
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Cadastrar Turma", 
                                                  JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String codigo = codigoField.getText().trim();
            String serie = serieField.getText().trim();
            String cargaHorariaStr = cargaHorariaField.getText().trim();
            String codigoDisciplina = disciplinas.get(comboDisciplinas.getSelectedIndex()).getCodigo();

            if (codigo.isEmpty() || serie.isEmpty() || cargaHorariaStr.isEmpty() || codigoDisciplina.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int cargaHoraria = Integer.parseInt(cargaHorariaStr);
                // Selecionar professor
                Professor professor = selecionarProfessor();
                if (professor == null) return;
                Turma novaTurma = new Turma(codigo, serie, professor, cargaHoraria, codigoDisciplina);
                turmaService.cadastrar(novaTurma);
                professor.adicionarTurma(novaTurma);
                professorService.salvar();
                JOptionPane.showMessageDialog(this, "✅ Turma cadastrada com sucesso!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Carga horária deve ser um número válido!", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void listarTurmas() {
        List<Turma> turmas = turmaService.listarTodas();
        if (turmas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma turma cadastrada.", 
                                        "Lista de Turmas", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Código | Série | Professor | Carga Horária\n");
        sb.append("-".repeat(60)).append("\n");
        
        for (Turma t : turmas) {
            String professorNome = t.getProfessorResponsavel() != null ? 
                t.getProfessorResponsavel().getNome() : "Não atribuído";
            sb.append(String.format("%s | %s | %s | %dh\n", 
                t.getCodigo(), t.getSerie(), professorNome, t.getCargaHoraria()));
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Lista de Turmas", 
                                    JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscarTurma() {
        String codigo = JOptionPane.showInputDialog(this, "Digite o código da turma:");
        if (codigo != null && !codigo.trim().isEmpty()) {
            Turma turma = turmaService.buscarPorCodigo(codigo.trim());
            if (turma != null) {
                String professorNome = turma.getProfessorResponsavel() != null ? 
                    turma.getProfessorResponsavel().getNome() : "Não atribuído";
                JOptionPane.showMessageDialog(this, 
                    "Código: " + turma.getCodigo() + "\n" +
                    "Série: " + turma.getSerie() + "\n" +
                    "Professor: " + professorNome + "\n" +
                    "Carga Horária: " + turma.getCargaHoraria() + "h",
                    "Turma Encontrada", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Turma não encontrada.", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void atualizarTurma() {
        // Listar turmas disponíveis
        List<Turma> turmas = turmaService.listarTodas();
        if (turmas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma turma cadastrada para atualizar.", 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mostrar turmas disponíveis
        StringBuilder sb = new StringBuilder("Turmas cadastradas:\n");
        for (Turma t : turmas) {
            String professorNome = t.getProfessorResponsavel() != null ? 
                t.getProfessorResponsavel().getNome() : "Não atribuído";
            sb.append("- ").append(t.getCodigo()).append(" | ")
              .append(t.getSerie()).append(" | ")
              .append(professorNome).append(" | ")
              .append(t.getCargaHoraria()).append("h\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Turmas Disponíveis", 
                                    JOptionPane.INFORMATION_MESSAGE);

        // Selecionar turma para atualizar
        String[] opcoesTurmas = turmas.stream()
            .map(t -> t.getCodigo() + " - " + t.getSerie() + 
                 (t.getProfessorResponsavel() != null ? " (" + t.getProfessorResponsavel().getNome() + ")" : " (Sem professor)"))
            .toArray(String[]::new);

        String turmaSelecionada = (String) JOptionPane.showInputDialog(this, 
            "Selecione a turma para atualizar:", "Selecionar Turma", 
            JOptionPane.QUESTION_MESSAGE, null, opcoesTurmas, opcoesTurmas[0]);

        if (turmaSelecionada == null) return;

        String codigo = turmaSelecionada.split(" - ")[0];
        Turma turma = turmaService.buscarPorCodigo(codigo);
        if (turma == null) {
            JOptionPane.showMessageDialog(this, "Turma não encontrada!", 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField novoCodigoField = new JTextField(turma.getCodigo());
        JTextField novaSerieField = new JTextField(turma.getSerie());
        JTextField novaCargaHorariaField = new JTextField(String.valueOf(turma.getCargaHoraria()));

        // Seleção de disciplina
        java.util.List<models.Disciplina> disciplinas = disciplinaService.listarTodas();
        String[] opcoesDisciplinas = disciplinas.stream()
            .map(d -> d.getCodigo() + " - " + d.getNome())
            .toArray(String[]::new);
        JComboBox<String> comboDisciplinas = new JComboBox<>(opcoesDisciplinas);
        // Selecionar disciplina atual
        int idxAtual = 0;
        for (int i = 0; i < disciplinas.size(); i++) {
            if (disciplinas.get(i).getCodigo().equals(turma.getCodigoDisciplina())) {
                idxAtual = i;
                break;
            }
        }
        comboDisciplinas.setSelectedIndex(idxAtual);

        Object[] message = {
            "Novo código:", novoCodigoField,
            "Nova série:", novaSerieField,
            "Nova carga horária:", novaCargaHorariaField,
            "Disciplina:", comboDisciplinas
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Atualizar Turma", 
                                                  JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String novoCodigo = novoCodigoField.getText().trim();
            String novaSerie = novaSerieField.getText().trim();
            String novaCargaHorariaStr = novaCargaHorariaField.getText().trim();
            String novoCodigoDisciplina = disciplinas.get(comboDisciplinas.getSelectedIndex()).getCodigo();

            if (novoCodigo.isEmpty() || novaSerie.isEmpty() || novaCargaHorariaStr.isEmpty() || novoCodigoDisciplina.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int novaCargaHoraria = Integer.parseInt(novaCargaHorariaStr);
                // Selecionar novo professor
                Professor novoProfessor = selecionarProfessor();
                if (novoProfessor == null) return;
                turmaService.atualizar(codigo.trim(), novoCodigo, novaSerie, novoProfessor, novaCargaHoraria, novoCodigoDisciplina);
                JOptionPane.showMessageDialog(this, "✅ Turma atualizada com sucesso!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Carga horária deve ser um número válido!", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removerTurma() {
        // Listar turmas disponíveis
        List<Turma> turmas = turmaService.listarTodas();
        if (turmas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma turma cadastrada para remover.", 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Selecionar turma para remover
        String[] opcoesTurmas = turmas.stream()
            .map(t -> t.getCodigo() + " - " + t.getSerie() + 
                 (t.getProfessorResponsavel() != null ? " (" + t.getProfessorResponsavel().getNome() + ")" : " (Sem professor)"))
            .toArray(String[]::new);

        String turmaSelecionada = (String) JOptionPane.showInputDialog(this, 
            "Selecione a turma para remover:", "Selecionar Turma", 
            JOptionPane.QUESTION_MESSAGE, null, opcoesTurmas, opcoesTurmas[0]);

        if (turmaSelecionada == null) return;

        String codigo = turmaSelecionada.split(" - ")[0];
        Turma turma = turmaService.buscarPorCodigo(codigo);
        if (turma == null) {
            JOptionPane.showMessageDialog(this, "Turma não encontrada!", 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Tem certeza que deseja remover a turma " + turma.getCodigo() + " (" + turma.getSerie() + ")?", 
            "Confirmar Remoção", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            turmaService.remover(codigo);
            JOptionPane.showMessageDialog(this, "✅ Turma removida com sucesso!");
        }
    }

    private Professor selecionarProfessor() {
        List<Professor> professores = professorService.listarTodos();
        if (professores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum professor cadastrado. Cadastre um professor primeiro.", 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String[] opcoes = professores.stream()
            .map(p -> p.getMatricula() + " - " + p.getNome() + " (" + p.getDisciplina() + ")")
            .toArray(String[]::new);

        String selecionado = (String) JOptionPane.showInputDialog(this, 
            "Selecione o professor responsável:", "Selecionar Professor", 
            JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);

        if (selecionado == null) return null;

        String matricula = selecionado.split(" - ")[0];
        return professores.stream()
            .filter(p -> p.getMatricula().equals(matricula))
            .findFirst().orElse(null);
    }
} 