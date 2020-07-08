/******************************************************************************
 *  Compilation:  javac EdgeWeightedDigraph.java
 *  Execution:    java EdgeWeightedDigraph digraph.txt
 *  Dependencies: util.Bag.java directed.DirectedEdge.java
 *  Data files:   https://algs4.cs.princeton.edu/44sp/tinyEWD.txt
 *                https://algs4.cs.princeton.edu/44sp/mediumEWD.txt
 *                https://algs4.cs.princeton.edu/44sp/largeEWD.txt
 *
 *  An edge-weighted digraph, implemented using adjacency lists.
 *
 ******************************************************************************/

import directed.DirectedEdge;
import util.Bag;
import util.In;
import util.StdOut;
import util.StdRandom;

import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * The {@code EdgeWeightedDigraph} class represents a edge-weighted
 * digraph of vertices named 0 through <em>numberofVertices</em> - 1, where each
 * directed edge is of type {@link DirectedEdge} and has a real-valued weight.
 * It supports the following two primary operations: add a directed edge
 * to the digraph and iterate over all of edges incident from a given vertex.
 * It also provides methods for returning the indegree or outdegree of a
 * vertex, the number of vertices <em>numberofVertices</em> in the digraph, and
 * the number of edges <em>E</em> in the digraph.
 * Parallel edges and self-loops are permitted.
 * <p>
 * This implementation uses an <em>adjacency-lists representation</em>, which
 * is a vertex-indexed array of {@link Bag} objects.
 * It uses &Theta;(<em>E</em> + <em>numberofVertices</em>) space, where <em>E</em> is
 * the number of edges and <em>numberofVertices</em> is the number of vertices.
 * All instance methods take &Theta;(1) time. (Though, iterating over
 * the edges returned by {@link #getAdjacencyList(int)} (int)} takes time proportional
 * to the outdegree of the vertex.)
 * Constructing an empty edge-weighted digraph with <em>numberofVertices</em> vertices
 * takes &Theta;(<em>numberofVertices</em>) time; constructing an edge-weighted digraph
 * with <em>E</em> edges and <em>numberofVertices</em> vertices takes
 * &Theta;(<em>E</em> + <em>numberofVertices</em>) time.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class EdgeWeightedDigraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int numberofVertices;                // number of vertices in this digraph
    private int E;                      // number of edges in this digraph
    private Bag<DirectedEdge>[] adj;    // adj[vertex] = adjacency list for vertex vertex
    private int[] indegree;             // indegree[vertex] = indegree of vertex vertex

    /**
     * Initializes an empty edge-weighted digraph with {@code numberofVertices} vertices and 0 edges.
     *
     * @param numberofVertices the number of vertices
     * @throws IllegalArgumentException if {@code numberofVertices < 0}
     */
    public EdgeWeightedDigraph(int numberofVertices) {
        if (numberofVertices < 0)
            throw new IllegalArgumentException("Number of vertices in a directed.Digraph must be nonnegative");
        this.numberofVertices = numberofVertices;
        this.E = 0;
        this.indegree = new int[numberofVertices];
        adj = (Bag<DirectedEdge>[]) new Bag[numberofVertices];
        for (int vertex = 0; vertex < numberofVertices; vertex++)
            adj[vertex] = new Bag<DirectedEdge>();
    }

    /**
     * Initializes a random edge-weighted digraph with {@code numberofVertices} vertices and <em>E</em> edges.
     *
     * @param numberofVertices the number of vertices
     * @param E                the number of edges
     * @throws IllegalArgumentException if {@code numberofVertices < 0}
     * @throws IllegalArgumentException if {@code E < 0}
     */
    public EdgeWeightedDigraph(int numberofVertices, int E) {
        this(numberofVertices);
        if (E < 0) throw new IllegalArgumentException("Number of edges in a directed.Digraph must be nonnegative");
        for (int i = 0; i < E; i++) {
            int vertex = StdRandom.uniform(numberofVertices);
            int current = StdRandom.uniform(numberofVertices);
            double weight = 0.01 * StdRandom.uniform(100);
            DirectedEdge e = new DirectedEdge(vertex, current, weight);
            addEdge(e);
        }
    }

    /**
     * Initializes an edge-weighted digraph from the specified input stream.
     * The format is the number of vertices <em>numberofVertices</em>,
     * followed by the number of edges <em>E</em>,
     * followed by <em>E</em> pairs of vertices and edge weights,
     * with each entry separated by whitespace.
     *
     * @param in the input stream
     * @throws IllegalArgumentException if {@code in} is {@code null}
     * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
    public EdgeWeightedDigraph(In in) {
        if (in == null) throw new IllegalArgumentException("argument is null");
        try {
            this.numberofVertices = in.readInt();
            if (numberofVertices < 0)
                throw new IllegalArgumentException("number of vertices in a directed.Digraph must be nonnegative");
            indegree = new int[numberofVertices];
            adj = (Bag<DirectedEdge>[]) new Bag[numberofVertices];
            for (int vertex = 0; vertex < numberofVertices; vertex++) {
                adj[vertex] = new Bag<DirectedEdge>();
            }

            int E = in.readInt();
            if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
            for (int i = 0; i < E; i++) {
                int vertex = in.readInt();
                int current = in.readInt();
                validateVertex(vertex);
                validateVertex(current);
                double weight = in.readDouble();
                addEdge(new DirectedEdge(vertex, current, weight));
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in EdgeWeightedDigraph constructor", e);
        }
    }

    /**
     * Initializes a new edge-weighted digraph that is a deep copy of {@code G}.
     *
     * @param G the edge-weighted digraph to copy
     */
    public EdgeWeightedDigraph(EdgeWeightedDigraph G) {
        this(G.getNumberofVertices());
        this.E = G.E();
        for (int vertex = 0; vertex < G.getNumberofVertices(); vertex++)
            this.indegree[vertex] = G.indegree(vertex);
        for (int vertex = 0; vertex < G.getNumberofVertices(); vertex++) {
            // reverse so that adjacency list is in same order as original
            Stack<DirectedEdge> reverse = new Stack<DirectedEdge>();
            for (DirectedEdge e : G.adj[vertex]) {
                reverse.push(e);
            }
            for (DirectedEdge e : reverse) {
                adj[vertex].add(e);
            }
        }
    }

    /**
     * Returns the number of vertices in this edge-weighted digraph.
     *
     * @return the number of vertices in this edge-weighted digraph
     */
    public int getNumberofVertices() {
        return numberofVertices;
    }

    /**
     * Returns the number of edges in this edge-weighted digraph.
     *
     * @return the number of edges in this edge-weighted digraph
     */
    public int E() {
        return E;
    }

    // throw an IllegalArgumentException unless {@code 0 <= vertex < numberofVertices}
    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex >= numberofVertices)
            throw new IllegalArgumentException("vertex " + vertex + " is not between 0 and " + (numberofVertices - 1));
    }

    /**
     * Adds the directed edge {@code e} to this edge-weighted digraph.
     *
     * @param e the edge
     * @throws IllegalArgumentException unless endpoints of edge are between {@code 0}
     *                                  and {@code numberofVertices-1}
     */
    public void addEdge(DirectedEdge e) {
        int vertex = e.from();
        int current = e.to();
        validateVertex(vertex);
        validateVertex(current);
        adj[vertex].add(e);
        indegree[current]++;
        E++;
    }


    /**
     * Returns the directed edges incident from vertex {@code vertex}.
     *
     * @param vertex the vertex
     * @return the directed edges incident from vertex {@code vertex} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= vertex < numberofVertices}
     */
    public Iterable<DirectedEdge> getAdjacencyList(int vertex) {
        validateVertex(vertex);
        return adj[vertex];
    }

    /**
     * Returns the number of directed edges incident from vertex {@code vertex}.
     * This is known as the <em>outdegree</em> of vertex {@code vertex}.
     *
     * @param vertex the vertex
     * @return the outdegree of vertex {@code vertex}
     * @throws IllegalArgumentException unless {@code 0 <= vertex < numberofVertices}
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
     * @throws IllegalArgumentException unless {@code 0 <= vertex < numberofVertices}
     */
    public int indegree(int vertex) {
        validateVertex(vertex);
        return indegree[vertex];
    }

    /**
     * Returns all directed edges in this edge-weighted digraph.
     * To iterate over the edges in this edge-weighted digraph, use foreach notation:
     * {@code for (directed.DirectedEdge e : G.edges())}.
     *
     * @return all edges in this edge-weighted digraph, as an iterable
     */
    public Iterable<DirectedEdge> edges() {
        Bag<DirectedEdge> list = new Bag<DirectedEdge>();
        for (int vertex = 0; vertex < numberofVertices; vertex++) {
            for (DirectedEdge e : getAdjacencyList(vertex)) {
                list.add(e);
            }
        }
        return list;
    }

    /**
     * Returns a string representation of this edge-weighted digraph.
     *
     * @return the number of vertices <em>numberofVertices</em>, followed by the number of edges <em>E</em>,
     * followed by the <em>numberofVertices</em> adjacency lists of edges
     */
    public String toString() {
        StringBuilder source = new StringBuilder();
        source.append(numberofVertices + " " + E + NEWLINE);
        for (int vertex = 0; vertex < numberofVertices; vertex++) {
            source.append(vertex + ": ");
            for (DirectedEdge e : adj[vertex]) {
                source.append(e + "  ");
            }
            source.append(NEWLINE);
        }
        return source.toString();
    }

    /**
     * Unit tests the {@code EdgeWeightedDigraph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        StdOut.println(G);
    }
}
