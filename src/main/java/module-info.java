module com.example.vstron {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;

    opens trongame to javafx.fxml;
    exports trongame;
}