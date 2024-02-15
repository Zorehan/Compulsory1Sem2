package dk.easv.presentation.controller;

import dk.easv.entities.TopMovie;
import dk.easv.entities.User;
import dk.easv.entities.UserSimilarity;
import dk.easv.presentation.model.AppModel;
import dk.easv.presentation.util.HttpService;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import dk.easv.entities.Movie;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainWindowController {
    /*
    Opretter alle FXML variabler og en instans af klasserne "HttpService(API-util klasse) og modellen
     */
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
/*
Initialize sætter useren til den man loggede ind med på loginskærmen, laver Scrollpanesne
knapperne til Scrollpanes og henter information, på en ny tråd i mellemtiden begynder den at hente billeder
fra API-en (dette er så programmet kan starte efter nødvendig data er hentet)
 */
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
            intializeScrollPaneUserSimilarity(scrollPane3, appModel.getObsSimilarUsers());
            initializeScrollPaneTopMovies(scrollPane4, appModel.getObsTopMoviesSimilarUsers());
            appModel.loadUsers();
        }

        // Asynchronously load actual images
        new Thread(() -> {
            loadImages(scrollPane1);
            loadImages(scrollPane2);
            loadImages(scrollPane4);
        }).start();
    }

/*
denne metode sætter en ScrollPane op baseret fra information fra Observablelisten hentet i Modellen
 */
    private void initializeScrollPane(ScrollPane scrollPane, ObservableList<Movie> movies) {
        HBox contentBox = new HBox();
        contentBox.setSpacing(10);

        for (Movie movie : movies) {
            addButton(contentBox, movie);
        }

        scrollPane.setContent(contentBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    private void intializeScrollPaneUserSimilarity(ScrollPane scrollPane, ObservableList<UserSimilarity> similarUsers)
    {
        HBox contentBox = new HBox();
        contentBox.setSpacing(10);

        for (UserSimilarity userSimilarity : similarUsers) {
            addButtonUserSimilarity(contentBox, userSimilarity);
        }

        scrollPane.setContent(contentBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    private void addButton(HBox contentBox, Movie movie) {
        Button button = new Button();
        double buttonWidth = 120;
        double buttonHeight = 120;
        button.setPrefSize(buttonWidth, buttonHeight);
        button.setOnAction(event -> System.out.println("Button clicked for movie: " + movie.getTitle()));
        button.setUserData(movie);
        button.getStyleClass().add("button"); // Add a CSS class to identify buttons later

        // Set a placeholder image
        button.setStyle("-fx-background-image: url('/placeholder.png'); " +
                "-fx-background-size: stretch;");

        Label titleLabel = new Label(movie.getTitle() + " (" + movie.getYear() + ")");
        titleLabel.setWrapText(false); // Do not allow text to wrap
        titleLabel.setMaxWidth(buttonWidth); // Limit the width to fit the button size
        titleLabel.setMaxHeight(buttonHeight); // Limit the height to fit one line
        titleLabel.getStyleClass().add("movieLabels"); // Add a CSS class to identify labels later
        titleLabel.setEllipsisString("..."); // Set ellipsis for truncated text

        // Create a VBox to hold the button and label
        VBox container = new VBox();
        container.getChildren().addAll(button, titleLabel);
        container.setSpacing(5); // Set spacing between button and label

        contentBox.getChildren().add(container);
    }

    private void addButtonUserSimilarity(HBox contentBox, UserSimilarity userSimilarity)
    {
        Button button = new Button();
        double buttonWidth = 120;
        double buttonHeight = 120;
        button.setPrefSize(buttonWidth,buttonHeight);
        button.setOnAction(event -> System.out.println("Button clicked for movie: " + userSimilarity.getName()));
        button.setUserData(userSimilarity);
        button.getStyleClass().add("button"); // Add a CSS class to identify buttons later

        // Set a placeholder image
        button.setStyle("-fx-background-image: url('/placeholderprofilepicture.jpg'); " +
                "-fx-background-size: stretch;");

        Label titleLabel = new Label(userSimilarity.getName());
        titleLabel.setWrapText(false); // Do not allow text to wrap
        titleLabel.setMaxWidth(buttonWidth); // Limit the width to fit the button size
        titleLabel.setMaxHeight(buttonHeight); // Limit the height to fit one line
        titleLabel.getStyleClass().add("movieLabels"); // Add a CSS class to identify labels later
        titleLabel.setEllipsisString("..."); // Set ellipsis for truncated text

        // Create a VBox to hold the button and label
        VBox container = new VBox();
        container.getChildren().addAll(button, titleLabel);
        container.setSpacing(5); // Set spacing between button and label

        contentBox.getChildren().add(container);
    }

    private void addButtonTopMovies(HBox contentBox, TopMovie topMovie)
    {
        Button button = new Button();
        double buttonWidth = 120;
        double buttonHeight = 120;
        button.setPrefSize(buttonWidth,buttonHeight);
        button.setOnAction(event -> System.out.println("Button clicked for movie: " + topMovie.getTitle()));
        button.setUserData(topMovie);
        button.getStyleClass().add("button"); // Add a CSS class to identify buttons later

        // Set a placeholder image
        button.setStyle("-fx-background-image: url('/placeholder.png'); " +
                "-fx-background-size: stretch;");

        Label titleLabel = new Label(topMovie.getTitle());
        titleLabel.setWrapText(false); // Do not allow text to wrap
        titleLabel.setMaxWidth(buttonWidth); // Limit the width to fit the button size
        titleLabel.setMaxHeight(buttonHeight); // Limit the height to fit one line
        titleLabel.getStyleClass().add("movieLabels"); // Add a CSS class to identify labels later
        titleLabel.setEllipsisString("..."); // Set ellipsis for truncated text

        // Create a VBox to hold the button and label
        VBox container = new VBox();
        container.getChildren().addAll(button, titleLabel);
        container.setSpacing(5); // Set spacing between button and label

        contentBox.getChildren().add(container);
    }

    private void initializeScrollPaneTopMovies(ScrollPane scrollPane, ObservableList<TopMovie> topMovies)
    {
        HBox contentBox = new HBox();
        contentBox.setSpacing(10);

        for (TopMovie topMovie : topMovies)
        {
            addButtonTopMovies(contentBox, topMovie);
        }

        scrollPane.setContent(contentBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    private void loadImages(ScrollPane scrollPane) {
        HBox contentBox = (HBox) scrollPane.getContent();
        int maxMovies = Math.min(contentBox.getChildren().size(), 20);

        for (int i = 0; i < maxMovies; i++) {
            Node node = contentBox.getChildren().get(i);
            if (node instanceof VBox) {
                VBox container = (VBox) node;
                Button button = (Button) container.getChildren().get(0);
                Object userData = button.getUserData();
                if (userData instanceof TopMovie) {
                    TopMovie topMovie = (TopMovie) userData;
                    Movie movie = topMovie.getMovie();
                    loadPosterImage(button, movie);
                } else if (userData instanceof Movie) {
                    Movie movie = (Movie) userData;
                    loadPosterImage(button, movie);
                }
            }
        }
    }

    private void loadPosterImage(Button button, Movie movie) {
        try {
            String jsonResponse = httpService.searchMovie(movie.getTitle());
            if (jsonResponse == null) {
                // If no movie results found, try searching for series
                System.out.println("Sorry, no movie found trying to look for series instead");
                jsonResponse = httpService.searchSeries(movie.getTitle());
                System.out.println("THIS SERIES HAS A POSTERPATH" + jsonResponse);
            }
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
