//CHAN CHEUK YIU
//17067305D
//JDK14
//IntelliJ IDEA
package LAB;

import java.awt.image.BufferedImage;

public class Book {
    private String title; // store the title of the book
    private String ISBN; // store the ISBN of the book
    private boolean available; // keep the status of whether the book is available;
    // initially should be true
    private MyQueue<String> reservedQueue = new MyQueue<>(); // store the queue of waiting list
    private BufferedImage image;

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public MyQueue<String> getReservedQueue() {
        return reservedQueue;
    }

    public void setReservedQueue(MyQueue<String> reservedQueue) {
        this.reservedQueue = reservedQueue;
    }
}
