package repository;

import model.Driver;
import model.Passenger;
import model.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserRepository {
    private static final List<Passenger> passengers = new ArrayList<>();
    private static final List<Driver> drivers = new ArrayList<>();
    
    public static void addPassenger(Passenger passenger) {
        if (passenger != null && passenger.getId() != null) {
            passengers.add(passenger);
        }
    }
    
    public static Passenger findPassengerById(String id) {
        return passengers.stream()
            .filter(p -> p.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    public static Passenger findPassengerByUsername(String username) {
        return passengers.stream()
            .filter(p -> p.getName().equals(username))
            .findFirst()
            .orElse(null);
    }
    
    public static List<Passenger> getAllPassengers() {
        return new ArrayList<>(passengers);
    }
    
    public static void addDriver(Driver driver) {
        if (driver != null && driver.getId() != null) {
            drivers.add(driver);
        }
    }
    
    public static Driver findDriverById(String id) {
        return drivers.stream()
            .filter(d -> d.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    public static List<Driver> findAvailableDrivers() {
        return drivers.stream()
            .filter(Driver::isAvailable)
            .collect(Collectors.toList());
    }
    
    public static List<Driver> getAllDrivers() {
        return new ArrayList<>(drivers);
    }
    
    public static void updateDriverAvailability(String driverId, boolean available) {
        Driver driver = findDriverById(driverId);
        if (driver != null) {
            driver.setAvailable(available);
        }
    }
    
    public static void updateDriverLocation(String driverId, Location location) {
        Driver driver = findDriverById(driverId);
        if (driver != null && location != null) {
            driver.setCurrentLocation(location);
        }
    }
    
    public static String generatePassengerId() {
        return "PASS-" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    public static String generateDriverId() {
        return "DRIV-" + UUID.randomUUID().toString().substring(0, 8);
    }
}