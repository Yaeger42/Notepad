import javax.swing.*;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.io.FileFilter;
import java.util.Scanner;


public class Notepad extends JFrame implements ActionListener {
    char [] textToSearch;
    char [] textInScreen;
    char [] result;
    JFileChooser chooser;
    private JTextArea txt = new JTextArea();
    private JMenuBar newMenubar() {
        JMenuBar menubar = new JMenuBar();
        String[] titles = {"File", "Search"};
        String[][] elements = {{"New", "Open", "Save as","Save"}, {"Search"}};
        for(int i = 0; i < titles.length; i++) {
            String title = titles[i];
            String[] elems = elements[i];
            menubar.add(newMenu(title, elems));
        }
        return menubar;
    }

    private JMenu newMenu(String title, String[] elements) {
        JMenu menu = new JMenu(title);
        for(String element : elements) {
            JMenuItem menuitem = new JMenuItem(element);
            menu.add(menuitem);
            menuitem.addActionListener(this);
        }
        return menu;
    }

    private Notepad() {
        setTitle("untitled");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        setSize(800, 600); // straight forward lol
        setJMenuBar(newMenubar());
        JScrollPane scroller = new JScrollPane(txt);
        add(scroller);
    }

    public static void main(String[] args) {
        new Notepad().setVisible(true);//loads and sets it visible ik :p
    }

    public void saveAs(){
        JFileChooser chooser = new JFileChooser();
        int option = chooser.showSaveDialog(this);
        if(option == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedWriter buf = new BufferedWriter(new FileWriter(chooser.getSelectedFile().getAbsolutePath()));
                buf.write(txt.getText());
                setTitle(chooser.getSelectedFile().getName());
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void Open(){
        chooser = new JFileChooser();
        chooser.setFileFilter(new Filter());
        int option = chooser.showOpenDialog(this);
        if(option == JFileChooser.APPROVE_OPTION) {
            try {
                Scanner scanner = new Scanner(chooser.getSelectedFile());
                while(scanner.hasNext()) {
                    String data = scanner.nextLine();
                    txt.setText(data);
                }
                setTitle(chooser.getSelectedFile().getName());
                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void actionPerformed(ActionEvent actionEvent) {
        String [] fileName;
        File file = new File("/home/yaeger/Documents/Java");
        String cmd = actionEvent.getActionCommand();
        if(cmd.equals("Save as")) {
            saveAs();

        } else if(cmd.equals("Open")) {
            Open();
        }

        else if(cmd.equals("New")){

            txt.setText("");
        }

        else if(cmd.equals("Save")){

            if(chooser == null){
                //Create a new file
                saveAs();
            }
            else{
                try {
                    BufferedWriter buf = new BufferedWriter(new FileWriter(chooser.getSelectedFile().getAbsolutePath()));
                    buf.write(txt.getText());
                    setTitle(chooser.getSelectedFile().getName());
                    buf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        else if(cmd.equals("Search")){
            SubS subs = new SubS();
            JFrame searchFrame = new JFrame();
            JTextField searchText = new JTextField(20 );
            JTextField foundText = new JTextField(20);
            searchFrame.setVisible(true);
            searchFrame.setSize(300, 300);
            searchFrame.add(searchText);
            JButton searchButton = new JButton("Search");
            searchFrame.add(searchButton);
            searchFrame.add(foundText);
            searchFrame.setLayout(new FlowLayout());
            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    textToSearch = searchText.getText().toCharArray();
                    textInScreen = txt.getText().toCharArray();
                    foundText.setText("Found: " + subs.search(textInScreen, textToSearch));
                }
            });


        }
    }

    class Filter extends javax.swing.filechooser.FileFilter implements FileFilter {

        public boolean accept(File file) {
            return file.getName().endsWith(".txt") || file.getName().endsWith(".java");
        }

        @Override
        public String getDescription() {
            return "Text File (.txt)";
        }
    }
}

/*

Highlight something OF SOMETHING





class MyHighlightPainter extends DefaultHighlighter.DefaultHighlighter{
    public MyHighlightPainter(Color color){
        super(color);
    }
}

    Highlighter.HighlightPainter myHighLightPainter = (Highlighter.HighlightPainter) new MyHighlightPainter(Color.yellow);

    public void removeHighlights(JTextField textComp){
        Highlighter hilite = textComp.getHighlighter();
        Highlighter.Highlight [] unhighlight = hilite.getHighlights();
        for(int i = 0; i<unhighlight.length; i++){
            if(unhighlight[i].getPainter().instanceOf(MyHighlightPainter)){
                hilite.removeHighlights(unhighlight[i]);
            }
        }
    }

    public void highlight(JTextComponent textComp, String pattern){
        removeHighlights(textComp);

        try{

            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            int pos = 0;
            while((pos = text.toUpperCase().indexOf(pattern.toUpperCase(), pos)) >=0){

                hilite.addHighlight(pos, pos+pattern.length(), myHighLightPainter);
                pos += pattern.length();
            }

        }catch(Exception e){

        }

 }

 --------------------------------ANOTHER APPROACH ------------------------
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class LineHighlightPainter {

    String revisedText = "Extreme programming is one approach "
            + "of agile software development which emphasizes on frequent"
            + " releases in short development cycles which are called "
            + "time boxes. This result in reducing the costs spend for "
            + "changes, by having multiple short development cycles, "
            + "rather than one long one. Extreme programming includes "
            + "pair-wise programming (for code review, unit testing). "
            + "Also it avoids implementing features which are not included "
            + "in the current time box, so the schedule creep can be minimized. ";
    String token = "Extreme programming includes pair-wise programming";

    public static void main(String args[]) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    new LineHighlightPainter().createAndShowGUI();
                }
            });
        } catch (InterruptedException ex) {
            // ignore
        } catch (InvocationTargetException ex) {
            // ignore
        }
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("LineHighlightPainter demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextArea area = new JTextArea(9, 45);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setText(revisedText);

        // Highlighting part of the text in the instance of JTextArea
        // based on token.
        highlight(area, token);

        frame.getContentPane().add(new JScrollPane(area), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    // Creates highlights around all occurrences of pattern in textComp
    public void highlight(JTextComponent textComp, String pattern) {
        // First remove all old highlights
        removeHighlights(textComp);

        try {
            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());

            int pos = 0;
            // Search for pattern
            while ((pos = text.indexOf(pattern, pos)) >= 0) {
                // Create highlighter using private painter and apply around pattern
                hilite.addHighlight(pos, pos + pattern.length(), myHighlightPainter);
                pos += pattern.length();
            }

        } catch (BadLocationException e) {
        }
    }

    // Removes only our private highlights
    public void removeHighlights(JTextComponent textComp) {
        Highlighter hilite = textComp.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();

        for (int i = 0; i < hilites.length; i++) {
            if (hilites[i].getPainter() instanceof MyHighlightPainter) {
                hilite.removeHighlight(hilites[i]);
            }
        }
    }
    // An instance of the private subclass of the default highlight painter
    Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(Color.red);

    // A private subclass of the default highlight painter
    class MyHighlightPainter
            extends DefaultHighlighter.DefaultHighlightPainter {

        public MyHighlightPainter(Color color) {
            super(color);
        }
    }
}


 */