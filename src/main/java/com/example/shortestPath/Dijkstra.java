package com.example.shortestPath;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Dijkstra {
    private Vertix[] cities;  // Array to store Vertix objects for cities
    private int numVertices, numEdges;
    private String ShortPath = "";
    private double totalDistance;
    private double totalTime;
    private double totalCost;
    private LinkedList shortestPathEdges = new LinkedList();

    // Constructor to initialize cities array
    public Dijkstra() {

    }

    public void read(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);

        if (!scanner.hasNextLine()) {
            throw new IllegalArgumentException("File is empty or missing header line.");
        }

        String headerLine = scanner.nextLine().trim();
        String[] headerParts = headerLine.split(",");
        if (headerParts.length != 2) {
            throw new IllegalArgumentException("Invalid header format. Expected 'numVertices numEdges'.");
        }

        numVertices = Integer.parseInt(headerParts[0]);
        numEdges = Integer.parseInt(headerParts[1]);

        cities = new Vertix[numVertices]; // Initialize array based on the number of vertices

        // Read cities (vertices) and add them to the array
        for (int i = 0; i < numVertices; i++) {
            if (!scanner.hasNextLine()) {
                throw new IllegalArgumentException("Not enough city lines. Expected " + numVertices + " cities.");
            }
            String cityLine = scanner.nextLine().trim();

            // Check if the line is valid
            if (cityLine.isEmpty()) {
                continue; // Skip empty lines
            }

            String[] cityParts = cityLine.trim().split(",");
            if (cityParts.length != 3) {
                continue; // Skip invalid lines
            }

            String cityName = cityParts[0].trim();
            double latitude = Double.parseDouble(cityParts[1].trim());
            double longitude = Double.parseDouble(cityParts[2].trim());

            Vertix city = new Vertix(new City(cityName, latitude, longitude));  // Create Vertix instead of City
            cities[i] = city;  // Store Vertix in the cities array

        }

        // Read edges and add them between Vertices in the array
        for (int i = 0; i < numEdges; i++) {
            if (!scanner.hasNextLine()) {
                throw new IllegalArgumentException("Not enough edge lines. Expected " + numEdges + " edges.");
            }
            String edgeLine = scanner.nextLine().trim();

            // Check if the line is valid
            if (edgeLine.isEmpty()) {
                continue; // Skip empty lines
            }

            String[] edgeParts = edgeLine.split(",");
            if (edgeParts.length != 4) {
                continue; // Skip invalid lines
            }

            String fromCity = edgeParts[0].trim();
            String toCity = edgeParts[1].trim();
            double cost = Double.parseDouble(edgeParts[2].replace("$", ""));
            double time = Double.parseDouble(edgeParts[3].split(" ")[0]);

            // Find the cities in the array
            Vertix from = findCity(fromCity);
            Vertix to = findCity(toCity);

            if (from == null || to == null) {
                continue;  // Skip adding this edge if cities are not found
            }

            from.addEdge(to, cost, time);  // Add the edge to the Vertix object
        }

        scanner.close();
        System.out.println("File reading complete.");
    }

    // Method to find a Vertix object by city name
    private Vertix findCity(String name) {
        for (int i = 0; i < numVertices; i++) {
            if (cities[i] != null && cities[i].getCity().getName().equals(name)) {
                return cities[i];
            }
        }
        return null;  // Return null if city is not found
    }

    public void findShortestPath(String source, String destination, String criteria) {
        resetPathProperties();  // Reset totals before starting computation

        double[] timeOrCostOrDistance = new double[numVertices];  // Store timeOrCostOrDistance for each vertex
        Vertix[] previous = new Vertix[numVertices];  // Store previous cities (vertices) for path reconstruction
        boolean[] visited = new boolean[numVertices];  // Keep track of visited cities

        // Initialize timeOrCostOrDistance to infinity and visited to false
        for (int i = 0; i < numVertices; i++) {
            timeOrCostOrDistance[i] = Double.POSITIVE_INFINITY;
            visited[i] = false;
        }

        // Set the source city distance to 0
        Vertix sourceCity = findCity(source);
        if (sourceCity == null) {
            throw new IllegalArgumentException("Source city not found in the graph.");
        }
        int sourceIndex = getCityIndex(sourceCity);
        timeOrCostOrDistance[sourceIndex] = 0;

        // Dijkstra's Algorithm to find the shortest path
        while (true) {
            int currentIndex = -1;
            double smallest = Double.POSITIVE_INFINITY;
            for (int i = 0; i < numVertices; i++) {
                if (!visited[i] && (timeOrCostOrDistance[i] < smallest)) {
                    smallest = timeOrCostOrDistance[i];
                    currentIndex = i;
                }
            }

            if (currentIndex == -1) break;  // No more cities to visit

            visited[currentIndex] = true;
            Vertix current = cities[currentIndex];  // Access city via the cities array

            if (current.getCity().getName().equals(destination)) {
                break;
            }

            // Update timeOrCostOrDistance for neighbors (edges)
            Node currentEdgeNode = current.getEdges().getFirstNode();

            while (currentEdgeNode != null) {  // Loop through each edge in the list
                Edge edge = currentEdgeNode.getEdge();
                Vertix neighbor = edge.getDest();
                int neighborIndex = getCityIndex(neighbor);

                if (visited[neighborIndex]) {
                    currentEdgeNode = currentEdgeNode.getNext();
                    continue;
                }

                // Choose the appropriate criterion
                double weight = 0;
                if (criteria.equals("cost")) {
                    weight = edge.getCost();  // Use cost
                } else if (criteria.equals("time")) {
                    weight = edge.getTime();  // Use time
                } else if (criteria.equals("distance")) {
                    weight = edge.getDistance();  // Use distance
                }

                double newValue = timeOrCostOrDistance[currentIndex] + weight;

                if (newValue < timeOrCostOrDistance[neighborIndex]) {
                    timeOrCostOrDistance[neighborIndex] = newValue;
                    previous[neighborIndex] = current;
                }

                currentEdgeNode = currentEdgeNode.getNext();
            }
        }

        // Reconstruct the path using edges and also store the path in ShortPath as a string
        LinkedList pathEdges = new LinkedList();
        ShortPath = "";  // Start with an empty path string
        Vertix current = findCity(destination);

        // Traverse the previous vertices and build the path
        while (current != null && previous[getCityIndex(current)] != null) {
            Vertix parent = previous[getCityIndex(current)];
            Node currentEdgeNode = parent.getEdges().getFirstNode();

            while (currentEdgeNode != null) {  // Continue while there are edges in the list
                Edge edge = currentEdgeNode.getEdge();

                if (edge.getDest().equals(current)) {
                    pathEdges.addFirst(edge);  // Add the edge to the front to reverse the order
                    totalDistance += edge.getDistance();  // Add the edge's distance to total
                    totalTime += edge.getTime();  // Add the edge's time to total
                    totalCost += edge.getCost();  // Add the edge's cost to total
                    break;
                }

                currentEdgeNode = currentEdgeNode.getNext();
            }
            current = parent;
        }

        // Build the path string by iterating over the reversed edges
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(source);  // Start with the source city name

        // Iterate over the edges in the reversed order using pathEdges
        Node edge = pathEdges.getFirstNode();  // Get the first edge
        while (edge != null) {
            pathBuilder.append("\n↓\n");  // Add a down arrow before each city
            pathBuilder.append(edge.getEdge().getDest().getCity().getName());  // Add destination city name
            edge = edge.getNext();
        }

        // Set the path and its weight
        if (pathBuilder.toString().split("\n↓\n").length > 1)
            ShortPath = pathBuilder.toString();  // Final path with arrows and cities in reverse order
        else
            ShortPath = "";


        shortestPathEdges = pathEdges;
    }
    // Method to calculate total distance, time, and cost for the path
//    private void calculateTotals(Vertix[] previous, Vertix source, Vertix destination) {
//        Vertix current = destination;
//        resetPathProperties();
//
//        while (current != null && previous[getCityIndex(current)] != null) {
//            Vertix parent = previous[getCityIndex(current)];
//
//            int edgeIndex = 0;  // Initialize index to iterate over the edges
//            while (edgeIndex < parent.getEdges().getSize()) {
//                Edge edge = parent.getEdges().getNodeByIndex(edgeIndex).getEdge();  // Get the edge at the current index
//                if (edge.getDest().equals(current)) {
//                    totalDistance += edge.getDistance();  // Add the edge's distance to total
//                    totalTime += edge.getTime();  // Add the edge's time to total
//                    totalCost += edge.getCost();  // Add the edge's cost to total
//                    break;  // Stop after finding the correct edge
//                }
//                edgeIndex++;  // Move to the next edge
//            }
//
//            current = parent;
//        }
//    }
    // Helper method to get the city index in the array
    private int getCityIndex(Vertix city) {
        for (int i = 0; i < numVertices; i++) {
            if (cities[i] != null && cities[i].equals(city)) {
                return i;
            }
        }
        return -1;  // City not found
    }



    // Method to reset path properties (distance, time, cost)
    public void resetPathProperties() {
        totalDistance = 0;
        totalTime = 0;
        totalCost = 0;
    }

    // Getter methods for results
    public String getShortPath() {
        return ShortPath;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public LinkedList getShortestPathEdges() {
        return shortestPathEdges;  // Return the LinkedList of edges
    }

    public Vertix[] getCities() {
        return cities;  // Return the array of Vertix objects
    }

    public String[] getCityNames() {
        String[] cityNames = new String[numVertices];  // Create an array to store the city names

        for (int i = 0; i < numVertices; i++) {
            if (cities[i] != null) {
                cityNames[i] = cities[i].getCity().getName();  // Extract city name from the Vertix object
            }
        }

        return cityNames;  // Return the array of city names
    }
}
