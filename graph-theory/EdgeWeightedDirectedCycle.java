
/******************************************************************************
 *  Compilation:  javac EdgeWeightedDirectedCycle.java
 *  Execution:    java EdgeWeightedDirectedCycle getNumberofVertices E F
 *  Dependencies: mst.EdgeWeightedDigraph.java directed.DirectedEdge.java Stack.java
 *
 *  Finds a directed cycle in an edge-weighted digraph.
 *  Runs in O(E + getNumberofVertices) time.
 *
 *
 ******************************************************************************/

import directed.DirectedEdge;
import directed.Topological;
import mst.EdgeWeightedDigraph;
import util.StdOut;
import util.StdRandom;

import java.util.Stack;

/**
 * The {@code EdgeWeightedDirectedCycle} class represents a data type for
 * determining whether an edge-weighted digraph has a directed cycle.
 * The <em>hasCycle</em> operation determines whether the edge-weighted
 * digraph has a directed cycle and, if so, the <em>cycle</em> operation
 * returns one.
 * <p>
 * This implementation uses <em>depth-first search</em>.
 * The constructor takes &Theta;(<em>getNumberofVertices</em> + <em>E</em>) time in the
 * worst case, where <em>getNumberofVertices</em> is the number of vertices and
 * <em>E</em> is the number of edges.
 * Each instance method takes &Theta;(1) time.
 * It uses &Theta;(<em>getNumberofVertices</em>) extra space (not including the
 * edge-weighted digraph).
 * <p>
 * See {@link Topological} to compute a topological order if the
 * edge-weighted digraph is acyclic.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class EdgeWeightedDirectedCycle {
    private boolean[] isVisited;             // isVisited[vertex] = has vertex vertex been isVisited?
    private DirectedEdge[] fromEdge;        // fromEdge[vertex] = previous edge on path to vertex
    private boolean[] onStack;            // onStack[vertex] = is vertex on the stack?
    private Stack<DirectedEdge> cycle;    // directed cycle (or null if no such cycle)

    /**
     * Determines whether the edge-weighted digraph {@code G} has a directed cycle and,
     * if so, finds such a cycle.
     *
     * @param G the edge-weighted digraph
     */
    public EdgeWeightedDirectedCycle(EdgeWeightedDigraph G) {
        isVisited = new boolean[G.getNumberofVertices()];
        onStack = new boolean[G.getNumberofVertices()];
        fromEdge = new DirectedEdge[G.getNumberofVertices()];
        for (int vertex = 0; vertex < G.getNumberofVertices(); vertex++)
            if (!isVisited[vertex]) dfs(G, vertex);

        // check that digraph has a cycle
        assert check();
    }

    // check that algorithm computes either the topological order or finds a directed cycle
    private void dfs(EdgeWeightedDigraph G, int vertex) {
        onStack[vertex] = true;
        isVisited[vertex] = true;
        for (DirectedEdge e : G.getAdjacencyList(vertex)) {
            int current = e.to();

            // short circuit if directed cycle found
            if (cycle != null) return;

                // found new vertex, so recur
            else if (!isVisited[current]) {
                fromEdge[current] = e;
                dfs(G, current);
            }

            // trace back directed cycle
            else if (onStack[current]) {
                cycle = new Stack<DirectedEdge>();

                DirectedEdge f = e;
                while (f.from() != current) {
                    cycle.push(f);
                    f = fromEdge[f.from()];
                }
                cycle.push(f);

                return;
            }
        }

        onStack[vertex] = false;
    }

    /**
     * Does the edge-weighted digraph have a directed cycle?
     *
     * @return {@code true} if the edge-weighted digraph has a directed cycle,
     * {@code false} otherwise
     */
    public boolean hasCycle() {
        return cycle != null;
    }

    /**
     * Returns a directed cycle if the edge-weighted digraph has a directed cycle,
     * and {@code null} otherwise.
     *
     * @return a directed cycle (as an iterable) if the edge-weighted digraph
     * has a directed cycle, and {@code null} otherwise
     */
    public Iterable<DirectedEdge> cycle() {
        return cycle;
    }


    // certify that digraph is either acyclic or has a directed cycle
    private boolean check() {

        // edge-weighted digraph is cyclic
        if (hasCycle()) {
            // verify cycle
            DirectedEdge first = null, last = null;
            for (DirectedEdge e : cycle()) {
                if (first == null) first = e;
                if (last != null) {
                    if (last.to() != e.from()) {
                        System.err.printf("cycle edges %source and %source not incident\n", last, e);
                        return false;
                    }
                }
                last = e;
            }

            if (last.to() != first.from()) {
                System.err.printf("cycle edges %source and %source not incident\n", last, first);
                return false;
            }
        }


        return true;
    }

    /**
     * Unit tests the {@code EdgeWeightedDirectedCycle} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        // create random DAG with getNumberofVertices vertices and E edges; then add F random edges
        int getNumberofVertices = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        int F = Integer.parseInt(args[2]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(getNumberofVertices);
        int[] vertices = new int[getNumberofVertices];
        for (int i = 0; i < getNumberofVertices; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);
        for (int i = 0; i < E; i++) {
            int vertex, current;
            do {
                vertex = StdRandom.uniform(getNumberofVertices);
                current = StdRandom.uniform(getNumberofVertices);
            } while (vertex >= current);
            double weight = StdRandom.uniform();
            G.addEdge(new DirectedEdge(vertex, current, weight));
        }

        // add F extra edges
        for (int i = 0; i < F; i++) {
            int vertex = StdRandom.uniform(getNumberofVertices);
            int current = StdRandom.uniform(getNumberofVertices);
            double weight = StdRandom.uniform(0.0, 1.0);
            G.addEdge(new DirectedEdge(vertex, current, weight));
        }

        StdOut.println(G);

        // find a directed cycle
        EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(G);
        if (finder.hasCycle()) {
            StdOut.print("undirected.Cycle: ");
            for (DirectedEdge e : finder.cycle()) {
                StdOut.print(e + " ");
            }
            StdOut.println();
        }

        // or give topologial sort
        else {
            StdOut.println("No directed cycle");
        }
    }

}

