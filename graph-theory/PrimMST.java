import util.In;
import util.IndexMinPQ;
import util.Queue;
import util.StdOut;
/******************************************************************************
 *  Compilation:  javac PrimMST.java
 *  Execution:    java PrimMST filename.txt
 *  Dependencies: EdgeWeightedGraph.java Edge.java Queue.java
 *                util.IndexMinPQ.java UF.java In.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 *                https://algs4.cs.princeton.edu/43mst/mediumEWG.txt
 *                https://algs4.cs.princeton.edu/43mst/largeEWG.txt
 *
 *  Compute a minimum spanning forest using Prim's algorithm.
 *
 *  %  java PrimMST tinyEWG.txt 
 *  1-7 0.19000
 *  0-2 0.26000
 *  2-3 0.17000
 *  4-5 0.35000
 *  5-7 0.28000
 *  6-2 0.40000
 *  0-7 0.16000
 *  1.81000
 *
 *  % java PrimMST mediumEWG.txt
 *  1-72   0.06506
 *  2-86   0.05980
 *  3-67   0.09725
 *  4-55   0.06425
 *  5-102  0.03834
 *  6-129  0.05363
 *  7-157  0.00516
 *  ...
 *  10.46351
 *
 *  % java PrimMST largeEWG.txt
 *  ...
 *  647.66307
 *
 ******************************************************************************/

/**
 * The {@code PrimMST} class represents a data type for computing a
 * <em>minimum spanning tree</em> in an edge-weighted graph.
 * The edge weights can be positive, zero, or negative and need not
 * be distinct. If the graph is not connected, it computes a <em>minimum
 * spanning forest</em>, which is the union of minimum spanning trees
 * in each connected component. The {@code weight()} method returns the
 * weight of a minimum spanning tree and the {@code edges()} method
 * returns its edges.
 * <p>
 * This implementation uses <em>Prim's algorithm</em> with an indexed
 * binary heap.
 * The constructor takes &Theta;(<em>E</em> log <em>V</em>) time in
 * the worst case, where <em>V</em> is the number of
 * vertices and <em>E</em> is the number of edges.
 * Each instance method takes &Theta;(1) time.
 * It uses &Theta;(<em>V</em>) extra space (not including the
 * edge-weighted graph).
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/43mst">Section 4.3</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 * For alternate implementations, see {@link LazyPrimMST}, {@link KruskalMST},
 * and {@link BoruvkaMST}.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class PrimMST {
    private static final double FLOATING_POINT_EPSILON = 1E-12;

    private Edge[] LightestEdge;        // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
    private double[] lightestEdgeWeight;      // distTo[v] = weight of shortest such edge
    private boolean[] isOnTree;     // marked[v] = true if v on tree, false otherwise
    private IndexMinPQ<Double> pq;

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     *
     * @param G the edge-weighted graph
     */
    public PrimMST(EdgeWeightedGraph G) {
        LightestEdge = new Edge[G.getNumberOfVertices()];
        lightestEdgeWeight = new double[G.getNumberOfVertices()];
        isOnTree = new boolean[G.getNumberOfVertices()];
        pq = new IndexMinPQ<Double>(G.getNumberOfVertices());
        for (int v = 0; v < G.getNumberOfVertices(); v++)
            lightestEdgeWeight[v] = Double.POSITIVE_INFINITY;

        for (int v = 0; v < G.getNumberOfVertices(); v++)      // run from each vertex to find
            if (!isOnTree[v]) prim(G, v);      // minimum spanning forest

        // check optimality conditions
        assert check(G);
    }

    // run Prim's algorithm in graph G, starting from vertex s
    private void prim(EdgeWeightedGraph G, int s) {
        lightestEdgeWeight[s] = 0.0;
        pq.insert(s, lightestEdgeWeight[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            scan(G, v);
        }
    }

    // scan vertex v
    private void scan(EdgeWeightedGraph G, int v) {
        isOnTree[v] = true;

        for (Edge current : G.getAdjacenyEdges(v)) {
            int w = current.edgeOtherVertex(v);
            if (isOnTree[w]) continue;         // v-w is obsolete edge

            if (current.weight() < lightestEdgeWeight[w]) {
                lightestEdgeWeight[w] = current.weight();
                LightestEdge[w] = current;
                if (pq.contains(w)) {
                    pq.decreasePriority(w, lightestEdgeWeight[w]);
                } else {
                    pq.insert(w, lightestEdgeWeight[w]);
                }
            }
        }
    }


    public Iterable<Edge> getMSTEdges() {
        Queue<Edge> mst = new Queue<Edge>();
        for (Edge e : LightestEdge) {
            if (e != null) {
                mst.enqueue(e);
            }
        }
        return mst;
    }


    public double getMSTWeight() {
        double weight = 0.0;
        for (Edge e : getMSTEdges())
            weight += e.weight();
        return weight;
    }


    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean check(EdgeWeightedGraph G) {

        // check weight
        double totalWeight = 0.0;
        for (Edge e : getMSTEdges()) {
            totalWeight += e.weight();
        }
        if (Math.abs(totalWeight - getMSTWeight()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, getMSTWeight());
            return false;
        }

        UF uf = new UF(G.getNumberOfVertices());

        if (isAcyclic(uf)) return false;
        if (isMinumumSpanningForest(G, uf)) return false;

        if (isMinimalSpanningTree(G)) return false;

        return true;
    }

    private boolean isAcyclic(UF uf) {
        // check that it is acyclic
        for (Edge e : getMSTEdges()) {
            int v = e.either(), w = e.edgeOtherVertex(v);
            if (uf.find(v) == uf.find(w)) {
                System.err.println("Not a forest");
                return true;
            }
            uf.union(v, w);
        }
        return false;
    }

    private boolean isMinumumSpanningForest(EdgeWeightedGraph G, UF uf) {
        // check that it is a spanning forest
        for (Edge e : G.edges()) {
            int v = e.either(), w = e.edgeOtherVertex(v);
            if (uf.find(v) != uf.find(w)) {
                System.err.println("Not a spanning forest");
                return true;
            }
        }
        return false;
    }

    private boolean isMinimalSpanningTree(EdgeWeightedGraph G) {
        UF uf;// check that it is a minimal spanning forest (cut optimality conditions)
        for (Edge e : getMSTEdges()) {

            // all edges in MST except e
            uf = new UF(G.getNumberOfVertices());
            for (Edge f : getMSTEdges()) {
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

    /**
     * Unit tests the {@code PrimMST} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        PrimMST mst = new PrimMST(G);
        for (Edge e : mst.getMSTEdges()) {
            StdOut.println(e);
        }
        StdOut.printf("%.5f\n", mst.getMSTWeight());
    }


}

