package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.*;
import service.*;
import repository.*;

import java.io.IOException;

public class Controller {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

    @FXML
    private Text nameLabel;

    @FXML
    public void switchToLoginScene(ActionEvent Button) throws IOException {
        root = FXMLLoader.load(getClass().getResource("loginScene.fxml"));
        stage = (Stage)((Node)Button.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToRegisterScene(ActionEvent Button) throws IOException {
        root = FXMLLoader.load(getClass().getResource("registerScene.fxml"));
        stage = (Stage)((Node)Button.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToOpenScene(ActionEvent Button) throws IOException {
        root = FXMLLoader.load(getClass().getResource("openScene.fxml"));
        stage = (Stage)((Node)Button.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToMMNoTrip(ActionEvent Button) throws IOException {
        root = FXMLLoader.load(getClass().getResource("mainMenuNoTrip.fxml"));
        stage = (Stage)((Node)Button.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToMMRequested(ActionEvent Button) throws IOException {
        root = FXMLLoader.load(getClass().getResource("mainMenuRequested.fxml"));
        stage = (Stage)((Node)Button.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToMMInTrip(ActionEvent Button) throws IOException {
        root = FXMLLoader.load(getClass().getResource("mainMenuInTrip.fxml"));
        stage = (Stage)((Node)Button.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToTripHistory(ActionEvent Button) throws IOException {
        root = FXMLLoader.load(getClass().getResource("tripHistory.fxml"));
        stage = (Stage)((Node)Button.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToRequestTripScene(ActionEvent Button) throws IOException {
        root = FXMLLoader.load(getClass().getResource("requestTripScene.fxml"));
        stage = (Stage)((Node)Button.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Passenger currentUser;
    @FXML
    private TextField loginUsername;
    @FXML
    private PasswordField loginPassword;
    @FXML
    private Text loginError;
    @FXML
    public void login(ActionEvent Button) throws IOException {
        currentUser = UserService.login(loginUsername.getText(), loginPassword.getText());
        if (currentUser == null) {
            loginError.setText("Invalid username or password");
        }
        else {
            switchToMMNoTrip(Button);
        }
    }

    @FXML
    private TextField registerUsername;
    @FXML
    private PasswordField registerPassword;
    @FXML
    private Text registerError;
    @FXML
    public void register(ActionEvent Button) throws IOException {
        String error = UserService.registerPassenger(registerUsername.getText(), registerPassword.getText());
        if(error==null){
            switchToOpenScene(Button);
        }
        registerError.setText(error);
    }

    @FXML
    private TextField startX;
    @FXML
    private TextField startY;
    @FXML
    private TextField endX;
    @FXML
    private TextField endY;
    @FXML
    private Location startLocation;
    @FXML
    private Location endLocation;
    @FXML
    private Text requestError;
    @FXML
    private Trip trip;
    @FXML
    private Text tripPrice;
    @FXML
    private Text tripId;
    @FXML
    public void requestTrip(ActionEvent Button) throws IOException {
        if (!(startX.getText().matches("-?\\d+(\\.\\d+)?")) ||
                !(startY.getText().matches("-?\\d+(\\.\\d+)?")) ||
                !(endX.getText().matches("-?\\d+(\\.\\d+)?")) ||
                !(endY.getText().matches("-?\\d+(\\.\\d+)?")))
            requestError.setText("Coordinates must be valid numbers.");
        else{
            double sX = Double.parseDouble(startX.getText());
            double sY = Double.parseDouble(startY.getText());
            startLocation = new Location(sX,sY);
            double eX = Double.parseDouble(endX.getText());
            double eY = Double.parseDouble(endY.getText());
            endLocation = new Location(eX,eY);

            trip = TripService.requestTrip(currentUser, startLocation, endLocation);
            if (trip != null) {
                switchToMMRequested(Button);
                FileDataHandler.saveTripData();
                FileDataHandler.saveDriver(trip.getDriver());
                tripPrice.setText("("+trip.getFare()+"$)");
                tripId.setText("ID: "+trip.getId());
            } else {
                requestError.setText("Failed to create trip. No available drivers.");
            }
        }
    }


    @FXML
    private Text driverName;
    @FXML
    private Text driverVehicle;
    @FXML
    public void startTrip(ActionEvent Button) throws IOException {
        TripService.startTrip(currentUser);
        FileDataHandler.saveTripData();
        driverName.setText("Driver: "+trip.getDriver().getName());
        driverVehicle.setText("Vehicle: "+trip.getDriver().getVehicle());
        switchToMMInTrip(Button);
    }

    public void cancelTrip(ActionEvent Button) throws IOException {
        Trip activeTrip = TripService.getActiveTrip(currentUser);
        Driver driver = activeTrip.getDriver();
        TripService.cancelTrip(currentUser);
        FileDataHandler.saveTripData();
        FileDataHandler.saveDriver(driver);
        switchToMMNoTrip(Button);
    }

    public void endTrip(ActionEvent Button) throws IOException {
        Trip activeTrip = TripService.getActiveTrip(currentUser);
        Driver driver = activeTrip.getDriver();
        TripService.endTrip(currentUser);
        FileDataHandler.saveTripData();
        FileDataHandler.saveDriver(driver);
        switchToMMNoTrip(Button);
    }

    @FXML
    private TextArea tripHistoryList;
    @FXML
    public void tripHistory(ActionEvent Button) throws IOException {
        switchToTripHistory(Button);
        for (Trip trip : TripService.getTripHistory(currentUser)) {
            tripHistoryList.appendText("["+trip.getId()+"] "+trip.getStart()+" -> "+trip.getEnd()+" ("+trip.getFare()+"$) - "+trip.getStatusString()+"\n");
        }
    }

    public void backFromTripHistory(ActionEvent Button) throws IOException {
        Trip activeTrip = TripService.getActiveTrip(currentUser);
        if(activeTrip.getStatusString().equals("REQUESTED")){
            switchToMMRequested(Button);
        } else if(activeTrip.getStatusString().equals("IN_PROGRESS")){
            switchToMMInTrip(Button);
        } else {
            switchToMMNoTrip(Button);
        }
    }

    @FXML
    public void logout(ActionEvent Button) throws IOException {
        currentUser = null;
        switchToOpenScene(Button);
    }

    @FXML
    public void exit(ActionEvent Button) throws IOException {
        FileDataHandler.saveAllData();
        Stage stage = (Stage) ((Node) Button.getSource()).getScene().getWindow();
        stage.close();
        Platform.exit();
        System.exit(0);
    }

}
