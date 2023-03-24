package bookstore;

import client_toolkit.CLIENT_toolkit;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import json_toolkit.Book;

import java.io.IOException;
import java.util.Objects;

public class bookWindowController {

    CLIENT_toolkit cToolkit = new CLIENT_toolkit();
    private Book mybook;

        @FXML
        private Button add_to_cart_button;

        @FXML
        private Label authors;

        @FXML
        private Label isbn;

        @FXML
        private Label price;

        @FXML
        private Label qty;

        @FXML
        private Label title;

    public bookWindowController() throws IOException {
    }

    public void setData(Book book) {
            title.setText(book.getTitle());
            isbn.setText(book.getIsbn());
            price.setText(String.valueOf(book.getPrice()));
            qty.setText(String.valueOf(book.getStock()));
            StringBuilder authors_text = new StringBuilder();
            for (String author : book.getAuthors()){
                authors_text.append(author);
                if (!Objects.equals(author, book.getAuthors()[book.getAuthors().length - 1])) {
                    authors_text.append(", ");
                }
            }
            authors.setText(authors_text.toString());
            mybook = book;
        }

        public void addToCart(ActionEvent event) {
            CLIENT_toolkit.addBookToCart(mybook);
            System.out.println("Book added to cart: " + mybook.getTitle());

        }
}
