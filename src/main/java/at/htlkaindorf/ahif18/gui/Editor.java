package at.htlkaindorf.ahif18.gui;

import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;

public class Editor {

    private JPanel mainPanel;
    private JEditorPane console;
    private JButton btnFile;
    private JButton RUN;
    private JTextArea textArea1;

    public static void main(String[] args) {
        FlatIntelliJLaf.setup();
        JFrame frame = new JFrame("Editor");
        frame.setContentPane(new Editor().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(512, 512);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
