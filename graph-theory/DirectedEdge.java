
/******************************************************************************
 *  Compilation:  javac DirectedEdge.java
 *  Immutable weighted directed edge.
 ******************************************************************************/

import util.StdOut;

/**
 * The {DirectedEdge} class represents a weighted edge in an EdgeWeightedDigraph.
 * Each edge consists of two integers (naming the two vertices) and a real-value weight. The data type
 * provides methods for accessing the two endpoints of the directed edge and the weight.
 */

public class DirectedEdge {
    private final int vertex;
    private final int current;
    private final double weight;

    /**
     * Initializes a directed edge from vertex { vertex} to vertex { current} with
     * the given { weight}.
     */
    public DirectedEdge(int vertex, int current, double weight) {
        if (vertex < 0) throw new IllegalArgumentException("Vertex names must be nonnegative integers");
        if (current < 0) throw new IllegalArgumentException("Vertex names must be nonnegative integers");
        if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
        this.vertex = vertex;
        this.current = current;
        this.weight = weight;
    }

    /**
     * Returns the tail vertex of the directed edge.
     */
    public int from() {
        return vertex;
    }

    /**
     * Returns the head vertex of the directed edge.
     */
    public int to() {
        return current;
    }

    /**
     * Returns the weight of the directed edge.
     */
    public double weight() {
        return weight;
    }

    /**
     * Returns a string representation of the directed edge.
     */
    public String toString() {
        return vertex + "->" + current + " " + String.format("%5.2f", weight);
    }

    public static void main(String[] args) {
        DirectedEdge e = new DirectedEdge(12, 34, 5.67);
        StdOut.println(e);
    }
}
