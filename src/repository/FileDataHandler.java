package repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import model.*;
import service.*;

public class FileDataHandler {
    //name for files addresses
    private static final String PASSENGERS_FILE = "resources/passengers.csv";
    private static final String DRIVERS_FILE = "resources/drivers.csv";
    private static final String TRIPS_FILE = "resources/trips.csv";
    //save all data
    public static void saveAllData() {
        saveUserData();
        saveDriverData();
        saveTripData();
    }
    //load all data
    public static void loadInitialData() {
        loadDrivers();
        loadPassengers();
        loadTrips();
    }
    //save user data
    public static void saveUserData() {
        saveToFile(PASSENGERS_FILE, (writer) -> {
            for (Passenger p : UserService.getAllPassengers()) {
                writer.println(String.join(",",
                    p.getId(),
                    p.getName(),
                    p.getPassword()
                ));
            }
        }, "user");
    }
    //save driver data
    public static void saveDriverData() {
        saveToFile(DRIVERS_FILE, (writer) -> {
            for (Driver d : UserService.getAllDrivers()) {
                writer.println(String.join(",",
                    d.getId(),
                    d.getName(),
                    d.getVehicle(),
                    String.valueOf(d.getCurrentLocation().getX()),
                    String.valueOf(d.getCurrentLocation().getY()),
                    String.valueOf(d.isAvailable())
                ));
            }
        }, "driver");
    }
    //save trip data
    public static void saveTripData() {
        saveToFile(TRIPS_FILE, (writer) -> {
            for (Trip t : TripService.getAllTrips()) {
                writer.println(String.join(",",
                    t.getId(),
                    t.getPassenger().getId(),
                    t.getDriver().getId(),
                    String.valueOf(t.getStart().getX()),
                    String.valueOf(t.getStart().getY()),
                    String.valueOf(t.getEnd().getX()),
                    String.valueOf(t.getEnd().getY()),
                    String.valueOf(t.getFare()),
                    String.valueOf(t.getStatus())
                ));
            }
        }, "trip");
    }
    //updates A driver in the drivers based on id
    public static void saveDriver(Driver driver) {
        List<Driver> drivers = new ArrayList<>(UserService.getAllDrivers());
        //finds and replaces the driver
        for (int i = 0; i < drivers.size(); i++) {
            if (drivers.get(i).getId().equals(driver.getId())) {
                drivers.set(i, driver);
                break;
            }
        }
        //saves all drivers
        saveToFile(DRIVERS_FILE, (writer) -> {
            for (Driver d : drivers) {
                writer.println(String.join(",",
                    d.getId(),
                    d.getName(),
                    d.getVehicle(),
                    String.valueOf(d.getCurrentLocation().getX()),
                    String.valueOf(d.getCurrentLocation().getY()),
                    String.valueOf(d.isAvailable())
                ));
            }
        }, "driver");
    }
    //loads passengers from file
    private static void loadPassengers() {
        loadFromFile(PASSENGERS_FILE, (data) -> {
            if (data.length == 3) {
                Passenger p = new Passenger(data[0]/*ID*/, data[1]/*userename*/, data[2]/*password*/);
                UserService.addPassenger(p);
            }
        }, "passenger");
    }
    //loads drivers from file
    private static void loadDrivers() {
        loadFromFile(DRIVERS_FILE, (data) -> {
            if (data.length == 6) {
                if (UserService.findDriverById(data[0]) == null) {
                    Location loc = new Location(
                        Double.parseDouble(data[3]),
                        Double.parseDouble(data[4])
                    );
                    Driver d = new Driver(
                        data[0]/*ID*/, data[1]/*name*/, data[2]/*vehicle*/, loc, Boolean.parseBoolean(data[5])/*status*/
                    );
                    UserService.addDriver(d);
                }
            }
        }, "driver");
    }
    //loads trips from file
    private static void loadTrips() {
        loadFromFile(TRIPS_FILE, (data) -> {
            if (data.length == 9) {
                Location start = new Location(
                    Double.parseDouble(data[3]),
                    Double.parseDouble(data[4])
                );
                Location end = new Location(
                    Double.parseDouble(data[5]),
                    Double.parseDouble(data[6])
                );
                
                Passenger p = UserService.findPassengerById(data[1]);
                Driver d = UserService.findDriverById(data[2]);
                
                if (p != null && d != null) {
                    int status = Integer.parseInt(data[8]);
                    Trip trip = new Trip(
                        data[0]/*ID*/, p, d, start, end,
                        Double.parseDouble(data[7]),//fare
                        status 
                    );
                    TripService.addTrip(trip);
                }
            }
        }, "trip");
    }
    //interfaces for file handling
    private interface FileWriterHandler {
        void handle(PrintWriter writer);
    }
    private interface DataHandler {
        void handle(String[] data);
    }
    //saves given data to the given file
    private static void saveToFile(String filename, FileWriterHandler handler, String dataType) {
        File file = new File(filename);
        //creates the file if needed
        if (!file.exists()) createFile(file);
        //writes in the file
        try (PrintWriter writer = new PrintWriter(file)) {
            handler.handle(writer);
        } catch (IOException e) {
            System.err.println("Error saving " + dataType + " data: " + e.getMessage());
        }
    }
    //loads data from a given file
    private static void loadFromFile(String filename, DataHandler handler, String dataType) {
        File file = new File(filename);
        if (!file.exists()) return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                handler.handle(data);
            }
        } catch (IOException e) {
            System.err.println("Error loading " + dataType + " data: " + e.getMessage());
        }
    }
    //creates a file thats missing
    private static void createFile(File file) {
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            System.err.println("Error creating file: " + file.getPath());
        }
    }
}