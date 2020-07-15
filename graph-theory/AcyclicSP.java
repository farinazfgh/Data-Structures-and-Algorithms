import dijkstra.DirectedEdge;
import dijkstra.EdgeWeightedDigraph;
import util.In;
import util.StdOut;

import java.util.Stack;

/**
 * Computes a shortest paths tree from { s} to every other vertex in the directed acyclic graph {G}.
 * The { AcyclicSP} class represents a data type for solving the single-source shortest paths problem in edge-weighted directed acyclic
 * graphs (DAGs).
 * The edge weights can be positive, negative, or zero.
 * This implementation uses a topological-sort based algorithm.
 * The constructor takes O(V + E) time in the worst case, where V is the number of vertices and E is the number of edges.
 * Each instance method takes O(1) time.
 * It uses O(V) extra space (not including the edge-weighted digraph).
 */
public class AcyclicSP {
    private double[] distanceFromSource; // distTo[v] = distance of shortest s->v path
    private DirectedEdge[] parentEdge; // edgeTo[v] = last edge on shortest s->v path

    public AcyclicSP(EdgeWeightedDigraph G, int s) {
        distanceFromSource = new double[G.getNumberofVertices()];
        parentEdge = new DirectedEdge[G.getNumberofVertices()];
        validateVertex(s);
        for (int v = 0; v < G.getNumberofVertices(); v++)
            distanceFromSource[v] = Double.POSITIVE_INFINITY;
        distanceFromSource[s] = 0.0;
        // visit vertices in topological order
        Topological topological = new Topological(G);
        if (!topological.hasOrder())
            throw new IllegalArgumentException("Digraph is not acyclic.");
        for (int v : topological.order()) {
            for (DirectedEdge e : G.getAdjacencyEdgesList(v))
                relax(e);
        }
    }

    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distanceFromSource[w] > distanceFromSource[v] + e.weight()) {
            distanceFromSource[w] = distanceFromSource[v] + e.weight();
            parentEdge[w] = e;
        }
    }

    //Returns the length of a shortest path from the source vertex { s} to vertex { v}.
    public double distTo(int v) {
        validateVertex(v);
        return distanceFromSource[v];
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distanceFromSource[v] < Double.POSITIVE_INFINITY;
    }


    public Iterable<DirectedEdge> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = parentEdge[v]; e != null; e = parentEdge[e.from()]) {
            path.push(e);
        }
        return path;
    }

    // throw an IllegalArgumentException unless { 0 <= v < V}
    private void validateVertex(int v) {
        int V = distanceFromSource.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int s = Integer.parseInt(args[1]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        // find shortest path from s to each other vertex in DAG
        AcyclicSP sp = new AcyclicSP(G, s);
        for (int v = 0; v < G.getNumberofVertices(); v++) {
            if (sp.hasPathTo(v)) {
                StdOut.printf("%d to %d (%.2f) ", s, v, sp.distTo(v));
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
