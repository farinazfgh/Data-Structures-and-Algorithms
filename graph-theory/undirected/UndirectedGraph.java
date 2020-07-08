import util.In;
import util.StdOut;

import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * The {@code Graph} class represents an undirected graph of vertices
 * named 0 through <em>V</em> – 1.
 * It supports the following two primary operations: add an edge to the graph,
 * iterate over all of the vertices adjacent to a vertex. It also provides
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
 * the vertices returned by {@link #getAdjacencyList(int)} takes time proportional
 * to the degree of the vertex.)
 * Constructing an empty graph with <em>V</em> vertices takes
 * &Theta;(<em>V</em>) time; constructing a graph with <em>E</em> edges
 * and <em>V</em> vertices takes &Theta;(<em>E</em> + <em>V</em>) time.
 * <p>
 * For additional documentation, see
 * <a href="https://algs4.cs.princeton.edu/41graph">Section 4.1</a>
 * of <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class UndirectedGraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;
    private int E;
    private Bag<Integer>[] adjacencyList;

    /**
     * Initializes an empty graph with {@code V} vertices and 0 edges.
     * param V the number of vertices
     *
     * @param V number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public UndirectedGraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        adjacencyList = (Bag<Integer>[]) new Bag[V];
        for (int vertex = 0; vertex < V; vertex++) {
            adjacencyList[vertex] = new Bag<Integer>();
        }
    }

    /**
     * Initializes a graph from the specified input stream.
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
    public UndirectedGraph(In in) {
        if (in == null) throw new IllegalArgumentException("argument is null");
        try {
            this.V = in.readInt();
            if (V < 0) throw new IllegalArgumentException("number of vertices in a Graph must be nonnegative");
            adjacencyList = (Bag<Integer>[]) new Bag[V];
            for (int vertex = 0; vertex < V; vertex++) {
                adjacencyList[vertex] = new Bag<Integer>();
            }
            int E = in.readInt();
            if (E < 0) throw new IllegalArgumentException("number of edges in a Graph must be nonnegative");
            for (int i = 0; i < E; i++) {
                int vertex = in.readInt();
                int current = in.readInt();
                validateVertex(vertex);
                validateVertex(current);
                addEdge(vertex, current);
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in UndirectedGraph constructor", e);
        }
    }


    /**
     * Initializes a new graph that is a deep copy of {@code G}.
     *
     * @param G the graph to copy
     * @throws IllegalArgumentException if {@code G} is {@code null}
     */
    public UndirectedGraph(UndirectedGraph G) {
        this.V = G.getNumberOfVertices();
        this.E = G.E();
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");

        // update adjacency lists
        adjacencyList = (Bag<Integer>[]) new Bag[V];
        for (int vertex = 0; vertex < V; vertex++) {
            adjacencyList[vertex] = new Bag<Integer>();
        }

        for (int vertex = 0; vertex < G.getNumberOfVertices(); vertex++) {
            // reverse so that adjacency list is in same order as original
            Stack<Integer> reverse = new Stack<Integer>();
            for (int current : G.adjacencyList[vertex]) {
                reverse.push(current);
            }
            for (int current : reverse) {
                adjacencyList[vertex].add(current);
            }
        }
    }

    /**
     * Returns the number of vertices in this graph.
     *
     * @return the number of vertices in this graph
     */
    public int getNumberOfVertices() {
        return V;
    }

    /**
     * Returns the number of edges in this graph.
     *
     * @return the number of edges in this graph
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
     * Adds the undirected edge vertex-current to this graph.
     *
     * @param vertex one vertex in the edge
     * @param current the other vertex in the edge
     * @throws IllegalArgumentException unless both {@code 0 <= vertex < V} and {@code 0 <= current < V}
     */
    public void addEdge(int vertex, int current) {
        validateVertex(vertex);
        validateVertex(current);
        E++;
        adjacencyList[vertex].add(current);
        adjacencyList[current].add(vertex);
    }


    /**
     * Returns the vertices adjacent to vertex {@code vertex}.
     *
     * @param vertex the vertex
     * @return the vertices adjacent to vertex {@code vertex}, as an iterable
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public Iterable<Integer> getAdjacencyList(int vertex) {
        validateVertex(vertex);
        return adjacencyList[vertex];
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
        return adjacencyList[vertex].size();
    }


    /**
     * Returns a string representation of this graph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     * followed by the <em>V</em> adjacency lists
     */
    public String toString() {
        StringBuilder source = new StringBuilder();
        source.append(V + " vertices, " + E + " edges " + NEWLINE);
        for (int vertex = 0; vertex < V; vertex++) {
            source.append(vertex + ": ");
            for (int current : adjacencyList[vertex]) {
                source.append(current + " ");
            }
            source.append(NEWLINE);
        }
        return source.toString();
    }


    /**
     * Unit tests the {@code UndirectedGraph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        UndirectedGraph G = new UndirectedGraph(in);
        StdOut.println(G);
    }
}
