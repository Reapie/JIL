package at.htlkaindorf.ahif18.gui;

import at.htlkaindorf.ahif18.eval.Evaluator;
import at.htlkaindorf.ahif18.tokens.TokenType;
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
/**
 * The Editor class represents the GUI. These class contains JavaSwing objects, listeners, methods for code highlighting and so on
 *
 * @author Fabian Ladenhaufen
 * @version 1.0
 * @since 1.0
 */
public class Editor {

    /**
     * the mainpanel of the JFrame
     */
    private JPanel mainPanel;
    /**
     * Text Area for the editor console
     */
    private JTextArea console;
    /**
     * Button to show the menue
     */
    private JButton btnFile;
    /**
     * Button to execute the code
     */
    private JButton RUN;
    /**
     * Textarea for the code
     */
    private JTextPane codeArea;
    /**
     * Panel to scroll down on the codeArea
     */
    private JScrollPane jsp;
    /**
     * textarea for the vertical row number
     */
    private JTextArea lines = null;
    /**
     * menue for save, open, new buttons
     */
    private JPopupMenu fileMenue;

    private JFrame frame;
    /**
     * filepath for the save location
     */
    private String filePath = "";
    private String syntaxRegex = "(\\W)*(sqrt|sin|)";
    private JPanel consoleContainer;

    /**
     * GUI Item Initialisations
     * Listeners, Formartings
     * @param frame
     */
    public Editor(JFrame frame) {
        this.frame = frame;
        fileMenue = new JPopupMenu();

        final StyleContext cont = StyleContext.getDefaultStyleContext();
        final AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
        final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);

        JMenuItem jmiNew = new JMenuItem("New");
        JMenuItem jmiOpen = new JMenuItem("Open");
        JMenuItem jmiSave = new JMenuItem("Save");
        JMenuItem jmiSaveAs = new JMenuItem("Save As");

        fileMenue.add(jmiNew);
        fileMenue.add(jmiOpen);
        fileMenue.add(jmiSave);
        fileMenue.add(jmiSaveAs);


        RUN.addActionListener(actionEvent -> run());
        codeArea.addKeyListener(new KeyAdapter() {
            @SneakyThrows
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F5) {
                    run();
                } else if (e.getKeyCode() == KeyEvent.VK_S && (e.getModifiers() & KeyEvent.CTRL_MASK) > 0) {
                    if (filePath.isEmpty()) {
                        saveFileAs();
                    } else {
                        saveFile();
                    }

                } else if (e.getKeyCode() == KeyEvent.VK_N && (e.getModifiers() & KeyEvent.CTRL_MASK) > 0) {
                    newFile();

                } else if (e.getKeyCode() == KeyEvent.VK_O && (e.getModifiers() & KeyEvent.CTRL_MASK) > 0) {
                    openFile();
                }
            }
        });


        btnFile.addActionListener(e -> fileMenue.show(frame, 10, 60));

        jmiNew.addActionListener(e -> newFile());

        jmiOpen.addActionListener(e -> {
            try {
                openFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        jmiSave.addActionListener(e -> {
            try {
                if (filePath.isEmpty()) {
                    saveFileAs();
                } else {
                    saveFile();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        jmiSaveAs.addActionListener(e -> {
            try {
                saveFileAs();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });


        DefaultStyledDocument doc = new DefaultStyledDocument() {
            /**
             * Everytime a character is typed in this method will be called.
             * colors certain words in red and otherwise in black
             * calls the codecompletion method
             * sets the row number text to the variable lines
             *
             * @param str the string to insert
             * @param offset index position of the last typed character
             * @param a the attributes for the inserted content
             * @since 1.0
             */
            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);
                //sets row number text to lines
                lines.setText(getTextLines());

                //System.out.println("InsertString");
                String text = getText(0, getLength());


                int before = findLastNonWordChar(text, offset);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offset + str.length());
                int wordL = before;
                int wordR = before;


                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                        if (text.substring(wordL, wordR).matches(syntaxRegex))
                            setCharacterAttributes(wordL, wordR - wordL, attr, false);
                        else
                            setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                        wordL = wordR;
                    }
                    wordR++;
                }

                codeCompletion(text, offset);
            }

            /**
             * Everytime a character will be removed  this method will be called.
             * words that not matches the key words will be colored in black
             * @param len the number of characters to remove
             * @param offs index position of the last typed character
             * @since 1.0
             */
            public void remove(int offs, int len) throws BadLocationException {
                super.remove(offs, len);
                lines.setText(getTextLines());
                //System.out.println("remove string");

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offs);

                if (text.substring(before, after).matches(syntaxRegex)) {
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

    /**
     *starts the code execution
     * @since 1.0
     */
    public void run() {
        String output = Evaluator.pipeline(codeArea.getText());
        console.setText(output);
    }

    /**
     * Finds the position of the last character of a word
     *
     * @param text  contains the full editor text
     * @param index index position of the last typed character
     * @return last index position of the current word
     * @since 1.0
     */
    private int findLastNonWordChar(String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    /**
     * Finds the position of the first character of a word
     *
     * @param text  contains the full editor text
     * @param index index position of the last typed character
     * @return first index position of the current word
     * @since 1.0
     */
    private int findFirstNonWordChar(String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }

    /**
     * Opens a jil File and sets the containing text to the editor
     * @since 1.0
     */

    private void openFile() throws IOException {

        JFileChooser chooser = new JFileChooser();
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("*.jil", "jil"));
        chooser.setAcceptAllFileFilterUsed(true);
        int result = chooser.showOpenDialog(null);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }


        File selectedFile = chooser.getSelectedFile();
        filePath = selectedFile.getAbsolutePath();
        String sourceCode = "";
        BufferedReader reader = new BufferedReader(new FileReader(selectedFile));

        List<String> sourceCodeList = reader.lines().collect(Collectors.toList());
        reader.close();

        for (String line : sourceCodeList) {
            sourceCode += line + "\n";
        }

        codeArea.setText(sourceCode);
    }

    /**
     * Saves the editor text to an already existing file
     * the existing file is the last saved file
     *
     * @since 1.0
     */

    private void saveFile() throws IOException {
        String code = codeArea.getText();
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

        writer.write(code);
        writer.close();

    }

    /**
     * Saves the editor text to an new file
     * a save dialog is opened for this
     *
     * @since 1.0
     */
    private void saveFileAs() throws IOException {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save");
        chooser.setSelectedFile(new File("programm.jil"));


        int result = chooser.showSaveDialog(null);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        filePath = chooser.getSelectedFile().getAbsolutePath();


        if (!filePath.contains(".jil")) {
            filePath = filePath + ".jil";
        }

        saveFile();

    }


    /**
     * Clears the editor text and sets the file path to none
     * @since 1.0
     */

    private void newFile() {
        codeArea.setText("");
        filePath = "";
    }


    /**
     * Makes a autocompletion when a specific character is typed in
     * If a parenthesis or a quote is entered, a second one will be set and the cursor position will be set in the middle
     *
     * @param text   contains the full current editor text
     * @param offset position of the last typed character
     * @since 1.0
     */
    private void codeCompletion(String text, int offset) {
        String character = "";
        switch (text.charAt(offset)) {
            case '(':
                character = ")";
                break;
            case '"':
                character = "\"";
                break;
            default:
                return;
        }
        text = new StringBuilder(text).insert(offset + 1, character).toString();

        codeArea.setText(text);
        codeArea.setCaretPosition(offset + 1);
    }

    /**
     * Calculates the vertical rows of the editor
     * @return  the row numbers as string
     * @since 1.0
     */
    public String getTextLines() {
        int caretPosition = codeArea.getDocument().getLength();
        Element root = codeArea.getDocument().getDefaultRootElement();
        StringBuilder text = new StringBuilder("1" + System.getProperty("line.separator"));

        for (int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
            text.append(i).append(System.getProperty("line.separator"));
        }
        return text.toString();
    }
}


