module sweng861 {
    requires javafx.controls;
    requires javafx.fxml;

    opens sweng861 to javafx.fxml;
    exports sweng861;
}
