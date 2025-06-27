package model;

import util.DistanceCalculator;

public class Driver {
    private String id;
    private String name;
    private String vehicle;
    private Location currentLocation;
    private boolean available;

    public Driver(String id, String name, String vehicle, Location currentLocation, boolean available) {
        this.id = id;
        this.name = name;
        this.vehicle = vehicle;
        this.currentLocation = currentLocation;
        this.available = available;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getVehicle() { return vehicle; }
    public Location getCurrentLocation() { return currentLocation; }
    public boolean isAvailable() { return available; }

    public void setCurrentLocation(Location location) { 
        this.currentLocation = location;
    }
    
    public void setAvailable(boolean available) { 
        this.available = available; 
    }

    public double calculateDistanceTo(Location location) {
        return DistanceCalculator.calculateDistance(this.currentLocation, location);
    }

    @Override
    public String toString() {
        return String.format("Driver[%s - %s | %s | %s | Available: %s]", 
                id, name, vehicle, currentLocation, available);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Driver driver = (Driver) obj;
        return id != null && id.equals(driver.id);
    }

}