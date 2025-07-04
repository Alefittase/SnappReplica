package util;

import model.Location;

public class DistanceCalculator {
    //calculates the distance between two locations
    public static double calculateDistance(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) return -1;
        double deltaX = loc2.getX() - loc1.getX();
        double deltaY = loc2.getY() - loc1.getY();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
    
    public static boolean isValidDistance(double distance) {
        return distance >= 0;
    }
}