package dk.easv.presentation.controller;

import dk.easv.entities.User;
import dk.easv.presentation.model.AppModel;
import dk.easv.presentation.util.HttpService;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import dk.easv.entities.Movie;

import java.io.IOException;

public class MainWindowController {
    private final HttpService httpService = new HttpService();
    private final String mainApiLink = "https://image.tmdb.org/t/p/original";
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

        // Asynchronously load actual images
        new Thread(() -> {
            loadImages(scrollPane1);
            loadImages(scrollPane2);
            loadImages(scrollPane3);
            loadImages(scrollPane4);
        }).start();
    }

    private void initializeScrollPane(ScrollPane scrollPane, ObservableList<Movie> movies) {
        HBox contentBox = new HBox();
        contentBox.setSpacing(10);

        for (Movie movie : movies) {
            addButton(contentBox, movie);
        }

        scrollPane.setContent(contentBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    }

    private void addButton(HBox contentBox, Movie movie) {
        Button button = new Button(movie.getTitle() + " (" + movie.getYear() + ")");
        double buttonWidth = 120;
        double buttonHeight = 120;
        button.setPrefSize(buttonWidth, buttonHeight);
        button.setOnAction(event -> System.out.println("Button clicked for movie: " + movie.getTitle()));
        button.setUserData(movie);
        button.getStyleClass().add("button"); // Add a CSS class to identify buttons later

        // Set a placeholder image
        button.setStyle("-fx-background-image: url('/placeholder.png'); " +
                "-fx-background-size: stretch;");

        contentBox.getChildren().add(button);
    }

    private void loadImages(ScrollPane scrollPane) {
        for (Node node : scrollPane.getContent().lookupAll(".button")) {
            Button button = (Button) node;
            Movie movie = (Movie) button.getUserData();
            loadPosterImage(button, movie);
        }
    }

    private void loadPosterImage(Button button, Movie movie) {
        try {
            String jsonResponse = httpService.searchMovie(movie.getTitle());
            if (jsonResponse != null) {
                String posterPath = httpService.getPosterPath(jsonResponse);
                if (posterPath != null) {
                    String imageUrl = mainApiLink + posterPath;
                    Platform.runLater(() -> {
                        button.setStyle("-fx-background-image: url('" + imageUrl + "');" +
                                "-fx-background-size: stretch;");
                    });
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setModel(AppModel model) {
        this.appModel = model;
    }
}
