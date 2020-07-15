package dijkstra;
/******************************************************************************
 * Dijkstra's algorithm. Computes the shortest path tree.
 * Assumes all weights are nonnegative.
 ******************************************************************************/

import util.In;
import util.IndexMinPQ;
import util.StdOut;

import java.util.Stack;

/**
 * The  DijkstraSP class represents a data type for solving the single-source shortest paths problem in edge-weighted digraphs
 * where the edge weights are non-negative.
 * This implementation uses Dijkstra's algorithm with a binary heap. The constructor takes O(E log V) time in the worst case,
 * where V is the number of vertices and E is the number of edges. Each instance method takes O(1) time.
 * It uses O(V) extra space (not including the edge-weighted digraph).
 */
public class DijkstraSP {
    private final double[] distanceFromSource; // distTo[v] = distance of shortest s->v path
    private final DirectedEdge[] parentEdge; // edgeTo[v] = last edge on shortest s->v path
    private final IndexMinPQ<Double> priorityQueue; // priority queue of vertices

    /**
     * Computes a shortest-paths tree from the source vertex  s to every other vertex in the edge-weighted digraph  G.
     */
    public DijkstraSP(EdgeWeightedDigraph G, int source) {
        validateEdges(G);

        distanceFromSource = new double[G.getNumberofVertices()];

        parentEdge = new DirectedEdge[G.getNumberofVertices()];

        validateVertex(source);
        for (int v = 0; v < G.getNumberofVertices(); v++) {
            distanceFromSource[v] = Double.POSITIVE_INFINITY;
        }
        distanceFromSource[source] = 0.0;
        // relax vertices in order of distance from s
        priorityQueue = new IndexMinPQ<>(G.getNumberofVertices());
        priorityQueue.insert(source, distanceFromSource[source]);
        while (!priorityQueue.isEmpty()) {
            int v = priorityQueue.delMin();
            for (DirectedEdge e : G.getAdjacencyEdgesList(v))
                relax(e);
        }
// check optimality conditions
        assert check(G, source);
    }

    private void validateEdges(EdgeWeightedDigraph G) {
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        int s = Integer.parseInt(args[1]);
        DijkstraSP sp = new DijkstraSP(G, s);

        printShortestPath(G, s, sp);
    }

    private static void printShortestPath(EdgeWeightedDigraph g, int s, DijkstraSP sp) {
        for (int t = 0; t < g.getNumberofVertices(); t++) {
            if (sp.hasPathTo(t)) {
                StdOut.printf("%d to %d (%.2f) ", s, t, sp.getShortestDistanceFromSource(t));
                for (DirectedEdge e : sp.pathTo(t)) {
                    StdOut.print(e + " ");
                }
                StdOut.println();
            } else {
                StdOut.printf("%d to %d no path\n", s, t);
            }
        }
    }

    private void relax(DirectedEdge e) {
        //v->w
        int v = e.from(), w = e.to();
        //this means the old path from source to w is bigger than the new edge and new parent vertex that we are visiting
        // so we need to update the old path we the new shorter path
        if (distanceFromSource[w] > distanceFromSource[v] + e.weight()) {
            distanceFromSource[w] = distanceFromSource[v] + e.weight();
            parentEdge[w] = e;
            if (priorityQueue.contains(w)) {
                priorityQueue.decreasePriority(w, distanceFromSource[w]);
            } else {
                priorityQueue.insert(w, distanceFromSource[w]);
            }
        }
    }

    /**
     * Returns the length of a shortest path from the source vertex source to vertex  v.
     */
    public double getShortestDistanceFromSource(int v) {
        validateVertex(v);
        return distanceFromSource[v];
    }

    /**
     * Returns true if there is a path from the source vertex  s to vertex  v.
     */
    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distanceFromSource[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source vertex  s to vertex  v.
     */
    public Iterable<DirectedEdge> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = parentEdge[v]; e != null; e = parentEdge[e.from()]) {
            path.push(e);
        }
        return path;
    }

    // check optimality conditions:
    // (i) for all edges e: distTo[e.to()] <= distTo[e.from()] + e.weight()
    // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
    private boolean check(EdgeWeightedDigraph G, int s) {
        // check that edge weights are nonnegative
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }
        // check that distTo[v] and edgeTo[v] are consistent
        if (distanceFromSource[s] != 0.0 || parentEdge[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < G.getNumberofVertices(); v++) {
            if (v == s) continue;
            if (parentEdge[v] == null && distanceFromSource[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }
        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
        for (int v = 0; v < G.getNumberofVertices(); v++) {
            for (DirectedEdge e : G.getAdjacencyEdgesList(v)) {
                int w = e.to();
                if (distanceFromSource[v] + e.weight() < distanceFromSource[w]) {
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
            if (distanceFromSource[v] + e.weight() != distanceFromSource[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }

    // throw an IllegalArgumentException unless  0 <= v < V}
    private void validateVertex(int v) {
        int V = distanceFromSource.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }
}
