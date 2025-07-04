package util;

import model.Location;

public class FareCalculator {
    //base fare and per unit rate
    private static double baseFare = 5.0;
    private static double perUnitRate = 0.5;
    //main functions that calculates the fare between two locations
    public static double calculateFare(Location start, Location end) {
        if (start == null || end == null) return baseFare;
        double distance = DistanceCalculator.calculateDistance(start, end);
        return calculateFare(distance);
    }
    //calculates fare based on distance
    public static double calculateFare(double distance) {
        if (distance < 0) distance = 0;
        double fare = baseFare + (distance * perUnitRate);
        return roundToTwoDecimals(fare);
    }
    //rounds a number to two decimals
    private static double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
    //setter methods
    public static void setBaseFare(double newBaseFare) {
        if (newBaseFare > 0) baseFare = newBaseFare;
    }
    public static void setPerUnitRate(double newRate) {
        if (newRate > 0) perUnitRate = newRate;
    }
    //getter methods
    public static double getBaseFare() {
        return baseFare;
    }
    public static double getPerUnitRate() {
        return perUnitRate;
    }
}