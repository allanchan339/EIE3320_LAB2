//LI Haoyang
//17083702D
//JDK14
//IntelliJ IDEA
package LAB;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class More extends JFrame{
	protected JButton borrow = new JButton("Borrow");
	protected JButton bringBack = new JButton("Return");	//"return" is reserved in java, so this variable is named as "bringBack"
	protected JButton reserve = new JButton("Reserve");
	protected JButton waitingQueue = new JButton("Waiting Queue");
	protected JTextArea notification = new JTextArea();
	protected JTextArea upperPanel;
	protected String ISBN, title;
	protected MyLinkedList<Book> library;
	boolean available;
	
	public JTextArea createUpperPanel() {
        JTextArea textArea = new JTextArea();
        textArea.append("ISBN: " + ISBN + "\nTitle: " + title + "\nAvailable: " + available + "\n");
        return textArea;
    }
	
	public JPanel createMiddlePanel() {
		JPanel middlePanel = new JPanel();
		middlePanel.add(borrow);
		middlePanel.add(bringBack);
		middlePanel.add(reserve);
		middlePanel.add(waitingQueue);
		if(available) {
			borrow.setEnabled(true);
			bringBack.setEnabled(false);
			reserve.setEnabled(false);
			waitingQueue.setEnabled(false);
		} else {
			borrow.setEnabled(false);
			bringBack.setEnabled(true);
			reserve.setEnabled(true);
			waitingQueue.setEnabled(true);
		}
		return middlePanel;
	}
	
	public More(String ISBN, String title, boolean available, MyLinkedList<Book> library) {
		this.ISBN = ISBN;
		this.title = title;
		this.available = available;
		this.library = library;
		upperPanel = createUpperPanel();
		JPanel middlePanel = createMiddlePanel();
		
		setLayout(new BorderLayout(0, 0));
        add(upperPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(notification, BorderLayout.SOUTH);
        
        actionLoader();
	}
	
	public void actionLoader() {
		ActionListener borrowListener = new BorrowListener();
		ActionListener reserveListener = new ReserveListener();
		ActionListener waitingQueueListener = new WaitingQueueListener();
		ActionListener bringBackListener = new BringBackListener();
		
		borrow.addActionListener(borrowListener);
		reserve.addActionListener(reserveListener);
		waitingQueue.addActionListener(waitingQueueListener);
		bringBack.addActionListener(bringBackListener);
	}
	
	class BorrowListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			available = false;
			borrow.setEnabled(false);
			bringBack.setEnabled(true);
			reserve.setEnabled(true);
			waitingQueue.setEnabled(true);
			for (int i = 0; i < library.size(); i++) {
                if (library.get(i).getISBN().equals(ISBN)) {
                	library.get(i).setAvailable(false);
                	break;
                }
            }
			upperPanel.setText("ISBN: " + ISBN + "\nTitle: " + title + "\nAvailable: " + available + "\n");
			notification.setText("The book is borrowed.");
		}
	}
	
	class ReserveListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String name = JOptionPane.showInputDialog("What's your name?");
			for (int i = 0; i < library.size(); i++) {
                if (library.get(i).getISBN().equals(ISBN)) {
                	if(name!=null) {
                		library.get(i).getReservedQueue().enqueue(name);
                		notification.setText("The book is reserved by " + name + ".");
                    	break;
                	}
                }
            }
		}
	}

	class WaitingQueueListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			MyQueue<String> queue = new MyQueue<>();
			for (int i = 0; i < library.size(); i++) {
                if (library.get(i).getISBN().equals(ISBN)) {
                	queue = library.get(i).getReservedQueue();
                	break;
                }
            }
			String temp= new String("The waiting queue:\n");
			if(queue.getSize()>0) {
				for(int i = 0; i < queue.getSize(); i++) {
					temp += (queue.getList().get(i) + "\n");
				}
			} else {
				temp = "The queue is now empty";
			}
			notification.setText(temp);
		}
	}
	
	class BringBackListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			for (int i = 0; i < library.size(); i++) {
                if (library.get(i).getISBN().equals(ISBN)) {
                	if(library.get(i).getReservedQueue().getSize()>0) {
                		String name = library.get(i).getReservedQueue().dequeue();
                		notification.setText("The book is borrowed.\nThe book is now borrowed by " + name);
                	} else {
                		library.get(i).setAvailable(true);
                		borrow.setEnabled(true);
            			bringBack.setEnabled(false);
            			reserve.setEnabled(false);
            			waitingQueue.setEnabled(false);
            			available = true;
            			upperPanel.setText("ISBN: " + ISBN + "\nTitle: " + title + "\nAvailable: " + available + "\n");
            			notification.setText("The book is returned.");
                	}
                }
            }
		}
	}
}
