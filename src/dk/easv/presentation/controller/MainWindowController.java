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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import dk.easv.entities.Movie;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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
/*
Der er 3 addButton metoder da de tager allesammen en anden klasse som parameter,
Metoden I sig selv opretter en knap med alt dataen fra klassen knyttet til det, tilføjer et placeholder billede
og laver en Label  ien seperat VBox som er knyttet til Knappens eksisterende HBox
 */
    private void addButton(HBox contentBox, Movie movie) {
        Button button = new Button();
        double buttonWidth = 120;
        double buttonHeight = 120;
        button.setPrefSize(buttonWidth, buttonHeight);
        button.setOnAction(event -> showMovieDetails(movie, button));
        button.setUserData(movie);
        button.getStyleClass().add("button");


        button.setStyle("-fx-background-image: url('/placeholder.png'); " +
                "-fx-background-size: stretch;" +
                "-fx-background-radius: 10px;");

        Label titleLabel = new Label(movie.getTitle() + " (" + movie.getYear() + ")");
        titleLabel.setWrapText(false);
        titleLabel.setMaxWidth(buttonWidth);
        titleLabel.setMaxHeight(buttonHeight);
        titleLabel.getStyleClass().add("movieLabels");
        titleLabel.setEllipsisString("...");

        VBox container = new VBox();
        container.getChildren().addAll(button, titleLabel);
        container.setSpacing(5);

        contentBox.getChildren().add(container);
    }

    private void addButtonUserSimilarity(HBox contentBox, UserSimilarity userSimilarity)
    {
        Button button = new Button();
        double buttonWidth = 120;
        double buttonHeight = 120;
        button.setPrefSize(buttonWidth,buttonHeight);
        button.setOnAction(event -> System.out.println("Button clicked for user: " + userSimilarity.getName()));
        button.setUserData(userSimilarity);
        button.getStyleClass().add("button");


        button.setStyle("-fx-background-image: url('/placeholderprofilepicture.jpg'); " +
                "-fx-background-size: stretch;");

        Label titleLabel = new Label(userSimilarity.getName());
        titleLabel.setWrapText(false);
        titleLabel.setMaxWidth(buttonWidth);
        titleLabel.setMaxHeight(buttonHeight);
        titleLabel.getStyleClass().add("movieLabels");
        titleLabel.setEllipsisString("...");


        VBox container = new VBox();
        container.getChildren().addAll(button, titleLabel);
        container.setSpacing(5);

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
        button.getStyleClass().add("button");

        button.setStyle("-fx-background-image: url('/placeholder.png'); " +
                "-fx-background-size: stretch;");

        Label titleLabel = new Label(topMovie.getTitle());
        titleLabel.setWrapText(false);
        titleLabel.setMaxWidth(buttonWidth);
        titleLabel.setMaxHeight(buttonHeight);
        titleLabel.getStyleClass().add("movieLabels");
        titleLabel.setEllipsisString("...");


        VBox container = new VBox();
        container.getChildren().addAll(button, titleLabel);
        container.setSpacing(5);

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
/*
loadImages loader billederne ind i scrollpanesne ved at køre loadPosterImage metoden på knappen og filmen der er knyttet til den
 */
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
/*
loadPosterimage henter billeder til knapperne ved hjælp af TMDB API'en som man kan læse nærmere om i httpService klassen
Den kigger efter om resultatet af api-requesten returner null, da det de fleste tilfæller betyder at det er en serie og ikke en film
herefter kører den searchSeries metoden, og kører getPosterPath metoden fra HttpService med responsen fra serie-api-endpointet
efter alt dette sætter den knappens billede til at være den hentet fra TMDB.
 */
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

  /*
  showMovieDetails metoden sørger for at hvis man trykker på en knap, så kommer der et nyt vindue op der viser nærmere
  information om filmen (her kunne man så hente mere information fra api'en, vi vælger dog bare at vise titlen, datoen
  og noget falsk latin brødtekst for at fylde resten ud. Ved siden af dette opretten den alle elementerne som
   */
  private void showMovieDetails(Movie movie, Button button) {

      Parent root = button.getScene().getRoot();


      BoxBlur blurEffect = new BoxBlur(10, 10, 3);
      root.setEffect(blurEffect);


      Stage stage = new Stage();
      stage.setTitle(movie.getTitle() + " Details");
      stage.setOnHidden(event -> root.setEffect(null));

      Image backgroundImage = button.getBackground().getImages().get(0).getImage();

      ImageView imageView = new ImageView(backgroundImage);
      imageView.setFitWidth(800);
      imageView.setFitHeight(600);
      imageView.setEffect(new BoxBlur(10, 10, 3));

      Pane backgroundPane = new Pane();
      backgroundPane.getChildren().add(imageView);


      Color textColor = calculateTextColor(backgroundImage);


      Label titleLabel = new Label("Title: " + movie.getTitle());
      titleLabel.setTextFill(textColor);
      titleLabel.setStyle("-fx-font-size: 24px; -fx-font-family: 'Inknut Antiqua'; -fx-font-weight: bold;");
      titleLabel.setAlignment(Pos.TOP_LEFT);
      titleLabel.setPadding(new Insets(20));

      Label yearLabel = new Label("Year: " + movie.getYear());
      yearLabel.setTextFill(textColor);
      yearLabel.setStyle("-fx-font-size: 20px; -fx-font-family: 'Inknut Antiqua';");
      yearLabel.setAlignment(Pos.TOP_LEFT);
      yearLabel.setPadding(new Insets(0, 20, 20, 20));


      Label descriptionLabel = new Label("Description: Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
      descriptionLabel.setWrapText(true);
      descriptionLabel.setTextFill(textColor);
      descriptionLabel.setStyle("-fx-font-size: 15px; -fx-font-family: 'Inknut Antiqua';");
      descriptionLabel.setAlignment(Pos.BOTTOM_RIGHT);
      descriptionLabel.setPadding(new Insets(20));


      VBox layout = new VBox();
      layout.getChildren().addAll(titleLabel, yearLabel, descriptionLabel);
      layout.setAlignment(Pos.TOP_LEFT);

      StackPane stackPane = new StackPane();
      stackPane.getChildren().addAll(backgroundPane, layout);

      Scene scene = new Scene(stackPane, 800, 600);
      stage.setScene(scene);

      Button playButton = new Button("PLAY");
      playButton.setStyle("-fx-background-color: red; -fx-text-fill: black; -fx-font-size: 18px; -fx-pref-width: 100px; -fx-pref-height: 50px; -fx-background-radius: 10; -fx-font-family: 'Inknut Antiqua';");

      playButton.setOnAction(event -> {
          System.out.println("Playing movie: " + movie.getTitle());
      });

      StackPane.setAlignment(playButton, Pos.BOTTOM_RIGHT);
      StackPane.setMargin(playButton, new Insets(0, 20, 20, 0));
      stackPane.getChildren().add(playButton);

      stage.show();
  }

  /*
  Metoden her bruger PixelReader klassen til at få fat i farvekoderne til hver enkelt pixel på posterbillederne
  efterfølgende bliver de kørt igennem et loop hvor ved hjælp af en formel(som vægter hvorvidt menneskeøjet opfanger forskellige farver)
  bestemmer om den pixel er lys eller mørk, til sidst får den det gennemsnitlige lysstyrke på billedet ud, hvis den er lys returner metoden sort
  og hvis den er mørk returner metoden hvid.
   */
    private Color calculateTextColor(Image image) {

        PixelReader pixelReader = image.getPixelReader();

        double totalBrightness = 0;
        int pixelCount = 0;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = pixelReader.getColor(x, y);

                double brightness = (0.2126 * color.getRed()) + (0.7152 * color.getGreen()) + (0.0722 * color.getBlue());
                totalBrightness += brightness;
                pixelCount++;
            }
        }


        double averageBrightness = totalBrightness / pixelCount;

        return (averageBrightness < 0.5) ? Color.WHITE : Color.BLACK;
    }

}
