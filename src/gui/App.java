package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application { //Application class is used for making GUI for apps using JavaFX
    @Override
    public void start(Stage stage) throws IOException { //handles the start of application
        //creates a parent root node and sets a scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("openScene.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 500, 700, Color.rgb(95, 201, 255)); //scene has a width of 500px, a height of 700px, and color code rgb(95, 201, 255)
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm()); //connects the CSS stylesheet to the application
        stage.setResizable(false); //makes the stage not resizable
        //set app title and icon
        stage.setTitle("InsTax! _ Your Instant Taxi App");
        Image icon = new Image(getClass().getResourceAsStream("/gui/img/appIcon.png"));
        stage.getIcons().add(icon);
        //initializes data
        Controller controller = loader.getController();
        controller.initializeSystem();

        //set scene and show the stage to open the app
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}