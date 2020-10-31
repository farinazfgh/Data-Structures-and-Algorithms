public class FlowEdge {
    private final int v;
    private final int w;
    private final double capacity;
    private double flow;

    public FlowEdge(int v, int w, double capacity) {
        if (v < 0) throw new IllegalArgumentException("vertex number should be positive");
        if (w < 0) throw new IllegalArgumentException("vertex number should be positive");
        if (capacity < 0.0) throw new IllegalArgumentException("capacity should be positive");
        this.v = v;
        this.w = w;
        this.capacity = capacity;
    }

    public int from() {
        return v;
    }

    public int to() {
        return w;
    }

    public double capacity() {
        return capacity;
    }

    public double flow() {
        return flow;
    }

    int other(int vertex) {
        if (vertex == v) return w;
        if (vertex == w) return v;
        throw new IllegalArgumentException("invalid input!");
    }

    //residual network : forward edge what is left backward edge the flow that is there
    //there will be only one edge between two vertices if you have a full forward edge or an empty backward edge
    //means there is no capacity forward or nothing is taken from the capacity yet
    double residualCapacityTo(int vertex) {
        if (vertex == v) return flow;
        if (vertex == w) return capacity - flow;
        throw new IllegalArgumentException("invalid input!");
    }

    void addResidualFlowTo(int vertex, double delta) {
        if (delta < 0) throw new IllegalArgumentException("delta should not be negative.");
        if (vertex == v) {
            flow -= delta;
        } else if (vertex == w) {
            flow += delta;
        } else
            throw new IllegalArgumentException("invalid endpoint!");
    }
}
