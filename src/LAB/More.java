//LI Haoyang
//17083702D
//JDK14
//IntelliJ IDEA
package LAB;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class More extends JFrame {
	protected JButton borrow = new JButton("Borrow");
	protected JButton bringBack = new JButton("Return");    //"return" is reserved in java, so this variable is named as "bringBack"
	protected JButton reserve = new JButton("Reserve");
	protected JButton waitingQueue = new JButton("Waiting Queue");
	protected JTextArea notification = new JTextArea();
	protected JTextArea upperPanel;
	protected String ISBN, title;
	protected MyLinkedList<Book> library;
	boolean available;
	protected JButton addImage = new JButton("Add Book Image (PNG only)");
	protected ImageIcon imageIcon = new ImageIcon();
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
		middlePanel.add(addImage);
		if (available) {
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

//	public void switchImage(){
////		JPanel ImageLayer = new JPanel();
//		BufferedImage image = null;
//		for (int i = 0; i < library.size(); i++) {
//			if (library.get(i).getISBN().equals(ISBN)) {
//				image = library.get(i).getImage();
//			}
//			}
//
//		imageIcon.setImage(image);
////		ImageLayer.add(imageIcon);
//	}

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
		add(new JLabel(imageIcon), BorderLayout.EAST);
		actionLoader();
	}

	public void actionLoader() {
		ActionListener borrowListener = new BorrowListener();
		ActionListener reserveListener = new ReserveListener();
		ActionListener waitingQueueListener = new WaitingQueueListener();
		ActionListener bringBackListener = new BringBackListener();
		ActionListener imageListener = new AddImageListener();

		borrow.addActionListener(borrowListener);
		reserve.addActionListener(reserveListener);
		waitingQueue.addActionListener(waitingQueueListener);
		bringBack.addActionListener(bringBackListener);
		addImage.addActionListener(imageListener);
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
					if (name != null) {
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
			String temp = "The waiting queue:\n";
			if (queue.getSize() > 0) {
				for (int i = 0; i < queue.getSize(); i++) {
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
					if (library.get(i).getReservedQueue().getSize() > 0) {
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

	class AddImageListener implements ActionListener {

		String fileSelector() {
			String location = null;
			JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
			int r = fileChooser.showOpenDialog(null);
			if (r == JFileChooser.APPROVE_OPTION) {
				location = fileChooser.getSelectedFile().getAbsolutePath();
			}
			return location;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String path = fileSelector();
			File file = new File(path);
				BufferedImage image = null;

				try {
					image = ImageIO.read(file);
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}

				for (int i = 0; i < library.size(); i++) {
					if (library.get(i).getISBN().equals(ISBN)) {
						library.get(i).setImage(image);
						imageIcon.setImage(image);
				}
			}
		}
	}
}
