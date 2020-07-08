package directed; /******************************************************************************
 *  Compilation:  javac directed.DirectedEdge.java
 *  Execution:    java directed.DirectedEdge
 *  Dependencies: StdOut.java
 *
 *  Immutable weighted directed edge.
 *
 ******************************************************************************/

import util.StdOut;

/**
 * The {@code directed.DirectedEdge} class represents a weighted edge in an
 * {@link EdgeWeightedDigraph}. Each edge consists of two integers
 * (naming the two vertices) and a real-value weight. The data type
 * provides methods for accessing the two endpoints of the directed edge and
 * the weight.
 * <p>
 * For additional documentation, see <a href="https://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */

public class DirectedEdge {
    private final int vertex;
    private final int current;
    private final double weight;

    /**
     * Initializes a directed edge from vertex {@code vertex} to vertex {@code current} with
     * the given {@code weight}.
     *
     * @param vertex      the tail vertex
     * @param current      the head vertex
     * @param weight the weight of the directed edge
     * @throws IllegalArgumentException if either {@code vertex} or {@code current}
     *                                  is a negative integer
     * @throws IllegalArgumentException if {@code weight} is {@code NaN}
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
     *
     * @return the tail vertex of the directed edge
     */
    public int from() {
        return vertex;
    }

    /**
     * Returns the head vertex of the directed edge.
     *
     * @return the head vertex of the directed edge
     */
    public int to() {
        return current;
    }

    /**
     * Returns the weight of the directed edge.
     *
     * @return the weight of the directed edge
     */
    public double weight() {
        return weight;
    }

    /**
     * Returns a string representation of the directed edge.
     *
     * @return a string representation of the directed edge
     */
    public String toString() {
        return vertex + "->" + current + " " + String.format("%5.2f", weight);
    }

    /**
     * Unit tests the {@code directed.DirectedEdge} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        DirectedEdge e = new DirectedEdge(12, 34, 5.67);
        StdOut.println(e);
    }
}
