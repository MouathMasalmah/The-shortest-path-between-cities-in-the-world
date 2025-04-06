package com.example.shortestPath;

public class Vertix {
	private City country;
	private LinkedList edges = new LinkedList();

	public Vertix(City country) {
		this.country = country;
	}

	// Method to add an edge to this city (from this city to another city)
	public void addEdge(Vertix destination, double cost, double time) {
		Edge edge = new Edge(this, destination, cost, time); // Create an edge from this city to the destination city
		edges.addFirst(edge); // Add the edge to the list of edges
	}

	public City getCity() {
		return country;
	}

	public LinkedList getEdges() {
		return edges;
	}


}
