import util.In;
import util.Queue;
import util.StdOut;

import java.util.Stack;
/******************************************************************************
 * Bellman-Ford shortest path algorithm. Computes the shortest path tree in
 * edge-weighted digraph G from vertex s, or finds a negative cost cycle
 * reachable from s.
 ******************************************************************************/

/**
 * The {BellmanFordSP} class represents a data type for solving the single-source shortest paths problem in edge-weighted digraphs with
 * no negative cycles.
 * The edge weights can be positive, negative, or zero.
 * This class finds either a shortest path from the source vertex s to every other vertex or a negative cycle reachable from the source vertex.
 * This implementation uses a queue-based implementation of the Bellman-Ford-Moore algorithm.
 * The constructor takes O(E V) time in the worst case, where V is the number of vertices and E is the number of edges.
 * In practice, it performs much better.
 * Each instance method takes O(1) time.
 * It uses O(V) extra space (not including the edge-weighted digraph).
 */
public class BellmanFordSP {
    private final double[] distTo; // distTo[v] = distance of shortest s->v path
    private final DirectedEdge[] parentEdge; // edgeTo[v] = last edge on shortest s->v path
    private final boolean[] isonQueue; // onQueue[v] = is v currently on the queue?
    private final Queue<Integer> queue; // queue of vertices to relax
    private int cost; // number of calls to relax()
    private Iterable<DirectedEdge> negativeCycle; // negative cycle (or null if no such cycle)

    /**
     * Computes a shortest paths tree from {s} to every other vertex in
     * the edge-weighted digraph {G}.
     */
    public BellmanFordSP(EdgeWeightedDigraph G, int source) {
        distTo = new double[G.getNumberofVertices()];
        parentEdge = new DirectedEdge[G.getNumberofVertices()];
        isonQueue = new boolean[G.getNumberofVertices()];
        for (int v = 0; v < G.getNumberofVertices(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[source] = 0.0;

        // Bellman-Ford algorithm
        queue = new Queue<>();
        queue.enqueue(source);
        isonQueue[source] = true;
        while (!queue.isEmpty() && !hasNegativeCycle()) {
            int v = queue.dequeue();
            isonQueue[v] = false;
            relax(G, v);
        }
        assert check(G, source);
    }

    private void relax(EdgeWeightedDigraph G, int v) {
        for (DirectedEdge edge : G.getAdjacencyEdgesList(v)) {
            int to = edge.to();
            if (distTo[to] > distTo[v] + edge.weight()) {
                distTo[to] = distTo[v] + edge.weight();
                parentEdge[to] = edge;
                if (!isonQueue[to]) {
                    queue.enqueue(to);
                    isonQueue[to] = true;
                }
            }
            if (++cost % G.getNumberofVertices() == 0) {
                findNegativeCycle();
                if (hasNegativeCycle()) return; // found a negative cycle
            }
        }
    }

    /**
     * Is there a negative cycle reachable from the source vertex {s}?
     */
    public boolean hasNegativeCycle() {
        return negativeCycle != null;
    }

    public Iterable<DirectedEdge> negativeCycle() {
        return negativeCycle;
    }

    private void findNegativeCycle() {
        int numberofVertices = parentEdge.length;
        EdgeWeightedDigraph spt = new EdgeWeightedDigraph(numberofVertices);
        for (DirectedEdge directedEdge : parentEdge)
            if (directedEdge != null)
                spt.addEdge(directedEdge);
        EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(spt);
        negativeCycle = finder.cycle();
    }

    /**
     * Returns the length of a shortest path from the source vertex {s} to vertex {v}.
     */
    public double distTo(int v) {
        validateVertex(v);
        if (hasNegativeCycle())
            throw new UnsupportedOperationException("Negative cost cycle exists");
        return distTo[v];
    }

    /**
     * Is there a path from the source {s} to vertex {v}?
     */
    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source {s} to vertex {v}.
     */
    public Iterable<DirectedEdge> pathTo(int v) {
        validateVertex(v);
        if (hasNegativeCycle())
            throw new UnsupportedOperationException("Negative cost cycle exists");
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = parentEdge[v]; e != null; e = parentEdge[e.from()]) {
            path.push(e);
        }
        return path;
    }

    // check optimality conditions: either
    // (i) there exists a negative cycle reacheable from s or
    // (ii) for all edges e = v->w: distTo[w] <= distTo[v] + e.weight()
    // (ii') for all edges e = v->w on the SPT: distTo[w] == distTo[v] + e.weight()
    private boolean check(EdgeWeightedDigraph G, int s) {
        // has a negative cycle
        if (hasNegativeCycle()) {
            double weight = 0.0;
            for (DirectedEdge e : negativeCycle()) {
                weight += e.weight();
            }
            if (weight >= 0.0) {
                System.err.println("error: weight of negative cycle = " + weight);
                return false;
            }
        }
    // no negative cycle reachable from source
        else {
    // check that distTo[v] and edgeTo[v] are consistent
            if (distTo[s] != 0.0 || parentEdge[s] != null) {
                System.err.println("distanceTo[s] and edgeTo[s] inconsistent");
                return false;
            }
            for (int v = 0; v < G.getNumberofVertices(); v++) {
                if (v == s) continue;
                if (parentEdge[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                    System.err.println("distTo[] and edgeTo[] inconsistent");
                    return false;
                }
            }
// check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
            for (int v = 0; v < G.getNumberofVertices(); v++) {
                for (DirectedEdge e : G.getAdjacencyEdgesList(v)) {
                    int w = e.to();
                    if (distTo[v] + e.weight() < distTo[w]) {
                        System.err.println("edge " + e + " not relaxed");
                        return false;
                    }
                }
            }
// check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
            for (int w = 0; w < G.getNumberofVertices(); w++) {
                if (parentEdge[w] == null) continue;
                DirectedEdge e = parentEdge[w];
                int v = e.from();
                if (w != e.to()) return false;
                if (distTo[v] + e.weight() != distTo[w]) {
                    System.err.println("edge " + e + " on shortest path not tight");
                    return false;
                }
            }
        }
        StdOut.println("Satisfies optimality conditions");
        StdOut.println();
        return true;
    }

    // throw an IllegalArgumentException unless {0 <= v < getNumberofVertices}
    private void validateVertex(int v) {
        int getNumberofVertices = distTo.length;
        if (v < 0 || v >= getNumberofVertices)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (getNumberofVertices - 1));
    }

    /**
     * Unit tests the {BellmanFordSP} data type.
     * <p>
     * 7/8/2020 BellmanFordSP.java
     * https://algs4.cs.princeton.edu/44sp/BellmanFordSP.java.html 5/5
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        int s = Integer.parseInt(args[1]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        BellmanFordSP sp = new BellmanFordSP(G, s);
// print negative cycle
        if (sp.hasNegativeCycle()) {
            for (DirectedEdge e : sp.negativeCycle())
                StdOut.println(e);
        }
// print shortest paths
        else {
            for (int v = 0; v < G.getNumberofVertices(); v++) {
                if (sp.hasPathTo(v)) {
                    StdOut.printf("%d to %d (%5.2f) ", s, v, sp.distTo(v));
                    for (DirectedEdge e : sp.pathTo(v)) {
                        StdOut.print(e + " ");
                    }
                    StdOut.println();
                } else {
                    StdOut.printf("%d to %d no path\n", s, v);
                }
            }
        }
    }
}
