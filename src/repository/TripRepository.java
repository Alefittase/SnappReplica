package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import model.Trip;

public class TripRepository {
    //list of trips
    private static final List<Trip> trips = new ArrayList<>();
    
    public static void addTrip(Trip trip) {
        if (trip != null && trip.getId() != null) {
            trips.add(trip);
        }
    }
    //find methods
    public static Trip findTripById(String id) {
        return trips.stream()
            .filter(t -> t.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    public static List<Trip> getTripsByPassengerId(String passengerId) {
        return trips.stream()
            .filter(t -> t.getPassenger().getId().equals(passengerId))
            .collect(Collectors.toList());
    }
    public static List<Trip> getActiveTrips() {
        return trips.stream()
            .filter(Trip::isActive)
            .collect(Collectors.toList());
    }
    public static List<Trip> getAllTrips() {
        return new ArrayList<>(trips);
    }
    //updates trip status by id
    public static void updateTripStatus(String tripId, int status) {
        Trip trip = findTripById(tripId);
        if (trip != null) {
            switch (status) {
                case Trip.REQUESTED:
                    // Cannot revert to requested
                    break;
                case Trip.IN_PROGRESS:
                    trip.startTrip();
                    break;
                case Trip.COMPLETED:
                    trip.completeTrip();
                    break;
                case Trip.CANCELLED:
                    trip.cancelTrip();
                    break;
            }
        }
    }
    //generates id
    public static String generateId() {
        return "TRIP-" + UUID.randomUUID().toString().substring(0, 8);
    }
}