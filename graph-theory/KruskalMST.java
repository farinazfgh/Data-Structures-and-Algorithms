import util.In;
import util.Queue;
import util.StdOut;

public class KruskalMST {
    private static final double FLOATING_POINT_EPSILON = 1E-12;

    private double mstWeight;
    private final Queue<Edge> mstQueueEdges = new Queue<>();

    public KruskalMST(EdgeWeightedGraph G) {
        // create heap (PQ) with all edges in the graph with weight as the priority
        MinPQ<Edge> priorityQueue = new MinPQ<>();
        for (Edge e : G.edges()) {
            priorityQueue.insert(e);
        }

        UF uf = new UF(G.getNumberOfVertices());
        while (!priorityQueue.isEmpty() && mstQueueEdges.size() < G.getNumberOfVertices() - 1) {
            Edge e = priorityQueue.delMin();
            int v = e.either();
            int w = e.edgeOtherVertex(v);
            if (uf.find(v) != uf.find(w)) { // they dont belong to the same set and v-w does not create a cycle
                uf.union(v, w);
                mstQueueEdges.enqueue(e);
                mstWeight += e.weight();
            }
        }
        assert check(G);
    }

    public Iterable<Edge> edges() {
        return mstQueueEdges;
    }

    public double weight() {
        return mstWeight;
    }

    private boolean check(EdgeWeightedGraph G) {
        // check optimality conditions (takes time proportional to E V lg* V)
        double total = 0.0;
        for (Edge e : edges()) {
            total += e.weight();
        }
        if (Math.abs(total - weight()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", total, weight());
            return false;
        }

        UF uf = new UF(G.getNumberOfVertices());
        if (isAcyclic(uf)) return false;

        if (isSpanningForest(G, uf)) return false;

        if (isMinimalSpanningForest(G)) return false;

        return true;
    }

    private boolean isMinimalSpanningForest(EdgeWeightedGraph G) {
        // check that it is a minimal spanning forest (cut optimality conditions)

        UF uf;
        for (Edge e : edges()) {

            // all edges in MST except e
            uf = new UF(G.getNumberOfVertices());
            for (Edge f : mstQueueEdges) {
                int x = f.either(), y = f.edgeOtherVertex(x);
                if (f != e) uf.union(x, y);
            }

            // check that e is min weight edge in crossing cut
            for (Edge f : G.edges()) {
                int x = f.either(), y = f.edgeOtherVertex(x);
                if (uf.find(x) != uf.find(y)) {
                    if (f.weight() < e.weight()) {
                        System.err.println("Edge " + f + " violates cut optimality conditions");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isAcyclic(UF uf) {
        for (Edge e : edges()) {
            int v = e.either(), w = e.edgeOtherVertex(v);
            if (uf.find(v) == uf.find(w)) {
                System.err.println("Not a forest");
                return true;
            }
            uf.union(v, w);
        }
        return false;
    }

    private boolean isSpanningForest(EdgeWeightedGraph G, UF uf) {
        for (Edge e : G.edges()) {
            int v = e.either(), w = e.edgeOtherVertex(v);
            if (uf.find(v) != uf.find(w)) {
                System.err.println("Not a spanning forest");
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        KruskalMST mst = new KruskalMST(G);
        for (Edge e : mst.edges()) {
            StdOut.println(e);
        }
        StdOut.printf("%.5f\n", mst.weight());
    }

}

