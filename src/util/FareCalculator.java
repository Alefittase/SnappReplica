package util;

import model.Location;

public class FareCalculator {
    private static double baseFare = 5.0;
    private static double perUnitRate = 0.5;
    
    public static double calculateFare(Location start, Location end) {
        if (start == null || end == null) return baseFare;
        double distance = DistanceCalculator.calculateDistance(start, end);
        return calculateFare(distance);
    }
    
    public static double calculateFare(double distance) {
        if (distance < 0) distance = 0;
        double fare = baseFare + (distance * perUnitRate);
        return roundToTwoDecimals(fare);
    }
    
    private static double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public static void setBaseFare(double newBaseFare) {
        if (newBaseFare > 0) baseFare = newBaseFare;
    }
    
    public static void setPerUnitRate(double newRate) {
        if (newRate > 0) perUnitRate = newRate;
    }
    
    public static double getBaseFare() {
        return baseFare;
    }
    
    public static double getPerUnitRate() {
        return perUnitRate;
    }
}