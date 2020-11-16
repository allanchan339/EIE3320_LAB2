//CHAN CHEUK YIU
//17067305D
//JDK14
//IntelliJ IDEA

package LAB;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
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
        String User2 = "Student Name and ID: CHAN CHEUK YIU (17067305d)";
        Date date = new Date();
        textArea.append(User1+"\n"+User2+"\n"+date+"\n\n");
//        upperPanel.add(textArea);
        return textArea;
    }

    public JScrollPane createMiddlePanel(){
//        JPanel middlePanel = new JPanel();
        String[] columnNames = { "ISBN", "Title", "Available" };

        TableModel model = new DefaultTableModel();

        TableColumnModel columnModel = new DefaultTableColumnModel();
        TableColumn firstColumn = new TableColumn(1);
        firstColumn.setHeaderValue(columnNames[0]);
        columnModel.addColumn(firstColumn);

        TableColumn secondColumn = new TableColumn(0);
        secondColumn.setHeaderValue(columnNames[1]);
        columnModel.addColumn(secondColumn);

        TableColumn thirdColumn = new TableColumn(0);
        thirdColumn.setHeaderValue(columnNames[2]);
        columnModel.addColumn(thirdColumn);

        JTable jTable = new JTable(model, columnModel);

        JScrollPane jScrollPane = new JScrollPane(jTable);
//        middlePanel.add(jScrollPane);
        return jScrollPane;
    }
    public UI(){
        JPanel lowerPanel = createLowerPanel();
        JTextArea upperPanel = createUpperPanel();
        JScrollPane middlePanel = createMiddlePanel();

        setLayout(new BorderLayout(0,0));
        add(upperPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(lowerPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        UI ui = new UI();
        ui.setTitle("Library Admin System");
        ui.setSize(800,400);
        ui.setLocationRelativeTo(null);
        ui.setDefaultCloseOperation(EXIT_ON_CLOSE);
        ui.setVisible(true);
    }

}
