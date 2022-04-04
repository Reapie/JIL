package at.htlkaindorf.ahif18.gui;

import at.htlkaindorf.ahif18.eval.Evaluator;
import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Editor {

    private JPanel mainPanel;
    private JEditorPane console;
    private JButton btnFile;
    private JButton RUN;
    private JTextArea codeArea;

    public Editor() {
        RUN.addActionListener(actionEvent -> run());
        codeArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F5)
                    run();
            }
        });
        codeArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        console.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
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
