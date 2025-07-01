package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //sets a scene and a root node for the scene
        Parent root = FXMLLoader.load(getClass().getResource("openScene.fxml"));
        Scene scene = new Scene(root, 500, 700, Color.rgb(95, 201, 255));
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        //set stage details
        stage.setResizable(false);
        //set app title and icon
        stage.setTitle("InsTax _ Your Instant Taxi App");
        Image icon = new Image("file:src/gui/img/appIcon.png");
        stage.getIcons().add(icon);

        //set scene and show the stage to open the app
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}