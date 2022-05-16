package at.htlkaindorf.ahif18.gui;

import at.htlkaindorf.ahif18.eval.Evaluator;
import com.formdev.flatlaf.FlatIntelliJLaf;
import lombok.SneakyThrows;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.List;

public class Editor {

    private JPanel mainPanel;
    private JTextArea console;
    private JButton btnFile;
    private JButton RUN;
    private JTextPane codeArea;
    private JScrollPane jsp;
    private JPanel consoleContainer;
    private JTextArea lines = null;
    private JPopupMenu fileMenue;
    private JFrame frame;
    private String filePath = "";


    public Editor(JFrame frame) {
        this.frame = frame;
        RUN.addActionListener(actionEvent -> run());
        codeArea.addKeyListener(new KeyAdapter() {
            @SneakyThrows
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F5) {
                    run();
                }else if (e.getKeyCode() == KeyEvent.VK_S && (e.getModifiers() & KeyEvent.CTRL_MASK) > 0) {
                    if(filePath.isEmpty()){
                        saveFileAs();
                    }else{
                        saveFile();
                    }

                } else if (e.getKeyCode() == KeyEvent.VK_N && (e.getModifiers() & KeyEvent.CTRL_MASK) > 0) {
                    newFile();

                }else if (e.getKeyCode() == KeyEvent.VK_O && (e.getModifiers() & KeyEvent.CTRL_MASK) > 0) {
                    openFile();
                }

        }});

        fileMenue = new JPopupMenu();

        JMenuItem jmiNew = new JMenuItem("New");
        JMenuItem jmiOpen = new JMenuItem("Open");
        JMenuItem jmiSave = new JMenuItem("Save");
        JMenuItem jmiSaveAs = new JMenuItem("Save As");

        fileMenue.add(jmiNew);
        fileMenue.add(jmiOpen);
        fileMenue.add(jmiSave);
        fileMenue.add(jmiSaveAs);

        btnFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               fileMenue.show(frame,10,60);
            }
        });

        jmiNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newFile();
            }


        });

        jmiOpen.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }


        });

        jmiSave.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                if(filePath.isEmpty()){
                    saveFileAs();
                }else{
                   saveFile();
                }
            }
        });

        jmiSaveAs.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFileAs();
            }
        });

        final StyleContext cont = StyleContext.getDefaultStyleContext();
        final AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
        final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);

        DefaultStyledDocument doc = new DefaultStyledDocument() {

            public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offset);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offset + str.length());
                int wordL = before;
                int wordR = before;

                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                        if (text.substring(wordL, wordR).matches("(\\W)*(var|null)"))
                            setCharacterAttributes(wordL, wordR - wordL, attr, false);
                        else
                            setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                        wordL = wordR;
                    }
                    wordR++;
                }
            }

            public void remove (int offs, int len) throws BadLocationException {
                super.remove(offs, len);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offs);

                if (text.substring(before, after).matches("(\\W)*(var|null)")) {
                    setCharacterAttributes(before, after - before, attr, false);
                } else {
                    setCharacterAttributes(before, after - before, attrBlack, false);
                }
            }
        };

        codeArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        codeArea.setStyledDocument(doc);
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
        frame.setContentPane(new Editor(frame).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(512, 512);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public void run () {
        String output = Evaluator.pipeline(codeArea.getText());
        console.setText(output);
    }

    private int findLastNonWordChar (String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    private int findFirstNonWordChar (String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }

    private void openFile() throws IOException {

        JFileChooser chooser = new JFileChooser();
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("*.jil", "jil"));
        chooser.setAcceptAllFileFilterUsed(true);
        int result =  chooser.showOpenDialog(null);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }


        File selectedFile = chooser.getSelectedFile();
        filePath = selectedFile.getAbsolutePath();
        String sourceCode = "";
        BufferedReader reader = new BufferedReader(new FileReader(selectedFile));

        List<String> sourceCodeList = reader.lines().collect(Collectors.toList());
        reader.close();

        for(String line : sourceCodeList){
            sourceCode+=line + "\n";
        }

        codeArea.setText(sourceCode);
    }

    private void saveFile() throws IOException {
        String code = codeArea.getText();
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

        writer.write(code);
        writer.close();

    }
    private void saveFileAs() throws IOException {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save");
        chooser.setSelectedFile(new File("programm.jil"));



        int result =  chooser.showSaveDialog(null);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        filePath = chooser.getSelectedFile().getAbsolutePath();


        if(!filePath.contains(".jil")){
            filePath = filePath + ".jil";
        }

        saveFile();

    }

    private void newFile() {
        codeArea.setText("");
        filePath = "";
    }
}
