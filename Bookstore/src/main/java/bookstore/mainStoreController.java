package bookstore;

import client_toolkit.CLIENT_toolkit;
import client_toolkit.cTool;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import json_toolkit.Book;
import json_toolkit.User;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class mainStoreController implements Initializable {
    private static List<Book> books;
    private boolean isUserBooks = false;

    @FXML
    private GridPane books_grid;

    @FXML
    private Label books_title_tag;

    @FXML
    private Button refresh_button;

    @FXML
    private Button books_toggle_button;

    @FXML
    private Button buy_button;

    @FXML
    private GridPane cart_grid;

    @FXML
    private Button clear_cart_button;

    @FXML
    private Menu edit_button;

    @FXML
    private Menu file_button;

    @FXML
    private Menu help_button;

    @FXML
    private Button load_cart_button;

    @FXML
    private Button save_cart_button;

    @FXML
    private Label total_tag;

    @FXML
    private Label user_credits;

    @FXML
    private Label username_tag;

    @FXML
    private Button refresh_all_button;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateBooksAll();
        updateCart();
        try {
            updateUser();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void buyBooksCall(ActionEvent event) throws Exception {
        buyBooks();
        updateCart();
        updateUser();
    }

    public void toggleButton(ActionEvent event) throws Exception {
        toggle_books();
    }

    public void updateCartCall(ActionEvent event) {
        updateCart();
    }

    public void saveCartCall(ActionEvent event) throws Exception {
        CLIENT_toolkit.saveCart();
        updateCart();
    }

    public void loadCartCall(ActionEvent event) throws Exception {
        CLIENT_toolkit.loadCart();
        updateCart();
    }

    public void clearCartCall(ActionEvent event) {
        CLIENT_toolkit.clearCartBooks();
        updateCart();
    }

    public void logOutCall(ActionEvent event) throws Exception {
        logOut();
    }

    public void updateAllCall(ActionEvent event) throws Exception {
        refreshAll();
    }

    private void refreshAll() throws Exception {
        updateUser();
        updateCart();
        if (isUserBooks) {
            updateBooksUser();
        } else {
            updateBooksAll();
        }
    }

    private void logOut() throws Exception {
        CLIENT_toolkit.logOut();

        BookstoreApplication m = new BookstoreApplication();
        m.changeScene("login_screen.fxml");
    }

    private void updateUser() throws Exception {
        User user = CLIENT_toolkit.getUser(CLIENT_toolkit.client);
        username_tag.setText(user.getUsername());
        user_credits.setText(String.valueOf(user.getCredits()));
    }

    private void toggle_books() throws Exception {
        if(isUserBooks){
            // The user books are shown, show all books
            updateBooksAll();
            books_title_tag.setText("Shop Books");
            books_toggle_button.setText("User Books");
            isUserBooks = false;
        } else {
            // The shop books are displayed
            updateBooksUser();
            books_title_tag.setText("User Books");
            books_toggle_button.setText("Shop Books");
            isUserBooks = true;
        }
        updateUser();
    }

    private void buyBooks() throws Exception {
        CLIENT_toolkit.buyCartBooks(CLIENT_toolkit.client);
    }

    void updateCart() {
        try {

            System.out.println("Trying to update cart");

            books = new ArrayList<>(CLIENT_toolkit.getCartBooks());

            int columns = 0;
            int row = 1;
            double price = 0;

            cart_grid.getChildren().clear();

            for (Book book : books) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(mainStoreController.class.getResource("cart_book.fxml")); // FXML file
                VBox box = fxmlLoader.load();
                CartBookController bookController = fxmlLoader.getController(); // New controller
                System.out.println("Book controller created ...");
                bookController.setData(book);

                price += book.getPrice()*book.getStock();

                if (columns == 1) { // Change to see more cols
                    columns = 0;
                    ++row;
                }
                cart_grid.add(box, columns++, row);
                GridPane.setMargin(box, new Insets(10));
            }

            total_tag.setText(String.valueOf(price));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void updateBooksAll() {
        try {
            books = new ArrayList<>(booksAll());

            books_grid.getChildren().clear();

            int columns = 0;
            int row = 1;

            for (Book book : books) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("book_window.fxml"));
                VBox box = fxmlLoader.load();
                bookWindowController bookController = fxmlLoader.getController();
                bookController.setData(book);

                if (columns == 1) { // Change to see more cols
                    columns = 0;
                    ++row;
                }
                books_grid.add(box, columns++, row);
                GridPane.setMargin(box, new Insets(10));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void updateBooksUser() {
        try {
            books = new ArrayList<>(booksUser());

            books_grid.getChildren().clear();

            int columns = 0;
            int row = 1;

            for (Book book : books) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("user_book_window.fxml"));
                VBox box = fxmlLoader.load();
                bookWindowControllerUser bookController = fxmlLoader.getController();
                bookController.setData(book);

                if (columns == 1) { // Change to see more cols
                    columns = 0;
                    ++row;
                }
                books_grid.add(box, columns++, row);
                GridPane.setMargin(box, new Insets(10));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Book> booksUser() throws Exception {
        CLIENT_toolkit.getUserBooks(CLIENT_toolkit.client);
        cTool.printBookList(CLIENT_toolkit.getAllBooksList());
        return CLIENT_toolkit.getAllUserBooksList();
    }

    private List<Book> booksAll() throws Exception {
        CLIENT_toolkit.getAllBooks(CLIENT_toolkit.client);
        cTool.printBookList(CLIENT_toolkit.getAllBooksList());
        return CLIENT_toolkit.getAllBooksList();
    }

}
