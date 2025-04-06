package com.example.shortestPath;

public class Node {


    private Edge edge;
    private Node next;

    public Node(Edge edge) {
        this.edge = edge;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge vertix) {
        this.edge = vertix;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
