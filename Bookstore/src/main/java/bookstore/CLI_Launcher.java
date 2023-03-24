package bookstore;

import client_toolkit.CLI_toolkit;
import javafx.application.Application;
import javafx.stage.Stage;

public class CLI_Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            CLI_toolkit.startCLI();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        // Do any cleanup or resource releasing here
    }
}
