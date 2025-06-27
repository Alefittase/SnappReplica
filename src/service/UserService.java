package service;

import model.Passenger;
import model.Driver;
import repository.UserRepository;
import java.util.List;

public class UserService {
    public static String registerPassenger(String username, String password) {
        if (username == null || username.isEmpty()) {
            return "Username cannot be empty";
        }
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty";
        }
        if (UserRepository.findPassengerByUsername(username) != null) {
            return "Username already exists";
        }
        
        String id = UserRepository.generatePassengerId();
        Passenger passenger = new Passenger(id, username, password);
        UserRepository.addPassenger(passenger);
        return null;
    }
    
    public static Passenger login(String username, String password) {
        Passenger passenger = UserRepository.findPassengerByUsername(username);
        if (passenger == null) return null;
        if (!passenger.authenticate(password)) return null;
        return passenger;
    }
    
    public static void addPassenger(Passenger passenger) {
        if (passenger != null && passenger.isValid()) {
            UserRepository.addPassenger(passenger);
        }
    }
    
    public static List<Passenger> getAllPassengers() {
        return UserRepository.getAllPassengers();
    }
    
    public static List<Driver> getAllDrivers() {
        return UserRepository.getAllDrivers();
    }
    
    public static void addDriver(Driver driver) {
        if (driver != null) {
            UserRepository.addDriver(driver);
        }
    }
    
    public static Passenger findPassengerById(String id) {
        return UserRepository.findPassengerById(id);
    }
    
    public static Driver findDriverById(String id) {
        return UserRepository.findDriverById(id);
    }
}