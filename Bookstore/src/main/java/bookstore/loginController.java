package bookstore;

import client_toolkit.CLIENT_toolkit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;;

import java.io.IOException;

public class loginController {

    @FXML
    private Button new_user_button;
    @FXML
    private Button login_button;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label wrong_log_in;

    public loginController() throws IOException {
    }

    public void onEnter(ActionEvent event) throws Exception {
        checkLogin();
    }

    public void userLogin(ActionEvent event) throws Exception {
        checkLogin();
    }

    public void newUserCall(ActionEvent event) throws Exception {
        newUser();
    }

    private void checkLogin() throws Exception {
        BookstoreApplication m = new BookstoreApplication();

        if (username.getText().equals("")) {
            wrong_log_in.setText("Username cannot be empty");
        } else {
            int check_password = CLIENT_toolkit.logIn(username.getText(), password.getText());
            if (check_password == 0) {
                wrong_log_in.setText("Successful login");
                m.changeScene("main_store.fxml");
            } else if (check_password == 1) {
                // Incorrect Password
                wrong_log_in.setText("Incorrect Password");
            } else if (check_password == 2) {
                // username not recognised
                wrong_log_in.setText("Username not Recognised");
            }
        }
    }

    private void newUser() throws Exception {
        BookstoreApplication m = new BookstoreApplication();

        if (username.getText().equals("")) {
            wrong_log_in.setText("Username cannot be empty");
        } else {
            int username_check = CLIENT_toolkit.addUser(username.getText(), password.getText(), CLIENT_toolkit.client);
            if (username_check == 0) {
                wrong_log_in.setText("Successful login");
                m.changeScene("main_store.fxml");
            } else if (username_check == 2) {
                wrong_log_in.setText("Username already exists");
            } else {
                wrong_log_in.setText("Something went wrong ...");
            }
        }
    }
}
