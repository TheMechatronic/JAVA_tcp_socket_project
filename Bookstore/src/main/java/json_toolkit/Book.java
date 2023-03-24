package json_toolkit;

import org.bson.types.ObjectId;

import java.util.Arrays;

public class Book {

    private ObjectId _id;
    private String isbn;
    private String title;
    private String[] authors;
    private int year;
    private int stock;
    private double price;

    public Book() {}

    public Book(ObjectId _id, String isbn, String title, String[] authors, int year, int stock, double price) {
        this._id = _id;
        this.isbn = isbn;
        this.title = title;
        this.authors = authors;
        this.year = year;
        this.stock = stock;
        this.price = price;
    }

    // getters and setters for all fields
    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // toString method for printing the json_toolkit.Book object
    @Override
    public String toString() {
        return "json_toolkit.Book{" +
                "_id=" + _id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", authors=" + Arrays.toString(authors) +
                ", year=" + year +
                ", stock=" + stock +
                ", price=" + price +
                '}';
    }
}
