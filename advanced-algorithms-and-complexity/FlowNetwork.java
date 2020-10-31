import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlowNetwork {
    private static Random random;    // pseudo-random number generator
    private static long seed;        // pseudo-random number generator seed

    static {
        // this is how the seed was set in Java 1.4
        seed = System.currentTimeMillis();
        random = new Random(seed);
    }

    private static final String NEWLINE = System.getProperty("line.separator");
    final int V;
    int E;
    List<FlowEdge>[] adj;

    public FlowNetwork(int v) {
        if (v < 0) throw new IllegalArgumentException("Number of vertices in a Graph must be nonnegative");
        this.V = v;
        this.E = 0;
        adj = (List<FlowEdge>[]) new ArrayList[V];
        for (int i = 0; i < adj.length; i++) {
            adj[i] = new ArrayList<>();
        }
    }
    public Iterable<FlowEdge> getAdj(int v) {
        validateVertex(v);
        return adj[v];
    }
    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }


    public FlowNetwork(int v, int e) {
        this(v);
        if (e < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
        for (int i = 0; i < e; i++) {
            int from = uniform(V);
            int to = uniform(V);
            double capacity = uniform(100);
            addEdge(new FlowEdge(from, to, capacity));
        }
    }

    public void addEdge(FlowEdge edge) {
        int v = edge.from();
        int w = edge.to();
        adj[v].add(edge);
        adj[w].add(edge);
        E++;
    }

    public static int uniform(int n) {
        if (n <= 0) throw new IllegalArgumentException("argument must be positive: " + n);
        return random.nextInt(n);
    }

    public int V() {
        return V;
    }
}
