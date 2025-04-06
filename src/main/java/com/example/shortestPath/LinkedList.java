package com.example.shortestPath;

public class LinkedList {

    private Node first;
    private int size; // size attribute to keep track of the number of elements

    public LinkedList() {
        first = null;
        size = 0; // Initialize size to 0
    }

    public Node getFirstNode() {
        return first;
    }

    public Node setFirstNode(Node n) {
        return first = n;
    }

    public void addFirst(Edge edge) { // O(1)
        Node temp = new Node(edge);

        // empty list
        if (first == null)
            first = temp;
        else { // adding as first
            temp.setNext(first);
            first = temp;
        }

        size++; // Increment size
    }

    public void addLast(Edge vertix) { // O(n)
        Node temp = new Node(vertix);

        // empty list
        if (first == null)
            first = temp;
        else {
            Node curr = first;
            // looping until last element
            while (curr.getNext() != null)
                curr = curr.getNext();

            curr.setNext(temp); // adding to last
        }

        size++; // Increment size
    }

    public boolean deleteFirst() { // O(1)
        if (first == null) // empty list
            return false;

        else {
            Node temp = first;
            first = first.getNext();
            temp.setNext(null);
        }
        size--; // Decrement size
        return true;
    }

    public boolean deleteLast() { // O(n)
        if (first == null) // empty list
            return false;

        else {
            Node current = first;
            // stops at one before last node
            while (current.getNext().getNext() != null)
                current = current.getNext();

            current.setNext(null);
        }
        size--; // Decrement size
        return true;
    }

    public Edge get(String name) { // O(n)
        Node curr = getNode(name);

        if (curr == null)
            return null; // not found

        else
            return curr.getEdge(); // found
    }

    public Node getNode(String name) {
        if (first != null) { // checking for empty list
            Node curr = first;

            while (curr != null) {
                if (curr.getEdge().getDest().getCity().getName().equals(name))
                    return curr; // found

                curr = curr.getNext();
            }
        }
        return null; // not found
    }

    public Edge getFirst() { // O(1)
        if (first == null)
            return null;
        return first.getEdge();
    }

    public Edge getLast() { // O(n)
        if (first == null)
            return null;

        Node curr = first;
        // looping until last element
        while (curr.getNext() != null)
            curr = curr.getNext();

        return curr.getEdge();
    }

    public void printList() { // O(n)
        Node current = first;
        if (first == null)
            return;
        while (current != null) {
            System.out.println(current.toString());
            current = current.getNext();
        }
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int getSize() {
        return size; // Return the current size
    }

    public void addAll(LinkedList list) {
        if (list == null || list.isEmpty()) {
            return; // If the provided list is null or empty, do nothing
        }

        // Get the first node of the provided list
        Node current = list.getFirstNode();

        while (current != null) {
            // Add each element of the provided list to the end of the current list
            this.addLast(current.getEdge());
            current = current.getNext();
        }
    }

    public Node getNodeByIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current;
    }

}
