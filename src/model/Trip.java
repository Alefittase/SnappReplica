package model;

import java.util.Objects;

public class Trip {
    public static final int REQUESTED = 0;
    public static final int IN_PROGRESS = 1;
    public static final int COMPLETED = 2;
    public static final int CANCELLED = 3;
    
    private final String id;
    private final Passenger passenger;
    private final Driver driver;
    private final Location start;
    private final Location end;
    private final double fare;
    private int status;

    public Trip(String id, Passenger passenger, Driver driver, Location start, Location end, double fare, int status) {
        this.id = id;
        this.passenger = passenger;
        this.driver = driver;
        this.start = start;
        this.end = end;
        this.fare = fare;
        this.status = validateStatus(status);
    }

    public String getId() { return id; }
    public Passenger getPassenger() { return passenger; }
    public Driver getDriver() { return driver; }
    public Location getStart() { return start; }
    public Location getEnd() { return end; }
    public double getFare() { return fare; }
    public int getStatus() { return status; }

    private int validateStatus(int status) {
        if (status >= REQUESTED && status <= CANCELLED) {
            return status;
        }
        return REQUESTED;
    }

    public void startTrip() {
        if (status == REQUESTED) {
            status = IN_PROGRESS;
        }
    }

    public void completeTrip() {
        if (status == IN_PROGRESS) {
            status = COMPLETED;
        }
    }

    public void cancelTrip() {
        if (status == REQUESTED || status == IN_PROGRESS) {
            status = CANCELLED;
        }
    }

    public boolean isActive() {
        return status == REQUESTED || status == IN_PROGRESS;
    }
    
    public boolean canBeCancelled() {
        return isActive();
    }
    
    public boolean isCompleted() {
        return status == COMPLETED;
    }
    
    public boolean isCancelled() {
        return status == CANCELLED;
    }
    
    public boolean isRequested() {
        return status == REQUESTED;
    }
    
    public boolean isInProgress() {
        return status == IN_PROGRESS;
    }

    public String getStatusString() {
        switch (status) {
            case REQUESTED:
                return "REQUESTED";
            case IN_PROGRESS:
                return "IN_PROGRESS";
            case COMPLETED:
                return "COMPLETED";
            case CANCELLED:
                return "CANCELLED";
            default:
                return "UNKNOWN";
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Trip trip = (Trip) obj;
        return Objects.equals(id, trip.id);
    }

    @Override
    public String toString() {
        return String.format("Trip[%s | %s --> %s | Fare: %.2f | Status: %s]", 
                id,
                start, end,
                fare,
                getStatusString());
    }
}