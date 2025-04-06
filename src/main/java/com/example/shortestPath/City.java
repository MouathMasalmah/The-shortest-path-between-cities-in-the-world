package com.example.shortestPath;

public class City implements Comparable<City> {
    private String name;
    private double latitude;
    private double longitude;
    private double x;
    private double y;
    public City(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        convertToPixels();
    }
    // Method to convert latitude and longitude to x and y pixel coordinates
    private void convertToPixels() {
        double maxWidth = 1200;  // Width of the map (or screen width)
        double maxHeight = 800;  // Height of the map (or screen height)

        // Formula to convert latitude and longitude to pixel coordinates
        this.x = (((longitude + 180.0) / 360.0) * maxWidth);
        this.y = (((90.0 - latitude) / 180.0) * maxHeight);
    }

    // Getter and setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }

    @Override
    public int compareTo(City other) {
        return this.name.compareTo(other.name); // Compare cities alphabetically by name
    }
}
