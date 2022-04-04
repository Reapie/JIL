package at.htlkaindorf.ahif18.gui;

import javax.swing.*;

public class Editor {

    private JPanel mainPanel;
    private JEditorPane console;
    private JButton btnFile;
    private JButton RUN;
    private JTextArea textArea1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Editor");
        frame.setContentPane(new Editor().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}
