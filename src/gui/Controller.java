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

public class Controller { //handles the actions done in the application
    //creating the variables
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;
    @FXML
    private Text nameLabel;
    @FXML
    private Text tripStatusAndPrice;
    @FXML
    private Text tripCords;
    @FXML
    private Text driverName;
    @FXML
    private Text driverVehicle;
    @FXML
    private TextArea tripHistoryList;
    @FXML
    private TextField loginUsername;
    @FXML
    private PasswordField loginPassword;
    @FXML
    private Text loginError;
    @FXML
    private TextField registerUsername;
    @FXML
    private PasswordField registerPassword;
    @FXML
    private Text registerError;
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
    private AnchorPane rootNode;
    //setter methods
    @FXML
    public void setNameLabel(String s) {
        nameLabel.setText(s);
    }
    @FXML
    private void setTripStatusAndPrice(String s) {
        tripStatusAndPrice.setText(s);
    }
    @FXML
    private void setTripCords(String s) {
        tripCords.setText(s);
    }
    @FXML
    private void setDriverName(String s) {
        driverName.setText(s);
    }
    @FXML
    private void setDriverVehicle(String s) {
        driverVehicle.setText(s);
    }
    @FXML
    public void appendTripHistoryListText(String s) {
        tripHistoryList.appendText(s);
    }
    //initializing the data and program
    public void initializeSystem(){
        //using a new thread, loads initial data
        Thread initThread = new Thread(()->{
            FileDataHandler.loadInitialData();
            if (UserRepository.getAllDrivers().isEmpty()) {
                DriverRepository.initializeDrivers();
                for (Driver driver : DriverRepository.getInitialDrivers()) {
                    UserRepository.addDriver(driver);
                }
                FileDataHandler.saveDriverData();
            }
        });
        initThread.start();
        //handles the window close (x) button to save before closing
        Platform.runLater(() -> {
            Stage stage = (Stage) rootNode.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                FileDataHandler.saveAllData();
            });
        });
    }
    //switches the scene to a new fxml file and handles loading the data into the scene
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
    public void switchToMMRequested(ActionEvent Button) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenuRequested.fxml"));
        root = loader.load();
        Controller controller = loader.getController();
        controller.setNameLabel("Hello "+Session.getSession().getCurrentUser().getName());
        controller.setTripStatusAndPrice("Trip Status: Requested  ("+Session.getSession().getActiveTrip().getFare()+"$)");
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
        controller.setTripStatusAndPrice("Trip Status: In Progress  ("+Session.getSession().getActiveTrip().getFare()+"$)");
        controller.setTripCords("Destination: "+Session.getSession().getActiveTrip().getEnd());
        controller.setDriverName("Driver: "+Session.getSession().getActiveTrip().getDriver().getName());
        controller.setDriverVehicle("Vehicle: "+Session.getSession().getActiveTrip().getDriver().getVehicle());
        stage = (Stage)((Node)Button.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
    //handles authentication
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
    public void register(ActionEvent Button) throws IOException {
        String error = UserService.registerPassenger(registerUsername.getText(), registerPassword.getText());
        if(error==null){
            switchToOpenScene(Button);
        } else {
            registerError.setText(error);
        }
    }
    //handles trip service
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
    @FXML
    public void cancelTrip(ActionEvent Button) throws IOException {
        Driver driver = Session.getSession().getActiveTrip().getDriver();
        TripService.cancelTrip(Session.getSession().getCurrentUser());
        FileDataHandler.saveTripData();
        FileDataHandler.saveDriver(driver);
        switchToMMNoTrip(Button);
    }
    @FXML
    public void endTrip(ActionEvent Button) throws IOException {
        Driver driver = Session.getSession().getActiveTrip().getDriver();
        TripService.endTrip(Session.getSession().getCurrentUser());
        FileDataHandler.saveTripData();
        FileDataHandler.saveDriver(driver);
        switchToMMNoTrip(Button);
    }
    //chooses the correct scene when going back from trip history
    @FXML
    public void backFromTripHistory(ActionEvent Button) throws IOException {
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
    //handles logging out and exiting
    @FXML
    public void logout(ActionEvent Button) throws IOException {
        Session.getSession().setCurrentUser(null);
        switchToOpenScene(Button);
    }
    @FXML
    public void exit(ActionEvent Button) throws IOException {
        Stage stage = (Stage) ((Node) Button.getSource()).getScene().getWindow();
        stage.close();
        Platform.exit();
        System.exit(0);
    }
}