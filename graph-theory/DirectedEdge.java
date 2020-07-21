import util.StdOut;

/******************************************************************************
 * The {DirectedEdge} class represents a weighted edge in an EdgeWeightedDigraph.
 * Each edge consists of two integers (naming the two vertices) and a real-value weight. The data type
 * provides methods for accessing the two endpoints of the directed edge and the weight.
 */

public class DirectedEdge {
    private final int from;
    private final int to;
    private final double weight;


    public DirectedEdge(int from, int to, double weight) {
        if (from < 0) throw new IllegalArgumentException("Vertex names must be nonnegative integers");
        if (to < 0) throw new IllegalArgumentException("Vertex names must be nonnegative integers");
        if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
        this.from = from;
        this.to = to;
        this.weight = weight;
    }


    public int from() {
        return from;
    }


    public int to() {
        return to;
    }


    public double weight() {
        return weight;
    }


    public String toString() {
        return from + "->" + to + " " + String.format("%5.2f", weight);
    }

    public static void main(String[] args) {
        DirectedEdge e = new DirectedEdge(12, 34, 5.67);
        StdOut.println(e);
    }
}
