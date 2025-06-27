package repository;

import model.Driver;
import model.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DriverRepository {
    private static final List<Driver> drivers = new ArrayList<>();
    private static final Random random = new Random();
    private static boolean initialized = false;
    
    private static final String[] DRIVER_NAMES = { "Ali Rezaei", "Mohammad Ahmadi", "Hossein Karimi", "Soheyl Mohammadi", "Fatemeh Alavi", "Reza Ahmadi" };
    private static final String[] VEHICLES = { "Peugeot 206", "Pride", "Samand", "Corolla", "Hellcat", "Peykan" };

    public static void initializeDrivers() {
        if (initialized) return;
        
        for (int i = 0; i < 6; i++) {
            String id = "DRIV-" + (i + 1);
            String name = DRIVER_NAMES[i];
            String vehicle = VEHICLES[i];
            Location location = generateRandomLocation();
            
            Driver driver = new Driver(id, name, vehicle, location, true);
            drivers.add(driver);
        }
        initialized = true;
    }
    
    private static Location generateRandomLocation() {
        double x = 1 + random.nextDouble() * 99; // 1-100 range
        double y = 1 + random.nextDouble() * 99; // 1-100 range
        return new Location(x, y);
    }
    
    public static List<Driver> getInitialDrivers() {
        return new ArrayList<>(drivers);
    }
}