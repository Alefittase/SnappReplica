package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToLoginScene(ActionEvent loginButton1) throws IOException {
        root = FXMLLoader.load(getClass().getResource("loginScene.fxml"));
        stage = (Stage)((Node)loginButton1.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToRegisterScene(ActionEvent registerButton1) throws IOException {
        root = FXMLLoader.load(getClass().getResource("registerScene.fxml"));
        stage = (Stage)((Node)registerButton1.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToOpenScene(ActionEvent backButton) throws IOException {
        root = FXMLLoader.load(getClass().getResource("openScene.fxml"));
        stage = (Stage)((Node)backButton.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
