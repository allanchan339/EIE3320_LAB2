//CHAN CHEUK YIU
//17067305D
//JDK14
//IntelliJ IDEA

package LAB;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
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
    protected JTextField ISBN = new JTextField("",8);
    protected JTextField Title = new JTextField("",8);
    protected JTable bookTable = createMiddlePanel();

    protected MyLinkedList<Book> library = new MyLinkedList<>();
    
    public JPanel createLowerPanel(){
        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new GridLayout(3,1));

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

    public JTextArea createUpperPanel(){
//        JPanel upperPanel = new JPanel();
//        upperPanel.setLayout(new FlowLayout());
        JTextArea textArea = new JTextArea();
        String User1 = "Student Name and ID: CHAN CHEUK YIU (17067305d)";
        String User2 = "Student Name and ID: LI Haoyang (17083702d)";
        Date date = new Date();
        textArea.append(User1+"\n"+User2+"\n"+date+"\n\n");
//        upperPanel.add(textArea);
        return textArea;
    }

    public JTable createMiddlePanel(){
        String[] columnNames = { "ISBN", "Title", "Available" };

        DefaultTableModel model = new DefaultTableModel();

        for (String name: columnNames) {
            model.addColumn(name);
        }
        bookTable = new JTable(model);
        return bookTable;
    }

    public UI(){
        JPanel lowerPanel = createLowerPanel();
        JTextArea upperPanel = createUpperPanel();
        JScrollPane jScrollPane = new JScrollPane(bookTable);
        setLayout(new BorderLayout(0,0));
        add(upperPanel, BorderLayout.NORTH);
        add(jScrollPane, BorderLayout.CENTER);
        add(lowerPanel, BorderLayout.SOUTH);
        
        ActionListener addListener = new AddListener();
        ActionListener loadTestDataListener = new loadTestDataListener();
        ActionListener refleshTable = new RefleshTable();
        Add.addActionListener(addListener);
        LoadTestData.addActionListener(loadTestDataListener);
        Timer timer = new Timer(1,refleshTable);
        timer.start();
    }

    public static void main(String[] args) {
        UI ui = new UI();
        ui.setTitle("Library Admin System");
        ui.setSize(800,400);
        ui.setLocationRelativeTo(null);
        ui.setDefaultCloseOperation(EXIT_ON_CLOSE);
        ui.setVisible(true);
    }


    class AddListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		if((ISBN.getText().length()!=0)&&(Title.getText().length()!=0)) {
    			for(int i=0; i<library.size(); i++) {
    				if(library.get(i).getISBN().equals(ISBN.getText())) {
    					JOptionPane.showMessageDialog(null,"This ISBN has been occupied by another book in the library!");
    					return;
    				}
    			}
    			Book newBook = new Book();
    			newBook.setAvailable(true);
    			newBook.setISBN(ISBN.getText());
    			newBook.setTitle(Title.getText());
    			library.add(newBook);
    		} else {
    			JOptionPane.showMessageDialog(null,"Please fill in both ISBN and Title!");
    		}
    	}
    }
    
    class loadTestDataListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		Book book1 = new Book();
    		book1.setAvailable(true);
    		book1.setISBN("0131450913");
    		book1.setTitle("HTML How to Program");
    		Book book2 = new Book();
    		book2.setAvailable(true);
    		book2.setISBN("0131857576");
    		book2.setTitle("C++ How to Program");
    		Book book3 = new Book();
    		book3.setAvailable(true);
    		book3.setISBN("0132222205");
    		book3.setTitle("Java How to Program");
			library.add(book1);
			library.add(book2);
			library.add(book3);
    	}
    }

    class RefleshTable implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] columnNames = { "ISBN", "Title", "Available" };
            DefaultTableModel model = new DefaultTableModel();
            for (String name: columnNames) {
                model.addColumn(name);
            }

                for (int i = 0; i < library.size(); i++){
                    String ISBN = library.get(i).getISBN();
                    String Title = library.get(i).getTitle();
                    boolean Available = library.get(i).isAvailable();
                    Object[] x = {ISBN, Title, Available};
                    model.addRow(x);
                }
                bookTable.setModel(model);
        }
    }
}


