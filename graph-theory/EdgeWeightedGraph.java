/******************************************************************************
 *  Compilation:  javac EdgeWeightedGraph.java
 *  Execution:    java EdgeWeightedGraph filename.txt
 *  Dependencies: util.Bag.java mst.Edge.java In.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 *                https://algs4.cs.princeton.edu/43mst/mediumEWG.txt
 *                https://algs4.cs.princeton.edu/43mst/largeEWG.txt
 *
 *  An edge-weighted undirected graph, implemented using adjacency lists.
 *  Parallel edges and self-loops are permitted.
 *
 *  % java EdgeWeightedGraph tinyEWG.txt 
 *  8 16
 *  0: 6-0 0.58000  0-2 0.26000  0-4 0.38000  0-7 0.16000  
 *  1: 1-3 0.29000  1-2 0.36000  1-7 0.19000  1-5 0.32000  
 *  2: 6-2 0.40000  2-7 0.34000  1-2 0.36000  0-2 0.26000  2-3 0.17000  
 *  3: 3-6 0.52000  1-3 0.29000  2-3 0.17000  
 *  4: 6-4 0.93000  0-4 0.38000  4-7 0.37000  4-5 0.35000  
 *  5: 1-5 0.32000  5-7 0.28000  4-5 0.35000  
 *  6: 6-4 0.93000  6-0 0.58000  3-6 0.52000  6-2 0.40000
 *  7: 2-7 0.34000  1-7 0.19000  0-7 0.16000  5-7 0.28000  4-7 0.37000
 *
 ******************************************************************************/

import mst.Edge;
import util.Bag;
import util.In;
import util.StdOut;
import util.StdRandom;

import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * The {@code EdgeWeightedGraph} class represents an edge-weighted
 * graph of vertices named 0 through <em>V</em> – 1, where each
 * undirected edge is of type {@link Edge} and has a real-valued weight.
 * It supports the following two primary operations: add an edge to the graph,
 * iterate over all of the edges incident to a vertex. It also provides
 * methods for returning the degree of a vertex, the number of vertices
 * <em>V</em> in the graph, and the number of edges <em>E</em> in the graph.
 * Parallel edges and self-loops are permitted.
 * By convention, a self-loop <em>vertex</em>-<em>vertex</em> appears in the
 * adjacency list of <em>vertex</em> twice and contributes two to the degree
 * of <em>vertex</em>.
 * <p>
 * This implementation uses an <em>adjacency-lists representation</em>, which
 * is a vertex-indexed array of {@link Bag} objects.
 * It uses &Theta;(<em>E</em> + <em>V</em>) space, where <em>E</em> is
 * the number of edges and <em>V</em> is the number of vertices.
 * All instance methods take &Theta;(1) time. (Though, iterating over
 * the edges returned by {@link #adj(int)} takes time proportional
 * to the degree of the vertex.)
 * Constructing an empty edge-weighted graph with <em>V</em> vertices takes
 * &Theta;(<em>V</em>) time; constructing a edge-weighted graph with
 * <em>E</em> edges and <em>V</em> vertices takes
 * &Theta;(<em>E</em> + <em>V</em>) time.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/43mst">Section 4.3</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class EdgeWeightedGraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;
    private int E;
    private Bag<Edge>[] adjacenyList;

    /**
     * Initializes an empty edge-weighted graph with {@code V} vertices and 0 edges.
     *
     * @param V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public EdgeWeightedGraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        adjacenyList = (Bag<Edge>[]) new Bag[V];
        for (int vertex = 0; vertex < V; vertex++) {
            adjacenyList[vertex] = new Bag<Edge>();
        }
    }

    /**
     * Initializes a random edge-weighted graph with {@code V} vertices and <em>E</em> edges.
     *
     * @param V the number of vertices
     * @param E the number of edges
     * @throws IllegalArgumentException if {@code V < 0}
     * @throws IllegalArgumentException if {@code E < 0}
     */
    public EdgeWeightedGraph(int V, int E) {
        this(V);
        if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
        for (int i = 0; i < E; i++) {
            int vertex = StdRandom.uniform(V);
            int current = StdRandom.uniform(V);
            double weight = Math.round(100 * StdRandom.uniform()) / 100.0;
            Edge e = new Edge(vertex, current, weight);
            addEdge(e);
        }
    }

    /**
     * Initializes an edge-weighted graph from an input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices and edge weights,
     * with each entry separated by whitespace.
     *
     * @param in the input stream
     * @throws IllegalArgumentException if {@code in} is {@code null}
     * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
    public EdgeWeightedGraph(In in) {
        if (in == null) throw new IllegalArgumentException("argument is null");

        try {
            V = in.readInt();
            adjacenyList = (Bag<Edge>[]) new Bag[V];
            for (int vertex = 0; vertex < V; vertex++) {
                adjacenyList[vertex] = new Bag<Edge>();
            }

            int E = in.readInt();
            if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
            for (int i = 0; i < E; i++) {
                int vertex = in.readInt();
                int current = in.readInt();
                validateVertex(vertex);
                validateVertex(current);
                double weight = in.readDouble();
                Edge e = new Edge(vertex, current, weight);
                addEdge(e);
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in EdgeWeightedGraph constructor", e);
        }

    }

    /**
     * Initializes a new edge-weighted graph that is a deep copy of {@code G}.
     *
     * @param G the edge-weighted graph to copy
     */
    public EdgeWeightedGraph(EdgeWeightedGraph G) {
        this(G.V());
        this.E = G.E();
        for (int vertex = 0; vertex < G.V(); vertex++) {
            // reverse so that adjacency list is in same order as original
            Stack<Edge> reverse = new Stack<Edge>();
            for (Edge e : G.adjacenyList[vertex]) {
                reverse.push(e);
            }
            for (Edge e : reverse) {
                adjacenyList[vertex].add(e);
            }
        }
    }


    /**
     * Returns the number of vertices in this edge-weighted graph.
     *
     * @return the number of vertices in this edge-weighted graph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this edge-weighted graph.
     *
     * @return the number of edges in this edge-weighted graph
     */
    public int E() {
        return E;
    }

    // throw an IllegalArgumentException unless {@code 0 <= vertex < V}
    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex >= V)
            throw new IllegalArgumentException("vertex " + vertex + " is not between 0 and " + (V - 1));
    }

    /**
     * Adds the undirected edge {@code e} to this edge-weighted graph.
     *
     * @param e the edge
     * @throws IllegalArgumentException unless both endpoints are between {@code 0} and {@code V-1}
     */
    public void addEdge(Edge e) {
        int vertex = e.either();
        int current = e.other(vertex);
        validateVertex(vertex);
        validateVertex(current);
        adjacenyList[vertex].add(e);
        adjacenyList[current].add(e);
        E++;
    }

    /**
     * Returns the edges incident on vertex {@code vertex}.
     *
     * @param vertex the vertex
     * @return the edges incident on vertex {@code vertex} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public Iterable<Edge> adj(int vertex) {
        validateVertex(vertex);
        return adjacenyList[vertex];
    }

    /**
     * Returns the degree of vertex {@code vertex}.
     *
     * @param vertex the vertex
     * @return the degree of vertex {@code vertex}
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public int degree(int vertex) {
        validateVertex(vertex);
        return adjacenyList[vertex].size();
    }

    /**
     * Returns all edges in this edge-weighted graph.
     * To iterate over the edges in this edge-weighted graph, use foreach notation:
     * {@code for (mst.Edge e : G.edges())}.
     *
     * @return all edges in this edge-weighted graph, as an iterable
     */
    public Iterable<Edge> edges() {
        Bag<Edge> list = new Bag<Edge>();
        for (int vertex = 0; vertex < V; vertex++) {
            int selfLoops = 0;
            for (Edge e : adj(vertex)) {
                if (e.other(vertex) > vertex) {
                    list.add(e);
                }
                // add only one copy of each self loop (self loops will be consecutive)
                else if (e.other(vertex) == vertex) {
                    if (selfLoops % 2 == 0) list.add(e);
                    selfLoops++;
                }
            }
        }
        return list;
    }

    /**
     * Returns a string representation of the edge-weighted graph.
     * This method takes time proportional to <em>E</em> + <em>V</em>.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     * followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        StringBuilder source = new StringBuilder();
        source.append(V + " " + E + NEWLINE);
        for (int vertex = 0; vertex < V; vertex++) {
            source.append(vertex + ": ");
            for (Edge e : adjacenyList[vertex]) {
                source.append(e + "  ");
            }
            source.append(NEWLINE);
        }
        return source.toString();
    }

    /**
     * Unit tests the {@code EdgeWeightedGraph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        StdOut.println(G);
    }

}
