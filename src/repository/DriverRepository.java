package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.Driver;
import model.Location;

public class DriverRepository {
    private static final List<Driver> drivers = new ArrayList<>();
    private static final Random random = new Random();
    private static boolean initialized = false;
    
    private static final String[] DRIVER_NAMES = { "Ali Rezaei", "Mohammad Ahmadi", "Hossein Karimi", "Soheyl Mohammadi", "Fatemeh Alavi", "Reza Ahmadi" };
    private static final String[] VEHICLES = { "Peugeot 206", "Pride", "Samand", "Corolla", "Hellcat", "Peykan" };
    //initialize the drivers if not previously initialized
    public static void initializeDrivers() {
        if (initialized) return;
        for (int i = 0; i < DRIVER_NAMES.length; i++) {
            String id = "DRIV-" + (i+1);
            String name = DRIVER_NAMES[i];
            String vehicle = VEHICLES[i];
            Location location = generateRandomLocation();
            //creates a driver based on the lists
            Driver driver = new Driver(id, name, vehicle, location, true);
            drivers.add(driver);
        }
        initialized = true;
    }
    //generates a random location
    private static Location generateRandomLocation() {
        double x = 1 + random.nextDouble() * 999; // 1-1000 range
        double y = 1 + random.nextDouble() * 999; // 1-1000 range
        return new Location(x, y);
    }
    //gets the list of initial drivers
    public static List<Driver> getInitialDrivers() {
        return new ArrayList<>(drivers);
    }
}