module com.bookstore {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires org.mongodb.bson;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    opens bookstore to javafx.fxml;
    opens json_toolkit;
    exports bookstore;
    exports json_toolkit;
}