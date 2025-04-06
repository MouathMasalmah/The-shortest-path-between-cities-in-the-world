module com.example.shortestPath {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.shortestPath to javafx.fxml;
    exports com.example.shortestPath;
}
