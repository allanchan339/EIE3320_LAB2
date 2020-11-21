//CHAN CHEUK YIU
//17067305D
//LI Haoyang
//17083702D
//JDK14
//IntelliJ IDEA

package LAB;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class UI extends JFrame {
    //    protected JTextArea jTextArea = new JTextArea().
//    jTextArea.setLineWrap(true);
    protected JButton Add = new JButton("Add");
    protected JButton Edit = new JButton("Edit");
    protected JButton Save = new JButton("Save");
    protected JButton Delete = new JButton("Delete");
    protected JButton Search = new JButton("Search");
    protected JButton More = new JButton("More>>");
    protected JButton LoadTestData = new JButton("Load Test Data");
    protected JButton DisplayAll = new JButton("Display All");
    protected JButton DisplayAllByTitle = new JButton("Display All by Title");
    protected JButton DisplayAllByISBN = new JButton("Display All by ISBN");
    protected JButton Exit = new JButton("Exit");
    protected JTextField ISBN = new JTextField("", 8);
    protected JTextField Title = new JTextField("", 8);
    protected JTable bookTable = createMiddlePanel();
    protected MyLinkedList<Book> library = new MyLinkedList<>();
    protected Connection conn = connect();

    public static Connection connect(){
        Connection conn = null;
        try {

            String url = "jdbc:sqlite:test.sqlite";
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to sqlite succeed");
        } catch (SQLException e){
            System.out.println(e.getMessage());
            System.out.println("If error code appeared, please insert sqlite-jbdc-<version>.jar to library // CLASSPATH");
        }
        return conn;
    }

    public static void clossSession(Connection conn){
        try {
            if (conn != null){
                conn.close();
                System.out.println("Connection to sqlite is closed");
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public JPanel createLowerPanel() {
        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new GridLayout(3, 1));

        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout());
        p2.add(new JLabel("ISBN:"));
        p2.add(ISBN);
        p2.add(new JLabel("Title:"));
        p2.add(Title);

        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout());
        p3.add(Add);
        p3.add(Edit);
        p3.add(Save);
        p3.add(Delete);
        p3.add(Search);
        p3.add(More);
        Save.setEnabled(false);

        JPanel p4 = new JPanel();
        p4.setLayout(new FlowLayout());
        p4.add(LoadTestData);
        p4.add(DisplayAll);
        p4.add(DisplayAllByISBN);
        p4.add(DisplayAllByTitle);
        p4.add(Exit);

        lowerPanel.add(p2);
        lowerPanel.add(p3);
        lowerPanel.add(p4);
        return lowerPanel;
    }

    public JTextArea createUpperPanel() {
        JTextArea textArea = new JTextArea();
        String User1 = "Student Name and ID: CHAN CHEUK YIU (17067305d)";
        String User2 = "Student Name and ID: LI Haoyang (17083702d)";
        Date date = new Date();
        textArea.append(User1 + "\n" + User2 + "\n" + date + "\n\n");
        return textArea;
    }

    public JTable createMiddlePanel() {
        String[] columnNames = {"ISBN", "Title", "Available"};

        DefaultTableModel model = new DefaultTableModel();

        for (String name : columnNames) {
            model.addColumn(name);
        }
        bookTable = new JTable(model);
        //starting point to load SQL data
        return bookTable;
    }

    public UI() {
        JPanel lowerPanel = createLowerPanel();
        JTextArea upperPanel = createUpperPanel();
        JScrollPane jScrollPane = new JScrollPane(bookTable);
        setLayout(new BorderLayout(0, 0));
        add(upperPanel, BorderLayout.NORTH);
        add(jScrollPane, BorderLayout.CENTER);
        add(lowerPanel, BorderLayout.SOUTH);

        actionLoader();
    }

    private void actionLoader() {
        ActionListener addListener = new AddListener();
        ActionListener loadTestDataListener = new loadTestDataListener();
        ActionListener editSaveActioner = new EditSaveActioner();
        ActionListener deleteListener = new DeleteListener();
        ListSelectionListener tableSelectedRowListener = new TableSelectedRowListener();
        ActionListener searchActioner = new SearchListener();
        ActionListener displayAllListener = new DisplayAllListener();
        ActionListener displayAllByISBNListener = new DisplayAllByISBNListener();
        ActionListener displayAllByTitleListener = new DisplayAllByTitleListener();
        ActionListener moreListener = new MoreListener();
        ActionListener exitListener = new ExitListener();

        Add.addActionListener(addListener);
        LoadTestData.addActionListener(loadTestDataListener);
        Edit.addActionListener(editSaveActioner);
        Save.addActionListener(editSaveActioner);
        Delete.addActionListener(deleteListener);
        bookTable.getSelectionModel().addListSelectionListener(tableSelectedRowListener);
        Search.addActionListener(searchActioner);
        DisplayAll.addActionListener(displayAllListener);
        DisplayAllByISBN.addActionListener(displayAllByISBNListener);
        DisplayAllByTitle.addActionListener(displayAllByTitleListener);
        More.addActionListener(moreListener);
        Exit.addActionListener(exitListener);
    }

    public static void main(String[] args) {
        UI ui = new UI();
        ui.setTitle("Library Admin System");
        ui.setSize(800, 400);
        ui.setLocationRelativeTo(null);
        ui.setDefaultCloseOperation(EXIT_ON_CLOSE);
        ui.setVisible(true);
    }

    private void refleshTable() {
        String[] columnNames = {"ISBN", "Title", "Available"};
        DefaultTableModel model = new DefaultTableModel();
        for (String name : columnNames) {
            model.addColumn(name);
        }

        for (int i = 0; i < library.size(); i++) {
            String ISBN = library.get(i).getISBN();
            String Title = library.get(i).getTitle();
            boolean Available = library.get(i).isAvailable();
            Object[] x = {ISBN, Title, Available};
            model.addRow(x);
        }
        bookTable.setModel(model);
    }

    private void refleshTable(MyLinkedList<Book> filter_result) {
        String[] columnNames = {"ISBN", "Title", "Available"};
        DefaultTableModel model = new DefaultTableModel();
        for (String name : columnNames) {
            model.addColumn(name);
        }

        for (int i = 0; i < filter_result.size(); i++) {
            String ISBN = filter_result.get(i).getISBN();
            String Title = filter_result.get(i).getTitle();
            boolean Available = filter_result.get(i).isAvailable();
            Object[] x = {ISBN, Title, Available};
            model.addRow(x);
        }
        bookTable.setModel(model);
    }

    class AddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if ((ISBN.getText().length() != 0) && (Title.getText().length() != 0)) {
                for (int i = 0; i < library.size(); i++) {
                    if (library.get(i).getISBN().equals(ISBN.getText())) {
                        JOptionPane.showMessageDialog(null, "This ISBN has been occupied by another book in the library!");
                        return;
                    }
                }
                Book newBook = new Book();
                newBook.setAvailable(true);
                newBook.setISBN(ISBN.getText());
                newBook.setTitle(Title.getText());
                library.add(newBook);

                refleshTable();
            } else {
                JOptionPane.showMessageDialog(null, "Please fill in both ISBN and Title!");
            }
        }
    }

    class loadTestDataListener implements ActionListener {

        boolean checkBook1(){
            Book book1 = new Book();
            book1.setAvailable(true);
            book1.setISBN("0131450913");
            book1.setTitle("HTML How to Program");
            BufferedImage image;
            try{
                image = ImageIO.read(new File("./resource/html.png"));}
            catch (IOException e){
                e.getMessage();
                image = null;
            }
            book1.setImage(image);
            for (Book book :
                    library) {
                if (book.getISBN().equals(book1.getISBN())) {
                    return true;
                }
            }
            library.add(book1);
            return false;
        }

        boolean checkBook2(){
            Book book2 = new Book();
            book2.setAvailable(true);
            book2.setISBN("0131857576");
            book2.setTitle("C++ How to Program");
            BufferedImage image;
            try{
            image = ImageIO.read(new File("./resource/C.png"));}
            catch (IOException e){
                e.getMessage();
                image = null;
            }
            book2.setImage(image);

            for (Book book :
                    library) {
                if (book.getISBN().equals(book2.getISBN())) {
                    return true;
                }
            }
            library.add(book2);
            return false;
        }

        boolean checkBook3(){
            Book book3 = new Book();
            book3.setAvailable(true);
            book3.setISBN("0132222205");
            book3.setTitle("Java How to Program");
            BufferedImage image;
            try{
                image = ImageIO.read(new File("./resource/java.png"));}
            catch (IOException e){
                e.getMessage();
                image = null;
            }
            book3.setImage(image);

            for (Book book :
                    library) {
                if (book.getISBN().equals(book3.getISBN())) {
                    return true;
                }
            }
            library.add(book3);
            return false;
        }

        public void actionPerformed(ActionEvent e) {
            if (checkBook1() & checkBook2() & checkBook3()) { // check if 3 book are already here
                JOptionPane.showMessageDialog(null, "Error: test data already exist in the current database");
            }
            refleshTable();
        }
    }

    class TableSelectedRowListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting() && bookTable.getSelectedRow() != -1) {
                //Important if check to avoid update/delete/add trigger this
                //it helps never jump to dead loop
                //!e.getValueIsAdjusting only response to the 2nd fire, eg. pressed and released mouse
                //getSelectedRow() != -1 only valid the click inside the table
                int index = bookTable.getSelectedRow();
                String ISBNData = (String) bookTable.getValueAt(index, 0);
                String titleData = (String) bookTable.getValueAt(index, 1);
                ISBN.setText(ISBNData);
                Title.setText(titleData);
            }
        }
    }

    class DeleteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            bookTable.clearSelection();
            refleshTable();
            String ISBNData = ISBN.getText();
            for (int i = 0; i < library.size(); i++) {
                if (ISBNData.equals(library.get(i).getISBN())) {
                    library.remove(i);
                    ISBN.setText("");
                    Title.setText("");
                }
            }
            refleshTable();
        }
    }

    class EditSaveActioner implements ActionListener {
        void buttonSwitched(boolean flag) {
            boolean flagN = !flag;
            Add.setEnabled(flagN);
            Edit.setEnabled(flagN);
            Delete.setEnabled(flagN);
            Search.setEnabled(flagN);
            LoadTestData.setEnabled(flagN);
            DisplayAll.setEnabled(flagN);
            DisplayAllByISBN.setEnabled(flagN);
            DisplayAllByTitle.setEnabled(flagN);
            Exit.setEnabled(flagN);
            More.setEnabled(flagN);
            Save.setEnabled(flag);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int index = bookTable.getSelectedRow();

            if (e.getSource() == Edit) {
                buttonSwitched(true);
            } else if (e.getSource() == Save) {
                String ISBNData = ISBN.getText();
                String titleData = Title.getText();
                boolean corrupt = false;

                for (Book book :
                        library) {
                    if (book.getISBN().equals(ISBNData)) {
                        JOptionPane.showMessageDialog(null, "Error: book ISBN exits in the current database");
                        corrupt = true;

                    }
                }
                if (!corrupt) {
                    Book temp = new Book();
                    temp.setISBN(ISBNData);
                    temp.setTitle(titleData);
                    temp.setAvailable(library.get(index).isAvailable());
                    temp.setReservedQueue(library.get(index).getReservedQueue());
                    library.set(index, temp);
                    buttonSwitched(false);
                    refleshTable();
                }
            }
        }
    }

    class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MyLinkedList<Book> result = new MyLinkedList<>();
            String ISBNData = ISBN.getText();
            String titleData = Title.getText();
            if (!ISBNData.isEmpty()) {
                for (int i = 0; i < library.size(); i++) {
                    if (library.get(i).getISBN().contains(ISBNData)) {
                        result.add(library.get(i));
                    }
                }
            }
            if (!titleData.isEmpty()) {
                for (int i = 0; i < library.size(); i++) {
                    if (library.get(i).getTitle().contains(titleData)) {
                        result.add(library.get(i));
                    }
                }
            }
            refleshTable(result);
        }
    }

    class DisplayAllListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            refleshTable();
        }
    }
    
    class DisplayAllByISBNListener implements ActionListener {
    	private boolean asc = true;
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		MyLinkedList<Book> result = new MyLinkedList<>();
    		if(asc) {
    			asc = false;
    			int lastISBN = 0, index = 0;
    			while(result.size()<library.size()) {
    				int minISBN = 999999999;
        			for(int i=0; i<library.size(); i++) {
        				int currentISBN = Integer.valueOf(library.get(i).getISBN());
        				if(currentISBN>lastISBN && currentISBN<minISBN) {
        					minISBN = currentISBN;
        					index = i;
        				}
        			}
        			lastISBN = minISBN;
        			result.add(library.get(index));
        		}
    		} else {
    			asc = true;
    			int lastISBN = 999999999, index = 0;
    			while(result.size()<library.size()) {
    				int maxISBN = 0;
        			for(int i=0; i<library.size(); i++) {
        				int currentISBN = Integer.valueOf(library.get(i).getISBN());
        				if(currentISBN<lastISBN && currentISBN>maxISBN) {
        					maxISBN = currentISBN;
        					index = i;
        				}
        			}
        			lastISBN = maxISBN;
        			result.add(library.get(index));
        		}
    		}
    		refleshTable(result);
    	}
    }
    
    class DisplayAllByTitleListener implements ActionListener {
    	private boolean asc = true;
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		MyLinkedList<Book> result = new MyLinkedList<>();
    		if(asc) {
    			asc = false;
    			String lastTitle = " ";
    			int index = 0;
    			while(result.size()<library.size()) {
    				String minTitle = "~~~~~~~~~~";
        			for(int i=0; i<library.size(); i++) {
        				String currentTitle = library.get(i).getTitle();
        				if(currentTitle.compareTo(lastTitle)>0 && currentTitle.compareTo(minTitle)<0) {
        					minTitle = currentTitle;
        					index = i;
        				}
        			}
        			lastTitle = minTitle;
        			result.add(library.get(index));
        		}
    		} else {
    			asc = true;
    			String lastTitle = "~~~~~~~~~~";
    			int index = 0;
    			while(result.size()<library.size()) {
    				String maxTitle = " ";
        			for(int i=0; i<library.size(); i++) {
        				String currentTitle = library.get(i).getTitle();
        				if(currentTitle.compareTo(lastTitle)<0 && currentTitle.compareTo(maxTitle)>0) {
        					maxTitle = currentTitle;
        					index = i;
        				}
        			}
        			lastTitle = maxTitle;
        			result.add(library.get(index));
        		}
    		}
    		refleshTable(result);
    	}
    	
    }

    class MoreListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		if(ISBN.getText().length()>0) {
    			for (int i = 0; i < library.size(); i++) {
                    if (library.get(i).getISBN().equals(ISBN.getText())) {
                    	More more = new More(library.get(i).getISBN(), library.get(i).getTitle(), library.get(i).isAvailable(), library);
                		more.setTitle(library.get(i).getTitle());
                        more.setSize(600, 500);
                        more.setLocationRelativeTo(null);
                        more.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                        more.setVisible(true);
                        return;
                    }
                }
    			JOptionPane.showMessageDialog(null, "No matched ISBN!");
    		} else {
    			JOptionPane.showMessageDialog(null, "Please fill in ISBN!");
    		}
    	}
    }
    class ExitListener implements ActionListener{

        public void libraryParseandStoreToSQL() throws SQLException, IOException {
            PreparedStatement reset = conn.prepareStatement("DELETE FROM Books;");
            reset.executeUpdate();
            for (int i = 0; i < library.size(); i++) {
                PreparedStatement store = conn.prepareStatement("INSERT INTO Books VALUES (?,?,?,?,?)");
                store.setString(1,library.get(i).getTitle());
                store.setString(2,library.get(i).getISBN());
                store.setBoolean(3,library.get(i).isAvailable());
                String queue = library.get(i).getReservedQueue().toString();
                queue = queue.replace("Queue: [","");
                queue = queue.replace("]","");
                queue = queue.trim();
                store.setString(4,queue);
                BufferedImage image = library.get(i).getImage();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if (image != null){
                    ImageIO.write(image,"png",baos);
                }
                store.setBytes(5,baos.toByteArray());
                store.executeUpdate();
            }
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                libraryParseandStoreToSQL();
            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            }
            clossSession(conn);
            System.exit(0);
        }
    }
}
