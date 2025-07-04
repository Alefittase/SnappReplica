package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import model.Driver;
import model.Location;
import model.Passenger;

public class UserRepository {
    //list of passengers and drivers
    private static final List<Passenger> passengers = new ArrayList<>();
    private static final List<Driver> drivers = new ArrayList<>();
    //passenger methods
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
    public static String generatePassengerId() {
        return "PASS-" + UUID.randomUUID().toString().substring(0, 8);
    }
    //driver methods
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
    public static String generateDriverId() {
        return "DRIV-" + UUID.randomUUID().toString().substring(0, 8);
    }
    //driver update methods
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
    
    
}