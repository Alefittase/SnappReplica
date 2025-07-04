package service;

import java.util.List;
import model.Driver;
import model.Passenger;
import repository.UserRepository;

public class UserService {
    //registers a passenger by username and password
    public static String registerPassenger(String username, String password) {
        //checks for the validity of the credentials
        if (username == null || username.isEmpty()) {
            return "Username cannot be empty";
        }
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty";
        }
        if (UserRepository.findPassengerByUsername(username) != null) {
            return "Username already exists";
        }
        //adds the user to repository
        String id = UserRepository.generatePassengerId();
        Passenger passenger = new Passenger(id, username, password);
        UserRepository.addPassenger(passenger);
        return null;
    }
    //logs a passenger in
    public static Passenger login(String username, String password) {
        Passenger passenger = UserRepository.findPassengerByUsername(username);
        if (passenger == null) return null;
        if (!passenger.authenticate(password)) return null;
        return passenger;
    }
    //adds a passenger
    public static void addPassenger(Passenger passenger) {
        if (passenger != null && passenger.isValid()) {
            UserRepository.addPassenger(passenger);
        }
    }
    //adds a driver
    public static void addDriver(Driver driver) {
        if (driver != null) {
            UserRepository.addDriver(driver);
        }
    }
    //gets the list of all passengers
    public static List<Passenger> getAllPassengers() {
        return UserRepository.getAllPassengers();
    }
    //gets the list of all drivers
    public static List<Driver> getAllDrivers() {
        return UserRepository.getAllDrivers();
    }
    //finds a passenger by ID
    public static Passenger findPassengerById(String id) {
        return UserRepository.findPassengerById(id);
    }
    //finds a driver by ID
    public static Driver findDriverById(String id) {
        return UserRepository.findDriverById(id);
    }
}