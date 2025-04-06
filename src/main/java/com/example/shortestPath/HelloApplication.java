package com.example.shortestPath;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class HelloApplication extends Application {
    private Dijkstra dijkstra = new Dijkstra(); // Example initialization, use the correct number
    private File file = null;
    private int w = 1200;
    private int h = 800;

    private final Group pointsGroup = new Group(); // Group for points (remains static)
    private final Group linesGroup = new Group(); // Group for lines (dynamic)

    public void InsertTheCountry(Group map, ChoiceBox<String> src_combo, ChoiceBox<String> dest_combo, TextArea path_field) {
        map.getChildren().clear(); // Clear the map for new cities
        for (Vertix city : dijkstra.getCities()) {
            // Create a circle to represent the city
            Circle circle = new Circle();
            circle.setRadius(3); // Set the radius of the circle (can be adjusted)
            circle.setFill(Color.RED); // Set the color of the circle

            // Set the circle's position based on the city's coordinates
            circle.setCenterX(city.getCity().getX());
            circle.setCenterY(city.getCity().getY());

            // Add click event to set the source and destination
            circle.setOnMouseClicked(e -> {
                if (src_combo.getValue() == null) {
                    src_combo.setValue(city.getCity().getName());
                } else if (dest_combo.getValue() == null) {
                    dest_combo.setValue(city.getCity().getName());
                } else {
                    path_field.setText("Source and Destination are full");
                    Alert alert = new Alert(Alert.AlertType.NONE, "Source and Destination are Full", ButtonType.OK);
                    alert.showAndWait();
                }
            });

            // Create text to display the city name
            Text cityText = new Text(city.getCity().getName());
            cityText.setFill(Color.WHITE); // Set text color to white
            cityText.setFont(Font.font("Arial", 12)); // Set font size to 12

            // Add DropShadow effect to make the text stand out
            DropShadow shadow = new DropShadow();
            shadow.setColor(Color.BLACK);
            shadow.setRadius(3);
            shadow.setOffsetX(2);
            shadow.setOffsetY(2);
            cityText.setEffect(shadow);

            // Position the text slightly above the circle to avoid overlap
            cityText.setLayoutX(city.getCity().getX() + 5);  // Adjust to the right of the circle
            cityText.setLayoutY(city.getCity().getY() - 5);  // Adjust slightly above the circle

            // Add the circle and text to the map
            map.getChildren().addAll(circle, cityText);
        }
    }

    @Override
    public void start(Stage stage) {
        Pane mainPane = new Pane();
        Image mh8 = new Image("World-Map.jpg");
        ImageView mah8 = new ImageView(mh8);
        mah8.setFitWidth(w);
        mah8.setFitHeight(h);

        ChoiceBox<String> srcChoiceBox = new ChoiceBox<>();
        srcChoiceBox.setPrefHeight(25);
        srcChoiceBox.setPrefWidth(150);
        srcChoiceBox.setLayoutX(1236);
        srcChoiceBox.setLayoutY(27);

        ChoiceBox<String> destChoiceBox = new ChoiceBox<>();
        destChoiceBox.setPrefHeight(25);
        destChoiceBox.setPrefWidth(150);
        destChoiceBox.setLayoutX(1236);
        destChoiceBox.setLayoutY(92);

        ChoiceBox<String> criteriaCombo = new ChoiceBox<>();
        criteriaCombo.setPrefHeight(25);
        criteriaCombo.setPrefWidth(150);
        criteriaCombo.setLayoutX(1236);
        criteriaCombo.setLayoutY(157);
        ObservableList<String> criteriaOptions = FXCollections.observableArrayList("distance", "time", "cost");
        criteriaCombo.setItems(criteriaOptions);
        criteriaCombo.setValue("distance");

        TextArea textArea = new TextArea();
        textArea.setPrefWidth(300);
        textArea.setPrefHeight(157);
        textArea.setLayoutX(1236);
        textArea.setLayoutY(222);
        textArea.setEditable(false);

        // Create labels and text fields for the results
        Label distanceLabel = new Label("Total Distance:");
        distanceLabel.setLayoutX(1236);
        distanceLabel.setLayoutY(400);
        distanceLabel.setStyle("-fx-font-size: 12px;");

        TextField distanceField = new TextField();
        distanceField.setLayoutX(1350);
        distanceField.setLayoutY(400);
        distanceField.setPrefWidth(150);
        distanceField.setEditable(false);

        Label timeLabel = new Label("Total Time:");
        timeLabel.setLayoutX(1236);
        timeLabel.setLayoutY(470);
        timeLabel.setStyle("-fx-font-size: 12px;");

        TextField timeField = new TextField();
        timeField.setLayoutX(1350);
        timeField.setLayoutY(470);
        timeField.setPrefWidth(150);
        timeField.setEditable(false);

        Label costLabel = new Label("Total Cost:");
        costLabel.setLayoutX(1236);
        costLabel.setLayoutY(540);
        costLabel.setStyle("-fx-font-size: 12px;");

        TextField costField = new TextField();
        costField.setLayoutX(1350);
        costField.setLayoutY(540);
        costField.setPrefWidth(150);
        costField.setEditable(false);

        Button button = new Button("Run");
        button.setPrefWidth(100);
        button.setPrefHeight(25);
        button.setLayoutX(1236);
        button.setLayoutY(600);
        button.setDisable(true);
        button.setOnAction(event -> {
            // Check if the source and destination cities are the same
            if (srcChoiceBox.getValue() != null && destChoiceBox.getValue() != null &&
                    srcChoiceBox.getValue().equals(destChoiceBox.getValue())) {
                textArea.setText("Source and destination cannot be the same city.");
                // If the cities are the same, show an alert
                showErrorAlert("Error", "Source and destination cannot be the same city.");
                return;  // Stop further execution
            }

            // Check if other values are not selected
            if (srcChoiceBox.getValue() == null || destChoiceBox.getValue() == null || criteriaCombo.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "You must choose source, destination, and criteria", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            try {
                // Run the Dijkstra algorithm
                dijkstra.findShortestPath(srcChoiceBox.getValue(), destChoiceBox.getValue(), criteriaCombo.getValue());

                // Check if a path exists
                if (dijkstra.getShortPath().isEmpty()) {
                    textArea.setText("No path found between " + srcChoiceBox.getValue() + " and " + destChoiceBox.getValue());
                    showErrorAlert("No path", "No path found between " + srcChoiceBox.getValue() + " and " + destChoiceBox.getValue());
                    return;
                }

                // Display results in TextArea
                textArea.setText("Shortest Path: " + "\n" + dijkstra.getShortPath());

                // Set values for Total Distance, Total Time, and Total Cost
                distanceField.setText(dijkstra.getTotalDistance() + " km");
                timeField.setText(dijkstra.getTotalTime() + " min");
                costField.setText("$" + dijkstra.getTotalCost());

                // Draw lines for the shortest path
                linesGroup.getChildren().clear(); // Clear any existing lines
                visualizePath(linesGroup); // Redraw the path

            } catch (Exception ex) {
                showErrorAlert("Error", "An error occurred while calculating the shortest path: " + ex.getMessage());
            }
        });

        Button loadButton = new Button("Load File");
        loadButton.setPrefWidth(100);
        loadButton.setPrefHeight(25);
        loadButton.setLayoutX(1236);
        loadButton.setLayoutY(650);
        loadButton.setOnAction(e -> {
            srcChoiceBox.setValue(null);
            destChoiceBox.setValue(null);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                try {
                    // Clear points and paths only for new cities
                    pointsGroup.getChildren().clear();
                    linesGroup.getChildren().clear();

                    // Read the file and populate data
                    dijkstra.read(file);
                    srcChoiceBox.getItems().clear();
                    destChoiceBox.getItems().clear();
                    srcChoiceBox.getItems().addAll(dijkstra.getCityNames());
                    destChoiceBox.getItems().addAll(dijkstra.getCityNames());
                    button.setDisable(false);

                    // Add city points to the map
                    InsertTheCountry(pointsGroup, srcChoiceBox, destChoiceBox, textArea);

                    // Show success message
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "File loaded successfully!", ButtonType.OK);
                    successAlert.showAndWait();
                } catch (Exception ex) {
                    showErrorAlert("Error Loading File", ex.getMessage());
                }
            } else {
                showErrorAlert("No File Selected", "Please select a valid file to load.");
            }
        });

        // Clear Button
        Button clearButton = new Button("Clear");
        clearButton.setPrefWidth(100);
        clearButton.setPrefHeight(25);
        clearButton.setLayoutX(1236);
        clearButton.setLayoutY(700);
        clearButton.setOnAction(e -> {
            linesGroup.getChildren().clear(); // Clear only the lines (paths)
            textArea.clear(); // Clear the text area
            srcChoiceBox.setValue(null); // Clear source selection
            destChoiceBox.setValue(null); // Clear destination selection
            distanceField.clear(); // Clear distance field
            timeField.clear(); // Clear time field
            costField.clear(); // Clear cost field
        });

        mainPane.getChildren().addAll(mah8, pointsGroup, linesGroup, srcChoiceBox, destChoiceBox, criteriaCombo, textArea, button, loadButton, clearButton, distanceLabel, distanceField, timeLabel, timeField, costLabel, costField);
        Scene scene = new Scene(mainPane, 1500, 900);
        stage.setMaximized(true);
        scene.getStylesheets().add(getClass().getResource("/com/example/projectthreealgorthem/style.css").toExternalForm());
        stage.setTitle("Dijkstra's Algorithm");
        stage.setScene(scene);
        stage.show();
    }

    private void visualizePath(Group group) {
        Node currentEdgeNode = dijkstra.getShortestPathEdges().getFirstNode();  // Start with the first edge in the path
        while (currentEdgeNode != null) {
            Edge edge = currentEdgeNode.getEdge();  // Get the current edge

            // Get the source and destination cities for this edge
            City sourceCity = edge.getSource().getCity();
            City destCity = edge.getDest().getCity();

            // Create a line to represent the path between the two cities
            double x = sourceCity.getX();
            double y = sourceCity.getY();
            double x1 = destCity.getX();
            double y1 = destCity.getY();

            // Create the line
            Line line = new Line(x, y, x1, y1);
            line.setStroke(Color.RED);  // Set the color of the line
            line.setStrokeWidth(3);     // Set the width of the line

            // Apply shadow to the line
            DropShadow lineShadow = new DropShadow();
            lineShadow.setColor(Color.BLACK);
            lineShadow.setRadius(5);
            lineShadow.setOffsetX(2);
            lineShadow.setOffsetY(2);
            line.setEffect(lineShadow);  // Apply the shadow to the line

            // Add the line to the Group (to be displayed on the map)
            group.getChildren().add(line);

            // Calculate the midpoint for the arrow
            double midX = (x + x1) / 2;
            double midY = (y + y1) / 2;

            // Calculate the angle of the path
            double angle = Math.atan2(y1 - y, x1 - x);
            double arrowLength = 15; // Length of the arrowhead
            double arrowAngle = Math.PI / 6; // Angle for the arrowhead

            // Calculate the points for the two arrow lines
            double x1Arrow = midX - arrowLength * Math.cos(angle - arrowAngle);
            double y1Arrow = midY - arrowLength * Math.sin(angle - arrowAngle);

            double x2Arrow = midX - arrowLength * Math.cos(angle + arrowAngle);
            double y2Arrow = midY - arrowLength * Math.sin(angle + arrowAngle);

            // Create the two arrow lines
            Line arrowLine1 = new Line(midX, midY, x1Arrow, y1Arrow);
            arrowLine1.setStroke(Color.RED);
            arrowLine1.setStrokeWidth(2);

            Line arrowLine2 = new Line(midX, midY, x2Arrow, y2Arrow);
            arrowLine2.setStroke(Color.RED);
            arrowLine2.setStrokeWidth(2);

            // Apply shadow to the arrow lines
            DropShadow arrowShadow = new DropShadow();
            arrowShadow.setColor(Color.BLACK);
            arrowShadow.setRadius(3);
            arrowShadow.setOffsetX(1);
            arrowShadow.setOffsetY(1);
            arrowLine1.setEffect(arrowShadow);  // Apply shadow to arrowLine1
            arrowLine2.setEffect(arrowShadow);  // Apply shadow to arrowLine2

            // Add the arrow lines to the group
            group.getChildren().addAll(arrowLine1, arrowLine2);

            // Move to the next edge in the list
            currentEdgeNode = currentEdgeNode.getNext();
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Applying the custom CSS class to style the alert box
        alert.getDialogPane().getStyleClass().add("alert-error");

        alert.showAndWait();
    }



    public static void main(String[] args) {
        launch();
    }
}
