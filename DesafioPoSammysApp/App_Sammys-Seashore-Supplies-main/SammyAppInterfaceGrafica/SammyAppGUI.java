import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SammyAppGUI extends JFrame {
    private JComboBox<EquipmentType> comboEquipamentos;
    private JTextField campoMinutos;
    private JTextArea areaResultado;
    private List<Rental> alugueis = new ArrayList<>();

    // Cores e fontes da interface
    private final Color COR_FUNDO = new Color(245, 248, 250);
    private final Color COR_CARTAO = Color.WHITE;
    private final Color COR_PRIMARIA = new Color(66, 165, 245);
    private final Color COR_PRIMARIA_ESCURO = new Color(25, 118, 210);
    private final Color COR_TEXTO = new Color(33, 33, 33);

    private final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 20);
    private final Font FONTE_SUBTITULO = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font FONTE_PADRAO = new Font("Segoe UI", Font.PLAIN, 14);

    public SammyAppGUI() {
        super("Sammy's Seashore Supplies");

        // Configurações básicas da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 500);
        setLocationRelativeTo(null);

        // Fundo da janela
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout(12, 12));

        // ----- TOPO (HEADER) -----
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COR_PRIMARIA);
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel titulo = new JLabel("Sammy's Seashore Supplies");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(FONTE_TITULO);

        JLabel subtitulo = new JLabel("Gerenciamento de Aluguéis de Equipamentos");
        subtitulo.setForeground(new Color(225, 245, 254));
        subtitulo.setFont(FONTE_SUBTITULO);

        JPanel boxTitulo = new JPanel();
        boxTitulo.setLayout(new BoxLayout(boxTitulo, BoxLayout.Y_AXIS));
        boxTitulo.setOpaque(false);
        boxTitulo.add(titulo);
        boxTitulo.add(Box.createVerticalStrut(2));
        boxTitulo.add(subtitulo);

        header.add(boxTitulo, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // ----- CENTRO (FORMULÁRIO + HISTÓRICO LADO A LADO) -----
        JPanel centro = new JPanel(new BorderLayout(12, 12));
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Painel "cartão" do formulário (lado esquerdo)
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBackground(COR_CARTAO);
        painelFormulario.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        comboEquipamentos = new JComboBox<>(EquipmentType.values());
        comboEquipamentos.setFont(FONTE_PADRAO);

        campoMinutos = new JTextField();
        campoMinutos.setFont(FONTE_PADRAO);

        JButton botaoConfirmar = new JButton("Confirmar Aluguel");
        botaoConfirmar.setFont(FONTE_PADRAO);
        botaoConfirmar.setBackground(COR_PRIMARIA_ESCURO);
        botaoConfirmar.setForeground(Color.WHITE);
        botaoConfirmar.setFocusPainted(false);
        botaoConfirmar.setBorder(new EmptyBorder(8, 16, 8, 16));
        botaoConfirmar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Título interno do formulário
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTituloForm = new JLabel("Novo aluguel");
        lblTituloForm.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTituloForm.setForeground(COR_TEXTO);
        painelFormulario.add(lblTituloForm, gbc);

        // Linha 1 - rótulo equipamento
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        painelFormulario.add(criarLabel("Equipamento:"), gbc);

        // Linha 1 - combo equipamentos
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        painelFormulario.add(comboEquipamentos, gbc);

        // Linha 2 - rótulo minutos
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        painelFormulario.add(criarLabel("Tempo (minutos):"), gbc);

        // Linha 2 - campo minutos
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        painelFormulario.add(campoMinutos, gbc);

        // Linha 3 - botão alinhado à direita
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        painelBotao.setOpaque(false);
        painelBotao.add(botaoConfirmar);
        painelFormulario.add(painelBotao, gbc);

        // Painel de resultados (lado direito)
        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Consolas", Font.PLAIN, 13));
        areaResultado.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(areaResultado);
        scroll.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                "Histórico de Aluguéis",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                FONTE_SUBTITULO,
                COR_TEXTO
        ));

        // SplitPane para colocar formulário (esq) e histórico (dir)
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                painelFormulario,
                scroll
        );
        splitPane.setResizeWeight(0.35); // 35% formulário, 65% histórico
        splitPane.setBorder(null);

        centro.add(splitPane, BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        // ----- LISTENERS -----
        botaoConfirmar.addActionListener(e -> confirmarAluguel());
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FONTE_PADRAO);
        label.setForeground(COR_TEXTO);
        return label;
    }

    private void confirmarAluguel() {
        try {
            EquipmentType tipo = (EquipmentType) comboEquipamentos.getSelectedItem();
            if (tipo == null) {
                throw new IllegalStateException("Selecione um equipamento válido.");
            }

            int minutos = Integer.parseInt(campoMinutos.getText().trim());

            Equipment eq;
            // Agora: se requer aula, sempre usa EquipmentWithLesson
            if (tipo.requerAula) {
                eq = new EquipmentWithLesson(tipo.id, tipo.nome, tipo.taxaBasica, tipo.valorHora);
            } else {
                eq = new EquipmentWithoutLesson(tipo.id, tipo.nome, tipo.taxaBasica, tipo.valorHora);
            }

            Rental r = new Rental(minutos, eq);
            alugueis.add(r);

            areaResultado.append(r.toString());
            areaResultado.append("\n-----------------------------\n");

            campoMinutos.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Digite um valor numérico válido para os minutos!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        // Tenta usar o tema Nimbus para ficar mais moderno
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Se der erro, mantém o padrão mesmo
        }

        SwingUtilities.invokeLater(() -> {
            SammyAppGUI app = new SammyAppGUI();
            app.setVisible(true);
        });
    }
}
