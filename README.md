# Demonstration 1

# Communication:

All requests from the Client will be sent via JSON messages. The request messages will contain two fields, the request type and the request data. The type will state what the request is, and the data will be a book object that contains the data should it be nececarry, otherwise this data will be empth but present.

```json
{"requestType": "MY_REQUEST", 
"requestData": ["myData1": "DataValue1", "myData2": "DataValue2"]}
```

The request type can be any of the following:

```java
GET_ALL_BOOKS,
ADD_BOOK,
REMOVE_BOOK,
EDIT_BOOK,
```

GET_BOOKS:

This receives all books back from the server. The request data should then be:

```json
{"requestType": "GET_BOOKS",
"requestData": ["filterBy": "catagory/none"]}
```

The response data will then be:

```json
{ "books": [["ISBN": "isbn1", "Title": "title1", "Authors": ["author. 1", "author. 2"], 
						"Year": "1234", "Catagory": "Fiction/Non_Fiction/Textbook", "Price": "12.3",
						"Qty": "1"],
						["ISBN": "isbn1", "Title": "title1", "Authors": ["author. 1", "author. 2"], 
						"Year": "1234", "Catagory": "Fiction/Non_Fiction/Textbook", "Price": "12.3",
						"Qty": "1"]]
}
```

The book class should look like:

```java
public class book {
	private int ISBN;
	private String Title;
	private String[] Authors;
	private int Year;
	private String Catagory;
	private int Price;
	private int Qty;

	// add constructor
	public void book (int ISBN, String Title, String[] Authors, int year, String Catagory, int Price, int Qty) {
	this.ISBN = ISBN;
	this.Title = Title;
	this.Authers = Authers;
	this.year = year;
	this.Catagory = Catagory;
	this.Price = Price;
	this.Qty = Qty;
}
```

# JSON Parser:

The JSON parser class should be able to do the following:

- return string with request, request data and formatting.
- return string with the request type seperated and request data seperated

# Client:

## Login Page:

```java
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");

        // Create the login form grid pane
        GridPane loginGrid = new GridPane();
        loginGrid.setPadding(new Insets(20, 20, 20, 20));
        loginGrid.setVgap(10);
        loginGrid.setHgap(10);

        // Add the username label and field
        Label usernameLabel = new Label("Username:");
        GridPane.setConstraints(usernameLabel, 0, 0);
        TextField usernameField = new TextField();
        GridPane.setConstraints(usernameField, 1, 0);

        // Add the password label and field
        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 1);
        PasswordField passwordField = new PasswordField();
        GridPane.setConstraints(passwordField, 1, 1);

        // Add the login button
        Button loginButton = new Button("Login");
        GridPane.setConstraints(loginButton, 1, 2);

        // Add the "New User" button
        Button newUserButton = new Button("New User");
        GridPane.setConstraints(newUserButton, 0, 2);

        // Add the "Forgot Password" link
        Hyperlink forgotPasswordLink = new Hyperlink("Forgot Password?");
        GridPane.setConstraints(forgotPasswordLink, 1, 3);

        // Add all components to the login grid
        loginGrid.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton, newUserButton, forgotPasswordLink);

        // Create a login action for the login button
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (username.equals("admin") && password.equals("password")) {
                System.out.println("Login successful!");
                // TODO: replace with your own code to open the main app window
            } else {
                System.out.println("Login failed!");
            }
        });

        // Create a new user form and action for the "New User" button
        GridPane newUserGrid = new GridPane();
        newUserGrid.setPadding(new Insets(20, 20, 20, 20));
        newUserGrid.setVgap(10);
        newUserGrid.setHgap(10);

        Label newUsernameLabel = new Label("New Username:");
        GridPane.setConstraints(newUsernameLabel, 0, 0);
        TextField newUsernameField = new TextField();
        GridPane.setConstraints(newUsernameField, 1, 0);

        Label newPasswordLabel = new Label("New Password:");
        GridPane.setConstraints(newPasswordLabel, 0, 1);
        PasswordField newPasswordField = new PasswordField();
        GridPane.setConstraints(newPasswordField, 1, 1);

        Button createUserButton = new Button("Create User");
        GridPane.setConstraints(createUserButton, 1, 2);

        newUserGrid.getChildren().addAll(newUsernameLabel, newUsernameField, newPasswordLabel, newPasswordField, createUserButton);

        newUserButton.setOnAction(event -> {
	          Stage newUserStage = new Stage();
						newUserStage.setTitle("New User");
		        Scene newUserScene = new Scene(newUserGrid, 300, 150);
		        newUserStage.setScene(newUserScene);
		        newUserStage.show();

        // Create a new user action for the create user button
        createUserButton.setOnAction(newUserEvent -> {
            String newUsername = newUsernameField.getText();
            String newPassword = newPasswordField.getText();
            // TODO: replace with your own code to create a new user in the database
            System.out.println("New user created: " + newUsername + "/" + newPassword);
            newUserStage.close();
        });
    });

    // Create a forgot password form and action for the "Forgot Password" link
    GridPane forgotPasswordGrid = new GridPane();
    forgotPasswordGrid.setPadding(new Insets(20, 20, 20, 20));
    forgotPasswordGrid.setVgap(10);
    forgotPasswordGrid.setHgap(10);

    Label forgotUsernameLabel = new Label("Username:");
    GridPane.setConstraints(forgotUsernameLabel, 0, 0);
    TextField forgotUsernameField = new TextField();
    GridPane.setConstraints(forgotUsernameField, 1, 0);

    Label forgotEmailLabel = new Label("Email Address:");
    GridPane.setConstraints(forgotEmailLabel, 0, 1);
    TextField forgotEmailField = new TextField();
    GridPane.setConstraints(forgotEmailField, 1, 1);

    Button resetPasswordButton = new Button("Reset Password");
    GridPane.setConstraints(resetPasswordButton, 1, 2);

    forgotPasswordGrid.getChildren().addAll(forgotUsernameLabel, forgotUsernameField, forgotEmailLabel, forgotEmailField, resetPasswordButton);

    forgotPasswordLink.setOnAction(event -> {
        Stage forgotPasswordStage = new Stage();
        forgotPasswordStage.setTitle("Forgot Password");
        Scene forgotPasswordScene = new Scene(forgotPasswordGrid, 300, 150);
        forgotPasswordStage.setScene(forgotPasswordScene);
        forgotPasswordStage.show();

        // Create a reset password action for the reset password button
        resetPasswordButton.setOnAction(resetEvent -> {
            String forgotUsername = forgotUsernameField.getText();
            String forgotEmail = forgotEmailField.getText();
            // TODO: replace with your own code to reset the user's password and send an email with the new password
            System.out.println("Password reset: " + forgotUsername + " (" + forgotEmail + ")");
            forgotPasswordStage.close();
        });
    });

    // Create the login scene and show the stage
    Scene loginScene = new Scene(loginGrid, 300, 150);
    primaryStage.setScene(loginScene);
    primaryStage.show();
}

public static void main(String[] args) {
    launch(args);
}
```

# External Page Links:

[Demonstration 1](Demonstration%201%201a1ddd574e4640a5870f7b74b03b42ec.md)

[Java OOP upskill Project: Project Brief.pdf](Demonstration%201%201a1ddd574e4640a5870f7b74b03b42ec/HCC874_2023_JavaOOPupskillProject_ProjectBrief.pdf)
