package repository;

import model.*;
import service.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileDataHandler {
    private static final String PASSENGERS_FILE = "resources/passengers.csv";
    private static final String DRIVERS_FILE = "resources/drivers.csv";
    private static final String TRIPS_FILE = "resources/trips.csv";
    
    public static void saveAllData() {
        saveUserData();
        saveDriverData();
        saveTripData();
    }

    public static void loadInitialData() {
        loadDrivers();
        loadPassengers();
        loadTrips();
    }

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

    public static void saveDriver(Driver driver) {
        List<Driver> drivers = new ArrayList<>(UserService.getAllDrivers());
        
        for (int i = 0; i < drivers.size(); i++) {
            if (drivers.get(i).getId().equals(driver.getId())) {
                drivers.set(i, driver);
                break;
            }
        }
        
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

    private static void loadPassengers() {
        loadFromFile(PASSENGERS_FILE, (data) -> {
            if (data.length == 3) {
                Passenger p = new Passenger(data[0], data[1], data[2]);
                UserService.addPassenger(p);
            }
        }, "passenger");
    }

    private static void loadDrivers() {
        loadFromFile(DRIVERS_FILE, (data) -> {
            if (data.length == 6) {
                if (UserService.findDriverById(data[0]) == null) {
                    Location loc = new Location(
                        Double.parseDouble(data[3]),
                        Double.parseDouble(data[4])
                    );
                    Driver d = new Driver(
                        data[0], data[1], data[2], loc, Boolean.parseBoolean(data[5])
                    );
                    UserService.addDriver(d);
                }
            }
        }, "driver");
    }

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
                        data[0], p, d, start, end,
                        Double.parseDouble(data[7]),
                        status 
                    );
                    TripService.addTrip(trip);
                }
            }
        }, "trip");
    }

    private interface FileWriterHandler {
        void handle(PrintWriter writer);
    }

    private interface DataHandler {
        void handle(String[] data);
    }

    private static void saveToFile(String filename, FileWriterHandler handler, String dataType) {
        File file = new File(filename);
        if (!file.exists()) createFile(file);
        
        try (PrintWriter writer = new PrintWriter(file)) {
            handler.handle(writer);
        } catch (IOException e) {
            System.err.println("Error saving " + dataType + " data: " + e.getMessage());
        }
    }

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

    private static void createFile(File file) {
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            System.err.println("Error creating file: " + file.getPath());
        }
    }
}