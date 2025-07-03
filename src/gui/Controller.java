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
    private Text nameLabel; //might be used in session
    @FXML
    public void setNameLabel(String s) {
        nameLabel.setText(s);
    }

    public void initializeSystem(){ //might not need multi threading
        Thread initThread = new Thread(()->{
            FileDataHandler.loadInitialData();
            if (UserRepository.getAllDrivers().isEmpty()) {
                DriverRepository.initializeDrivers();
                for (Driver driver : DriverRepository.getInitialDrivers()) {
                    UserRepository.addDriver(driver);
                }
                FileDataHandler.saveDriverData();
            }
            Platform.runLater(()->{
                //
            });
        });

        initThread.start();
    }

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenuNoTrip.fxml"));
        root = loader.load();
        Controller controller = loader.getController();

        controller.setNameLabel("Hello "+Session.getSession().getCurrentUser().getName());

        stage = (Stage)((Node)Button.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private Text tripPrice;
    @FXML
    private void setTripPrice(String s) {
        tripPrice.setText(s);
    }
    @FXML
    private Text tripCords;
    @FXML
    private void setTripCords(String s) {
        tripCords.setText(s);
    }
    @FXML
    private Text driverName;
    @FXML
    private void setDriverName(String s) {
        driverName.setText(s);
    }
    @FXML
    private Text driverVehicle;
    @FXML
    private void setDriverVehicle(String s) {
        driverVehicle.setText(s);
    }
    @FXML
    public void switchToMMRequested(ActionEvent Button) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenuRequested.fxml"));
        root = loader.load();
        Controller controller = loader.getController();

        controller.setNameLabel("Hello "+Session.getSession().getCurrentUser().getName());
        controller.setTripPrice("("+Session.getSession().getActiveTrip().getFare()+"$)");
        controller.setTripCords("Origin: "+Session.getSession().getActiveTrip().getStart()+", Destination: "+Session.getSession().getActiveTrip().getEnd());
        controller.setDriverName("Driver: "+Session.getSession().getActiveTrip().getDriver().getName());
        controller.setDriverVehicle("Vehicle: "+Session.getSession().getActiveTrip().getDriver().getVehicle());

        stage = (Stage)((Node)Button.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToMMInTrip(ActionEvent Button) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenuInTrip.fxml"));
        root = loader.load();
        Controller controller = loader.getController();

        controller.setNameLabel("Hello "+Session.getSession().getCurrentUser().getName());
        controller.setTripPrice("("+Session.getSession().getActiveTrip().getFare()+"$)");
        controller.setTripCords("Destination: "+Session.getSession().getActiveTrip().getEnd());
        controller.setDriverName("Driver: "+Session.getSession().getActiveTrip().getDriver().getName());
        controller.setDriverVehicle("Vehicle: "+Session.getSession().getActiveTrip().getDriver().getVehicle());

        stage = (Stage)((Node)Button.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private TextArea tripHistoryList;
    @FXML
    public void appendTripHistoryListText(String s) {
        tripHistoryList.appendText(s);
    }
    @FXML
    public void switchToTripHistory(ActionEvent Button) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("tripHistory.fxml"));
        root = loader.load();
        Controller controller = loader.getController();

        for (Trip trip : TripService.getTripHistory(Session.getSession().getCurrentUser())) {
            controller.appendTripHistoryListText("["+trip.getId()+"] "+trip.getStart()+" -> "+trip.getEnd()+" ("+trip.getFare()+"$) - "+trip.getStatusString()+"\n");
        }

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
    private TextField loginUsername;
    @FXML
    private PasswordField loginPassword;
    @FXML
    private Text loginError;
    @FXML
    public void login(ActionEvent Button) throws IOException {
        Session.getSession().setCurrentUser(UserService.login(loginUsername.getText(), loginPassword.getText()));
        if (Session.getSession().getCurrentUser() == null) {
            loginError.setText("Invalid username or password");
        } else {
            Trip lastTrip = TripService.getActiveTrip(Session.getSession().getCurrentUser());
            if(lastTrip == null || !lastTrip.isActive()) switchToMMNoTrip(Button);
            else if(lastTrip.isRequested()) switchToMMRequested(Button);
            else if(lastTrip.isInProgress()) switchToMMInTrip(Button);
            else loginError.setText("Error Logging In");
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
        } else {
            registerError.setText(error);
        }
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
    private Text requestError;
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
            Location startLocation = new Location(sX,sY);
            double eX = Double.parseDouble(endX.getText());
            double eY = Double.parseDouble(endY.getText());
            Location endLocation = new Location(eX,eY);
            TripService.requestTrip(Session.getSession().getCurrentUser(), startLocation, endLocation);

            if (Session.getSession().getActiveTrip() != null) {
                FileDataHandler.saveTripData();
                FileDataHandler.saveDriver(Session.getSession().getActiveTrip().getDriver());
                switchToMMRequested(Button);
            } else {
                requestError.setText("Failed to create trip. No available drivers.");
            }
        }
    }

    @FXML
    public void startTrip(ActionEvent Button) throws IOException {
        TripService.startTrip(Session.getSession().getCurrentUser());
        FileDataHandler.saveTripData();
        switchToMMInTrip(Button);
    }

    public void cancelTrip(ActionEvent Button) throws IOException {
        Driver driver = Session.getSession().getActiveTrip().getDriver();
        TripService.cancelTrip(Session.getSession().getCurrentUser());
        FileDataHandler.saveTripData();
        FileDataHandler.saveDriver(driver);
        switchToMMNoTrip(Button);
    }

    public void endTrip(ActionEvent Button) throws IOException {
        Driver driver = Session.getSession().getActiveTrip().getDriver();
        TripService.endTrip(Session.getSession().getCurrentUser());
        FileDataHandler.saveTripData();
        FileDataHandler.saveDriver(driver);
        switchToMMNoTrip(Button);
    }

    public void backFromTripHistory(ActionEvent Button) throws IOException { //this method has been changed from the Main.java equivalent
        if(Session.getSession().getActiveTrip() == null) {
            switchToMMNoTrip(Button);
        } else {
            if (Session.getSession().getActiveTrip().isRequested()) {
                switchToMMRequested(Button);
            }
            if (Session.getSession().getActiveTrip().isInProgress()) {
                switchToMMInTrip(Button);
            }
        }
    }

    @FXML
    public void logout(ActionEvent Button) throws IOException {
        Session.getSession().setCurrentUser(null);
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
