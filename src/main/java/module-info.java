module com.avs.book {
    requires org.kordamp.bootstrapfx.core;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.prefs;
    opens com.avs.book to javafx.fxml;
    exports com.avs.book;
    exports com.avs.book.controller;
    opens com.avs.book.controller to javafx.fxml;
}