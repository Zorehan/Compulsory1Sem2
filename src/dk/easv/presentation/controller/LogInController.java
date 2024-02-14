package dk.easv.presentation.controller;

import dk.easv.presentation.model.AppModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {
    @FXML private PasswordField passwordField;
    @FXML private TextField userId;

    private AppModel appModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        appModel = AppModel.getInstance();
    }

    public void pressLoginButton(ActionEvent actionEvent) {
        appModel.loadUsers();
        if(appModel.loginUserFromUsername(userId.getText())) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/presentation/view/MainWindow.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                Scene sceneMainWindow = new Scene(root);
                sceneMainWindow.getStylesheets().add(getClass().getResource("/mainWindow.css").toExternalForm());
                sceneMainWindow.getStylesheets().add("https://fonts.googleapis.com/css2?family=Inknut+Antiqua:wght@300;400;500;600;700;800;900&display=swap");
                stage.setScene(sceneMainWindow);
                stage.setTitle("Movie Recommendation System 0.01 Beta");
                stage.show();
                stage.setMaximized(true);
                Stage loginStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                loginStage.close();
                MainWindowController controller = loader.getController();
                controller.setModel(appModel);
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load App.fxml");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong username or password");
            alert.showAndWait();
        }
    }
}
