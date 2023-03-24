package bookstore;

import client_toolkit.CLIENT_toolkit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BookstoreApplication extends Application {

    private static Stage stg;
    @Override
    public void start(Stage primaryStage) throws IOException {
        stg = primaryStage;
        stg.setWidth(700);
        stg.setHeight(500);
        CLIENT_toolkit.connectClient();
        primaryStage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(BookstoreApplication.class.getResource("login_screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        primaryStage.setTitle("NOW Bookstore");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
        if (fxml.equals("login_screen.fxml")) {
            stg.setHeight(500);
            stg.setWidth(700);
        } else {
            stg.setHeight(800);
            stg.setWidth(1080);
        }
    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}