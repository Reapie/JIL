package at.htlkaindorf.ahif18.gui;

import at.htlkaindorf.ahif18.eval.Evaluator;
import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Editor {

    private JPanel mainPanel;
    private JTextArea console;
    private JButton btnFile;
    private JButton RUN;
    private JTextArea codeArea;
    private JScrollPane jsp;
    private JPanel consoleContainer;
    private final JTextArea lines;

    public Editor() {
        RUN.addActionListener(actionEvent -> run());
        codeArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F5)
                    run();
            }
        });

        // Setup for line count

        codeArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        console.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        lines = new JTextArea("1");
        lines.setBackground(Color.LIGHT_GRAY);
        lines.setEditable(false);
        lines.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        codeArea.getDocument().addDocumentListener(new DocumentListener() {
            public String getText(){
                int caretPosition = codeArea.getDocument().getLength();
                Element root = codeArea.getDocument().getDefaultRootElement();
                StringBuilder text = new StringBuilder("1" + System.getProperty("line.separator"));

                for(int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
                    text.append(i).append(System.getProperty("line.separator"));
                }
                return text.toString();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                lines.setText(getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                lines.setText(getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                lines.setText(getText());
            }
        });
        lines.setColumns(3);
        jsp.setRowHeaderView(lines);
    }

    public static void main(String[] args) {
        FlatIntelliJLaf.setup();
        JFrame frame = new JFrame("Editor");
        frame.setContentPane(new Editor().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(512, 512);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public void run () {
        String output = Evaluator.pipeline(codeArea.getText());
        console.setText(output);
    }
}
