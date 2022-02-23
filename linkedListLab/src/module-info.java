module plans {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens org.headroyce.ronn2023 to javafx.fxml;
    exports org.headroyce.ronn2023;
}