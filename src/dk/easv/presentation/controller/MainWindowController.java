package dk.easv.presentation.controller;

import dk.easv.presentation.model.AppModel;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import dk.easv.entities.*;
import java.util.Random;
public class MainWindowController {

    private AppModel appModel;

    @FXML
    private ScrollPane scrollPane1;

    @FXML
    private ScrollPane scrollPane2;

    @FXML
    private ScrollPane scrollPane3;

    @FXML
    private ScrollPane scrollPane4;

    @FXML
    public void initialize() {
        appModel = AppModel.getInstance();
        User loggedInUser = appModel.getObsLoggedInUser();

        if (loggedInUser != null) {
            appModel.loadData(loggedInUser);
            System.out.println(loggedInUser);
            System.out.println(appModel.getObsTopMovieSeen().toString());
            initializeScrollPane(scrollPane1, appModel.getObsTopMovieNotSeen());
            initializeScrollPane(scrollPane2, appModel.getObsTopMovieSeen());
            initializeScrollPane(scrollPane3, appModel.getObsTopMovieNotSeen());
            initializeScrollPane(scrollPane4, appModel.getObsTopMovieNotSeen());
            appModel.loadUsers();
        }
    }




    private void initializeScrollPane(ScrollPane scrollPane, ObservableList<Movie> movies) {
        // Create an HBox to hold the content
        HBox contentBox = new HBox();
        contentBox.setSpacing(10); // Adjust spacing between buttons

        // Add initial movies to the contentBox
        for (Movie movie : movies) {
            addButton(contentBox, movie);
        }

        // Listen for changes in the list of movies
        movies.addListener((ListChangeListener.Change<? extends Movie> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Movie movie : change.getAddedSubList()) {
                        addButton(contentBox, movie);
                    }
                } else if (change.wasRemoved()) {
                    // Remove corresponding buttons for removed movies
                    contentBox.getChildren().removeIf(node -> {
                        if (node instanceof Button) {
                            Movie movie = (Movie) node.getUserData();
                            return change.getRemoved().contains(movie);
                        }
                        return false;
                    });
                }
            }
        });

        // Set the content of the scroll pane to the HBox
        scrollPane.setContent(contentBox);

        // Enable horizontal scrolling
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    }

    private void addButton(HBox contentBox, Movie movie) {
        Button button = new Button(movie.getTitle() + " (" + movie.getYear() + ")");
        button.setPrefSize(150, 200); // Set fixed size for the button
        button.setOnAction(event -> {
            // Action to perform when the button is clicked
            System.out.println("Button clicked for movie: " + movie.getTitle());
        });
        button.setUserData(movie); // Store movie data in the button
        contentBox.getChildren().add(button);
    }

    // Method to create a placeholder thumbnail
    private Pane createThumbnail() {
        Pane thumbnail = new Pane();
        thumbnail.setPrefSize(150, 150); // Set thumbnail size

        // Placeholder rectangle (you would replace this with an actual image)
        Rectangle rectangle = new Rectangle(140, 140, Color.LIGHTGRAY);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(1);

        thumbnail.getChildren().add(rectangle);
        return thumbnail;
    }

    public void setModel(AppModel model) {
        this.appModel = model;
    }
}
