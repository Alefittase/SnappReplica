package model;

public class Location {
    private final double x;
    private final double y;
    //constructor
    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }
    //getter methods
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Location location = (Location) obj;
        return Double.compare(location.x, x) == 0 && Double.compare(location.y, y) == 0;
    }
    @Override
    public String toString() {
        return String.format("(%.1f, %.1f)", x, y);
    }
}