package service;

import java.util.List;
import model.*;
import repository.TripRepository;
import repository.UserRepository;
import util.FareCalculator;

public class TripService {
    //requests a trip based on the passenger, origin, and destination
    public static Trip requestTrip(Passenger passenger, Location start, Location end) {
        //Find nearest available driver
        Driver driver = findNearestAvailableDriver(start);
        if (driver == null) return null;
        //Calculate fare
        double fare = FareCalculator.calculateFare(start, end);
        //Create trip
        String tripId = TripRepository.generateId();
        Trip trip = new Trip(tripId, passenger, driver, start, end, fare, Trip.REQUESTED);
        //Update driver status and location
        driver.setAvailable(false);
        driver.setCurrentLocation(start);
        //Save trip
        TripRepository.addTrip(trip);
        return trip;
    }
    //starts a trip
    public static String startTrip(Passenger passenger) {
        Trip activeTrip = getActiveTrip(passenger);
        if (activeTrip == null) {
            return "No active trip found";
        }
        if (!activeTrip.isRequested()) {
            return "Trip cannot be started in current state";
        }
        activeTrip.startTrip();
        return null;
    }
    //ends the trip
    public static String endTrip(Passenger passenger) {
        Trip activeTrip = getActiveTrip(passenger);
        if (activeTrip == null) return "No active trip found";
        if (!activeTrip.isInProgress()) return "Trip cannot be ended in current state";
        //frees up the driver
        Driver driver = activeTrip.getDriver();
        driver.setCurrentLocation(activeTrip.getEnd());
        driver.setAvailable(true);
        //completes the trip
        activeTrip.completeTrip();
        return null;
    }
    //cancels the trip
    public static String cancelTrip(Passenger passenger) {
        Trip activeTrip = getActiveTrip(passenger);
        if (activeTrip == null) return "No active trip found";
        if (!activeTrip.canBeCancelled()) return "Trip cannot be cancelled in current state";
        //frees the driver
        Driver driver = activeTrip.getDriver();
        driver.setAvailable(true);
        //cancels the trip
        activeTrip.cancelTrip();
        return null;
    }
    //returns a passengers active trip
    public static Trip getActiveTrip(Passenger passenger) {
        //gets the passengers trip list
        List<Trip> trips = TripRepository.getTripsByPassengerId(passenger.getId());
        //searches through the list for active trips
        for (Trip trip : trips) {
            if (trip.isActive()) {
                return trip;
            }
        }
        return null;
    }
    //gets the passengers trip history
    public static List<Trip> getTripHistory(Passenger passenger) {
        return TripRepository.getTripsByPassengerId(passenger.getId());
    }
    //finds the nearest available driver
    private static Driver findNearestAvailableDriver(Location location) {
        //gets the available drivers list
        List<Driver> availableDrivers = UserRepository.findAvailableDrivers();
        if (availableDrivers.isEmpty()) return null;
        
        Driver nearestDriver = null;
        double minDistance = Double.MAX_VALUE;
        //searches for the neaerst driver in the available drivers list
        for (Driver driver : availableDrivers) {
            double distance = driver.calculateDistanceTo(location);
            if (distance < minDistance) {
                minDistance = distance;
                nearestDriver = driver;
            }
        }
        
        return nearestDriver;
    }
    //gets all trips
    public static List<Trip> getAllTrips() {
        return TripRepository.getAllTrips();
    }
    //addes a trip list to the repository
    public static void addTrip(Trip trip) {
        if (trip != null) {
            TripRepository.addTrip(trip);
        }
    }
}