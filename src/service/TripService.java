package service;

import model.*;
import repository.TripRepository;
import repository.UserRepository;
import util.DistanceCalculator;
import util.FareCalculator;
import java.util.List;

public class TripService {

    public static Trip requestTrip(Passenger passenger, Location start, Location end) {
        // 1. Find nearest available driver
        Driver driver = findNearestAvailableDriver(start);
        if (driver == null) return null;
        
        // 2. Calculate fare
        double fare = FareCalculator.calculateFare(DistanceCalculator.calculateDistance(start, end));
        
        // 3. Create trip
        String tripId = TripRepository.generateId();
        Trip trip = new Trip(tripId, passenger, driver, start, end, fare, Trip.REQUESTED);
        
        // 4. Update driver status and location
        driver.setAvailable(false);
        driver.setCurrentLocation(start);
        
        // 5. Save trip
        TripRepository.addTrip(trip);
        return trip;
    }

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
    
    public static String endTrip(Passenger passenger) {
        Trip activeTrip = getActiveTrip(passenger);
        if (activeTrip == null) return "No active trip found";
        if (!activeTrip.isInProgress()) return "Trip cannot be ended in current state";
        
        Driver driver = activeTrip.getDriver();
        driver.setCurrentLocation(activeTrip.getEnd());
        driver.setAvailable(true);
        
        activeTrip.completeTrip();
        return null;
    }
    
    public static String cancelTrip(Passenger passenger) {
        Trip activeTrip = getActiveTrip(passenger);
        if (activeTrip == null) return "No active trip found";
        if (!activeTrip.canBeCancelled()) return "Trip cannot be cancelled in current state";
        
        Driver driver = activeTrip.getDriver();
        driver.setAvailable(true);
        activeTrip.cancelTrip();
        return null;
    }
    
    
    public static Trip getActiveTrip(Passenger passenger) {
        List<Trip> trips = TripRepository.getTripsByPassengerId(passenger.getId());
        for (Trip trip : trips) {
            if (trip.isActive()) {
                return trip;
            }
        }
        return null;
    }
    
    public static List<Trip> getTripHistory(Passenger passenger) {
        return TripRepository.getTripsByPassengerId(passenger.getId());
    }
    
    private static Driver findNearestAvailableDriver(Location location) {
        List<Driver> availableDrivers = UserRepository.findAvailableDrivers();
        if (availableDrivers.isEmpty()) return null;
        
        Driver nearestDriver = null;
        double minDistance = Double.MAX_VALUE;
        
        for (Driver driver : availableDrivers) {
            double distance = driver.calculateDistanceTo(location);
            if (distance < minDistance) {
                minDistance = distance;
                nearestDriver = driver;
            }
        }
        
        return nearestDriver;
    }
    
    public static List<Trip> getAllTrips() {
        return TripRepository.getAllTrips();
    }
    
    public static void addTrip(Trip trip) {
        if (trip != null) {
            TripRepository.addTrip(trip);
        }
    }
}