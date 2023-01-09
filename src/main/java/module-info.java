module com.example.vstron {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;
    requires javatuples;

    opens tronGame to javafx.fxml;
    exports tronGame;
}