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

    public JTextArea createUpperPanel(){
        JTextArea textArea = new JTextArea();
        String User1 = "Student Name and ID: CHAN CHEUK YIU (17067305d)";
        String User2 = "Student Name and ID: LI Haoyang (17083702d)";
        Date date = new Date();
        textArea.append(User1+"\n"+User2+"\n"+date+"\n\n");
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

        actionLoader();
    }

    private void actionLoader(){
        ActionListener addListener = new AddListener();
        ActionListener loadTestDataListener = new loadTestDataListener();
        ActionListener editSaveActioner = new EditSaveActioner();
        ActionListener deleteListener = new DeleteListener();

        Add.addActionListener(addListener);
        LoadTestData.addActionListener(loadTestDataListener);
        Edit.addActionListener(editSaveActioner);
        Save.addActionListener(editSaveActioner);
        Delete.addActionListener(deleteListener);
    }
    public static void main(String[] args) {
        UI ui = new UI();
        ui.setTitle("Library Admin System");
        ui.setSize(800,400);
        ui.setLocationRelativeTo(null);
        ui.setDefaultCloseOperation(EXIT_ON_CLOSE);
        ui.setVisible(true);
    }


    private void refleshTable(){
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

    			refleshTable();
    		} else {
    			JOptionPane.showMessageDialog(null,"Please fill in both ISBN and Title!");
    		}
    	}
    }
    
    class loadTestDataListener implements ActionListener {

        boolean checkBook1(){
            Book book1 = new Book();
            book1.setAvailable(true);
            book1.setISBN("0131450913");
            book1.setTitle("HTML How to Program");
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
    	    if (checkBook1()&checkBook2()&checkBook3()){ // check if 3 book are already here
                JOptionPane.showMessageDialog(null, "Error: test data already exist in the current database");
            }
            refleshTable();
        }
    }

    class DeleteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = bookTable.getSelectedRow();
//            Book temp = new Book();
//            temp.setISBN((String)bookTable.getValueAt(index,0));
//            temp.setTitle((String)bookTable.getValueAt(index,1));
//            temp.setAvailable((Boolean)bookTable.getValueAt(index,2));
//            temp.setReservedQueue(library.get(index).getReservedQueue());
            library.remove(index);
            refleshTable();
        }
    }
    class EditSaveActioner implements ActionListener {
        void buttonSwitched(boolean flag){
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
            if (e.getSource() == Edit){
            String ISBNData = (String)bookTable.getValueAt(index,0);
            String titleData = (String)bookTable.getValueAt(index,1);
//            Boolean available = (Boolean)bookTable.getValueAt(index,2);

            ISBN.setText(ISBNData);
            Title.setText(titleData);
            buttonSwitched(true);
            }
            else if (e.getSource() == Save){
                String ISBNData = ISBN.getText();
                String titleData = Title.getText();
//                System.out.println(ISBNData+" "+titleData);
                boolean corrupt = false;
                for (Book book :
                        library) {
                    if (book.getISBN().equals(ISBNData)) {
//                        System.out.println(book.getISBN());
//                        System.out.println(ISBNData+" "+titleData);

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
                    library.set(index,temp);
                    buttonSwitched(false);
                    refleshTable();
                }
            }
        }
    }


}
