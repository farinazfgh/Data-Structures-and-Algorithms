/******************************************************************************
 *  Compilation:  javac Digraph.java
 *  Execution:    java Digraph filename.txt
 *  Dependencies: Bag.java In.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/mediumDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/largeDG.txt  
 *
 *  A graph, implemented using an array of lists.
 *  Parallel edges and self-loops are permitted.
 *
 *  % java Digraph tinyDG.txt
 *  13 vertices, 22 edges
 *  0: 5 1 
 *  1: 
 *  2: 0 3 
 *  3: 5 2 
 *  4: 3 2 
 *  5: 4 
 *  6: 9 4 8 0 
 *  7: 6 9
 *  8: 6 
 *  9: 11 10 
 *  10: 12 
 *  11: 4 12 
 *  12: 9 
 *
 ******************************************************************************/

import util.In;
import util.StdOut;

import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * The {@code Digraph} class represents a directed graph of vertices
 * named 0 through <em>V</em> - 1.
 * It supports the following two primary operations: add an edge to the digraph,
 * iterate over all of the vertices adjacent from a given vertex.
 * It also provides
 * methods for returning the indegree or outdegree of a vertex,
 * the number of vertices <em>V</em> in the digraph,
 * the number of edges <em>E</em> in the digraph, and the reverse digraph.
 * Parallel edges and self-loops are permitted.
 * <p>
 * This implementation uses an <em>adjacency-lists representation</em>, which
 * is a vertex-indexed array of {@link Bag} objects.
 * It uses &Theta;(<em>E</em> + <em>V</em>) space, where <em>E</em> is
 * the number of edges and <em>V</em> is the number of vertices.
 * All instance methods take &Theta;(1) time. (Though, iterating over
 * the vertices returned by {@link #adj(int)} takes time proportional
 * to the outdegree of the vertex.)
 * Constructing an empty digraph with <em>V</em> vertices takes
 * &Theta;(<em>V</em>) time; constructing a digraph with <em>E</em> edges
 * and <em>V</em> vertices takes &Theta;(<em>E</em> + <em>V</em>) time.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */

public class Digraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;           // number of vertices in this digraph
    private int E;                 // number of edges in this digraph
    private Bag<Integer>[] adj;    // adj[vertex] = adjacency list for vertex vertex
    private int[] indegree;        // indegree[vertex] = indegree of vertex vertex

    /**
     * Initializes an empty digraph with <em>V</em> vertices.
     *
     * @param V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public Digraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        this.V = V;
        this.E = 0;
        indegree = new int[V];
        adj = (Bag<Integer>[]) new Bag[V];
        for (int vertex = 0; vertex < V; vertex++) {
            adj[vertex] = new Bag<Integer>();
        }
    }

    /**
     * Initializes a digraph from the specified input stream.
     * The format is the number of vertices <em>V</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices, with each entry separated by whitespace.
     *
     * @param in the input stream
     * @throws IllegalArgumentException if {@code in} is {@code null}
     * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     * @throws IllegalArgumentException if the input stream is in the wrong format
     */
    public Digraph(In in) {
        if (in == null) throw new IllegalArgumentException("argument is null");
        try {
            this.V = in.readInt();
            if (V < 0) throw new IllegalArgumentException("number of vertices in a Digraph must be nonnegative");
            indegree = new int[V];
            adj = (Bag<Integer>[]) new Bag[V];
            for (int vertex = 0; vertex < V; vertex++) {
                adj[vertex] = new Bag<Integer>();
            }
            int E = in.readInt();
            if (E < 0) throw new IllegalArgumentException("number of edges in a Digraph must be nonnegative");
            for (int i = 0; i < E; i++) {
                int vertex = in.readInt();
                int current = in.readInt();
                addEdge(vertex, current);
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in Digraph constructor", e);
        }
    }

    /**
     * Initializes a new digraph that is a deep copy of the specified digraph.
     *
     * @param G the digraph to copy
     * @throws IllegalArgumentException if {@code G} is {@code null}
     */
    public Digraph(Digraph G) {
        if (G == null) throw new IllegalArgumentException("argument is null");

        this.V = G.V();
        this.E = G.E();
        if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");

        // update indegrees
        indegree = new int[V];
        for (int vertex = 0; vertex < V; vertex++)
            this.indegree[vertex] = G.indegree(vertex);

        // update adjacency lists
        adj = (Bag<Integer>[]) new Bag[V];
        for (int vertex = 0; vertex < V; vertex++) {
            adj[vertex] = new Bag<Integer>();
        }

        for (int vertex = 0; vertex < G.V(); vertex++) {
            // reverse so that adjacency list is in same order as original
            Stack<Integer> reverse = new Stack<Integer>();
            for (int current : G.adj[vertex]) {
                reverse.push(current);
            }
            for (int current : reverse) {
                adj[vertex].add(current);
            }
        }
    }

    /**
     * Returns the number of vertices in this digraph.
     *
     * @return the number of vertices in this digraph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this digraph.
     *
     * @return the number of edges in this digraph
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
     * Adds the directed edge vertex→current to this digraph.
     *
     * @param vertex the tail vertex
     * @param current the head vertex
     * @throws IllegalArgumentException unless both {@code 0 <= vertex < V} and {@code 0 <= current < V}
     */
    public void addEdge(int vertex, int current) {
        validateVertex(vertex);
        validateVertex(current);
        adj[vertex].add(current);
        indegree[current]++;
        E++;
    }

    /**
     * Returns the vertices adjacent from vertex {@code vertex} in this digraph.
     *
     * @param vertex the vertex
     * @return the vertices adjacent from vertex {@code vertex} in this digraph, as an iterable
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public Iterable<Integer> adj(int vertex) {
        validateVertex(vertex);
        return adj[vertex];
    }

    /**
     * Returns the number of directed edges incident from vertex {@code vertex}.
     * This is known as the <em>outdegree</em> of vertex {@code vertex}.
     *
     * @param vertex the vertex
     * @return the outdegree of vertex {@code vertex}
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public int outdegree(int vertex) {
        validateVertex(vertex);
        return adj[vertex].size();
    }

    /**
     * Returns the number of directed edges incident to vertex {@code vertex}.
     * This is known as the <em>indegree</em> of vertex {@code vertex}.
     *
     * @param vertex the vertex
     * @return the indegree of vertex {@code vertex}
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public int indegree(int vertex) {
        validateVertex(vertex);
        return indegree[vertex];
    }

    /**
     * Returns the reverse of the digraph.
     *
     * @return the reverse of the digraph
     */
    public Digraph reverse() {
        Digraph reverse = new Digraph(V);
        for (int vertex = 0; vertex < V; vertex++) {
            for (int current : adj(vertex)) {
                reverse.addEdge(current, vertex);
            }
        }
        return reverse;
    }

    /**
     * Returns a string representation of the graph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     * followed by the <em>V</em> adjacency lists
     */
    public String toString() {
        StringBuilder source = new StringBuilder();
        source.append(V + " vertices, " + E + " edges " + NEWLINE);
        for (int vertex = 0; vertex < V; vertex++) {
            source.append(String.format("%d: ", vertex));
            for (int current : adj[vertex]) {
                source.append(String.format("%d ", current));
            }
            source.append(NEWLINE);
        }
        return source.toString();
    }

    /**
     * Unit tests the {@code Digraph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        StdOut.println(G);
    }
}
