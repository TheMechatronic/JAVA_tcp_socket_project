package bookstore;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import json_toolkit.Book;

public class CartBookController {
    private Book myBook;
    @FXML
    private Label price_tag;

    @FXML
    private Label qty_tag;

    @FXML
    private Label title_tag;

    public CartBookController() {
    }

    public void setData(Book book) {
        myBook = book;
        title_tag.setText(myBook.getTitle());
        qty_tag.setText(String.valueOf(myBook.getStock()));
        price_tag.setText(String.valueOf(myBook.getPrice()));
    }
}
