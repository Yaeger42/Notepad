import javax.swing.*;
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