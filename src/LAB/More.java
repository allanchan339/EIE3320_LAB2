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
import javax.swing.filechooser.FileFilter;

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
	protected JLabel imageShow = new JLabel();
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

	public More(String ISBN, String title, boolean available, MyLinkedList<Book> library) {
		this.ISBN = ISBN;
		this.title = title;
		this.available = available;
		this.library = library;
		upperPanel = createUpperPanel();
		imageShow.setSize(150,150);
		JPanel middlePanel = createMiddlePanel();
		setLayout(new BorderLayout(0, 0));
		add(upperPanel, BorderLayout.NORTH);
		add(middlePanel, BorderLayout.CENTER);
		add(notification, BorderLayout.SOUTH);
		setImageIcon();
//		JLabel imageShow = new JLabel();
//		imageShow.setIcon(imageIcon);
		add(imageShow, BorderLayout.WEST);
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
					if (!name.isBlank() && name != null) {
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

	void setImageIcon(){
		for (int i = 0; i < library.size(); i++) {
			if (library.get(i).getISBN().equals(ISBN)) {
				BufferedImage image = library.get(i).getImage();
				if (image == null) {
//					try{
						image = UI.getDefaultImage();
//						image = ImageIO.read(new File("./resource/default.png"));
//					}catch (IOException e){
//						e.getMessage();
//						image = UI.getDefaultImage();
//					}
				}
				imageIcon.setImage(image);
				imageShow.setIcon(imageIcon);
			}
		}
	}
	void setImageIcon(BufferedImage image){
		for (int i = 0; i < library.size(); i++) {
			if (library.get(i).getISBN().equals(ISBN)) {
				library.get(i).setImage(image);
				setImageIcon();
			}
		}
	}

	class AddImageListener implements ActionListener {

		String fileSelector() {
			String location = null;
			JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.addChoosableFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					if (f.isDirectory()) {
						return true;
					}
					else {
						String filename = f.getName().toLowerCase();
						return filename.endsWith(".png");
					}
				}

				@Override
				public String getDescription() {
					return "PNG Images (*.png)";
				}
			});
			int r = fileChooser.showOpenDialog(null);
			if (r == JFileChooser.APPROVE_OPTION) {
				location = fileChooser.getSelectedFile().getAbsolutePath();
//				System.out.println(location);
			} else {
				System.out.println("Selector closed");
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
					System.out.println("Failed to read image");
				}
				setImageIcon(image);
				JOptionPane.showMessageDialog(null, "Please reload the More>> page to refresh the new image");

		}
	}
}
