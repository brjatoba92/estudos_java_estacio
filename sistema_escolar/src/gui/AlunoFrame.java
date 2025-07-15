package gui;

// AlunoFrame.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import services.AlunoService;
import services.DisciplinaService;
import models.Aluno;
import models.Nota;
import models.Disciplina;
import models.Prova;
import java.util.List;

public class AlunoFrame extends JFrame {
    private AlunoService alunoService = new AlunoService();
    private DisciplinaService disciplinaService = new DisciplinaService();

    public AlunoFrame() {
        setTitle("Gerenciamento de Alunos");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel principal com GridLayout
        JPanel mainPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Botões para todas as opções do menu
        mainPanel.add(createButton("📝 Cadastrar Aluno", e -> cadastrarAluno()));
        mainPanel.add(createButton("📋 Listar Alunos", e -> listarAlunos()));
        mainPanel.add(createButton("🔍 Buscar por Matrícula", e -> buscarAluno()));
        mainPanel.add(createButton("✏️ Atualizar Aluno", e -> atualizarAluno()));
        mainPanel.add(createButton("🗑️ Remover Aluno", e -> removerAluno()));
        mainPanel.add(createButton("📊 Adicionar Nota", e -> adicionarNota()));
        mainPanel.add(createButton("📈 Calcular Médias", e -> calcularMedias()));
        mainPanel.add(createButton("🔄 Atualizar Médias", e -> atualizarMedias()));
        mainPanel.add(createButton("💾 Listar do Banco", e -> listarDoBanco()));

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

    private void cadastrarAluno() {
        JTextField nomeField = new JTextField();
        JTextField matriculaField = new JTextField();
        JTextField dataField = new JTextField();

        Object[] message = {
            "Nome:", nomeField,
            "Matrícula:", matriculaField,
            "Data de nascimento (yyyy-mm-dd):", dataField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Cadastrar Aluno", 
                                                  JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nome = nomeField.getText().trim();
            String matricula = matriculaField.getText().trim();
            String data = dataField.getText().trim();

            if (nome.isEmpty() || matricula.isEmpty() || data.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            alunoService.cadastrar(new Aluno(nome, matricula, data));
            JOptionPane.showMessageDialog(this, "✅ Aluno cadastrado com sucesso!");
        }
    }

    private void listarAlunos() {
        List<Aluno> alunos = alunoService.listarTodos();
        if (alunos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum aluno cadastrado.", 
                                        "Lista de Alunos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Matrícula | Nome | Data Nascimento | Média Geral | Status\n");
        sb.append("-".repeat(80)).append("\n");
        
        for (Aluno a : alunos) {
            sb.append(String.format("%s | %s | %s | %.2f | %s\n", 
                a.getMatricula(), a.getNome(), a.getDataNascimento(), 
                a.getMediaGeral(), a.getStatusAprovacaoSalvo()));
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Lista de Alunos", 
                                    JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscarAluno() {
        String matricula = JOptionPane.showInputDialog(this, "Digite a matrícula:");
        if (matricula != null && !matricula.trim().isEmpty()) {
            Aluno aluno = alunoService.buscarPorMatricula(matricula.trim());
            if (aluno != null) {
                JOptionPane.showMessageDialog(this, 
                    "Nome: " + aluno.getNome() + "\nNascimento: " + aluno.getDataNascimento(),
                    "Aluno Encontrado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Aluno não encontrado.", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void atualizarAluno() {
        String matricula = JOptionPane.showInputDialog(this, "Digite a matrícula atual:");
        if (matricula == null || matricula.trim().isEmpty()) return;

        Aluno aluno = alunoService.buscarPorMatricula(matricula.trim());
        if (aluno == null) {
            JOptionPane.showMessageDialog(this, "Aluno não encontrado!", 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField novaMatriculaField = new JTextField(aluno.getMatricula());
        JTextField novoNomeField = new JTextField(aluno.getNome());
        JTextField novaDataField = new JTextField(aluno.getDataNascimento());

        Object[] message = {
            "Nova matrícula:", novaMatriculaField,
            "Novo nome:", novoNomeField,
            "Nova data de nascimento:", novaDataField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Atualizar Aluno", 
                                                  JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String novaMatricula = novaMatriculaField.getText().trim();
            String novoNome = novoNomeField.getText().trim();
            String novaData = novaDataField.getText().trim();

            if (novaMatricula.isEmpty() || novoNome.isEmpty() || novaData.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            alunoService.atualizar(matricula.trim(), novaMatricula, novoNome, novaData);
            JOptionPane.showMessageDialog(this, "✅ Aluno atualizado com sucesso!");
        }
    }

    private void removerAluno() {
        String matricula = JOptionPane.showInputDialog(this, "Digite a matrícula do aluno a remover:");
        if (matricula != null && !matricula.trim().isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja remover o aluno?", "Confirmar Remoção", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Usar reflexão para acessar o método privado
                try {
                    java.lang.reflect.Method removerMethod = AlunoService.class.getDeclaredMethod("remover", String.class);
                    removerMethod.setAccessible(true);
                    removerMethod.invoke(alunoService, matricula.trim());
                    JOptionPane.showMessageDialog(this, "✅ Aluno removido com sucesso!");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Erro ao remover aluno: " + e.getMessage(), 
                                                "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void adicionarNota() {
        // Listar alunos disponíveis
        List<Aluno> alunos = alunoService.listarTodos();
        if (alunos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum aluno cadastrado para adicionar nota.", 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] opcoesAlunos = alunos.stream()
            .map(a -> a.getMatricula() + " - " + a.getNome())
            .toArray(String[]::new);

        String alunoSelecionado = (String) JOptionPane.showInputDialog(this, 
            "Selecione o aluno:", "Adicionar Nota", 
            JOptionPane.QUESTION_MESSAGE, null, opcoesAlunos, opcoesAlunos[0]);

        if (alunoSelecionado == null) return;

        String matricula = alunoSelecionado.split(" - ")[0];
        Aluno aluno = alunoService.buscarPorMatricula(matricula);

        // Listar disciplinas disponíveis
        List<Disciplina> disciplinas = disciplinaService.listarTodas();
        if (disciplinas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma disciplina cadastrada.", 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] opcoesDisciplinas = disciplinas.stream()
            .map(d -> d.getCodigo() + " - " + d.getNome())
            .toArray(String[]::new);

        String disciplinaSelecionada = (String) JOptionPane.showInputDialog(this, 
            "Selecione a disciplina:", "Adicionar Nota", 
            JOptionPane.QUESTION_MESSAGE, null, opcoesDisciplinas, opcoesDisciplinas[0]);

        if (disciplinaSelecionada == null) return;

        String codigoDisciplina = disciplinaSelecionada.split(" - ")[0];
        Disciplina disciplina = disciplinas.stream()
            .filter(d -> d.getCodigo().equals(codigoDisciplina))
            .findFirst().orElse(null);

        // Solicitar descrição da prova
        String descricaoProva = JOptionPane.showInputDialog(this, "Descrição da prova:");
        if (descricaoProva == null || descricaoProva.trim().isEmpty()) return;

        // Solicitar data da prova
        String dataProvaStr = JOptionPane.showInputDialog(this, 
            "Digite a data da prova (yyyy-mm-dd) ou deixe vazio para data atual:");
        java.time.LocalDate dataProva;
        if (dataProvaStr == null || dataProvaStr.trim().isEmpty()) {
            dataProva = java.time.LocalDate.now();
        } else {
            try {
                dataProva = java.time.LocalDate.parse(dataProvaStr.trim());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Data inválida! Use o formato yyyy-mm-dd", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Solicitar peso da prova
        String pesoStr = JOptionPane.showInputDialog(this, "Digite o peso da prova (ex: 1.0, 2.0):");
        if (pesoStr == null || pesoStr.trim().isEmpty()) return;

        try {
            double peso = Double.parseDouble(pesoStr);
            if (peso <= 0) {
                JOptionPane.showMessageDialog(this, "Peso deve ser maior que zero!", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Solicitar nota
            String notaStr = JOptionPane.showInputDialog(this, "Digite a nota (0-10):");
            if (notaStr == null || notaStr.trim().isEmpty()) return;

            double nota = Double.parseDouble(notaStr);
            if (nota < 0 || nota > 10) {
                JOptionPane.showMessageDialog(this, "Nota deve estar entre 0 e 10!", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Criar uma prova e depois a nota
            Prova prova = new Prova(descricaoProva, disciplina, dataProva, peso);
            Nota novaNota = new Nota(nota, prova);
            alunoService.adicionarNota(matricula, novaNota);
            JOptionPane.showMessageDialog(this, "✅ Nota adicionada com sucesso!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido! Use números (ex: 1.0, 2.5)", 
                                        "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calcularMedias() {
        JOptionPane.showMessageDialog(this, 
            "Funcionalidade de cálculo de médias será implementada em breve.", 
            "Em Desenvolvimento", JOptionPane.INFORMATION_MESSAGE);
    }

    private void atualizarMedias() {
        JOptionPane.showMessageDialog(this, 
            "Funcionalidade de atualização de médias será implementada em breve.", 
            "Em Desenvolvimento", JOptionPane.INFORMATION_MESSAGE);
    }

    private void listarDoBanco() {
        JOptionPane.showMessageDialog(this, 
            "Funcionalidade de listagem do banco será implementada em breve.", 
            "Em Desenvolvimento", JOptionPane.INFORMATION_MESSAGE);
    }
}
