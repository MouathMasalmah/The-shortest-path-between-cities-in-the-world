package com.example.shortestPath;

public class Edge {
    private Vertix source;
    private Vertix dest;
    private double distance;
    private double cost;
    private double time;

    // Constructor to create an Edge
    public Edge(Vertix source, Vertix dest, double cost, double time) {
        this.source = source;
        this.dest = dest;
        this.cost = cost;
        this.time = time;
        // Calculate the distance between source and destination
        this.distance = haversine(source.getCity().getLatitude(), source.getCity().getLongitude(),
                dest.getCity().getLatitude(), dest.getCity().getLongitude());
    }

    // Getter for the source city
    public Vertix getSource() {
        return source;
    }

    // Getter for the destination city
    public Vertix getDest() {
        return dest;
    }

    // Getter for the distance
    public double getDistance() {
        return distance;
    }

    // Getter for the cost
    public double getCost() {
        return cost;
    }

    // Getter for the time
    public double getTime() {
        return time;
    }

    // Haversine formula to calculate the great-circle distance between two points
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Radius of Earth in kilometers
        double dLat = Math.toRadians(lat2 - lat1); // Difference in latitude
        double dLon = Math.toRadians(lon2 - lon1); // Difference in longitude
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)); // Distance in kilometers
    }

    @Override
    public String toString() {
        return "Edge from " + source.getCity().getName() + " to " + dest.getCity().getName() +
                " [Distance: " + distance + " km, Cost: " + cost + ", Time: " + time + " hours]\n";
    }
}
