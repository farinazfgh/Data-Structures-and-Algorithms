
/******************************************************************************
 *  An edge-weighted digraph, implemented using adjacency lists.
 ******************************************************************************/

import util.Bag;
import util.In;
import util.StdOut;
import util.StdRandom;

import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * The {EdgeWeightedDigraph} class represents a edge-weighted digraph of vertices named 0 through V - 1, where each
 * directed edge is of type {DirectedEdge} and has a real-valued weight.
 * It supports the following two primary operations:
 * 1. add a directed edge to the digraph and iterate over all of edges incident from a given vertex.
 * It also provides methods for returning the indegree or outdegree of a vertex, the number of vertices V in the digraph, and
 * the number of edges E in the digraph.
 * Parallel edges and self-loops are permitted.
 * This implementation uses an adjacency-lists representation, which is a vertex-indexed array of {Bag} objects.
 * It uses O(E + V) space, where E is the number of edges and V is the number of vertices.
 * All instance methods take O(1) time. (Though, iterating over the edges returned by {#getAdjacencyList(int)} (int)} takes time proportional
 * to the outdegree of the vertex.)
 * Constructing an empty edge-weighted digraph with V vertices  * takes O(V) time; constructing an edge-weighted digraph
 * with E edges and V vertices takes
 * O(E + V) time.
 */
public class EdgeWeightedDigraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;                // number of vertices in this digraph
    private int E;                      // number of edges in this digraph
    private Bag<DirectedEdge>[] adjacencyEdgesList;    // adj[vertex] = adjacency list for vertex vertex
    private int[] indegree;             // indegree[vertex] = indegree of vertex vertex

    public EdgeWeightedDigraph(int V) {
        if (V < 0)
            throw new IllegalArgumentException("Number of vertices in a directed.Digraph must be non-negative");
        this.V = V;
        this.E = 0;
        this.indegree = new int[V];
        adjacencyEdgesList = (Bag<DirectedEdge>[]) new Bag[V];
        for (int vertex = 0; vertex < V; vertex++)
            adjacencyEdgesList[vertex] = new Bag<>();
    }


    public EdgeWeightedDigraph(int V, int E) {
        this(V);
        if (E < 0) throw new IllegalArgumentException("Number of edges in a directed.Digraph must be nonnegative");
        for (int i = 0; i < E; i++) {
            int vertex = StdRandom.uniform(V);
            int current = StdRandom.uniform(V);
            double weight = 0.01 * StdRandom.uniform(100);
            DirectedEdge e = new DirectedEdge(vertex, current, weight);
            addEdge(e);
        }
    }

    public EdgeWeightedDigraph(In in) {
        if (in == null) throw new IllegalArgumentException("argument is null");
        try {
            this.V = in.readInt();
            if (V < 0)
                throw new IllegalArgumentException("number of vertices in a directed.Digraph must be nonnegative");
            indegree = new int[V];
            adjacencyEdgesList = (Bag<DirectedEdge>[]) new Bag[V];
            for (int vertex = 0; vertex < V; vertex++) {
                adjacencyEdgesList[vertex] = new Bag<DirectedEdge>();
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


    public EdgeWeightedDigraph(EdgeWeightedDigraph G) {
        this(G.getNumberofVertices());
        this.E = G.getNumberOfEdges();
        for (int vertex = 0; vertex < G.getNumberofVertices(); vertex++)
            this.indegree[vertex] = G.indegree(vertex);
        for (int vertex = 0; vertex < G.getNumberofVertices(); vertex++) {
            // reverse so that adjacency list is in same order as original
            Stack<DirectedEdge> reverse = new Stack<DirectedEdge>();
            for (DirectedEdge e : G.adjacencyEdgesList[vertex]) {
                reverse.push(e);
            }
            for (DirectedEdge e : reverse) {
                adjacencyEdgesList[vertex].add(e);
            }
        }
    }

    public int getNumberofVertices() {
        return V;
    }

    public int getNumberOfEdges() {
        return E;
    }

    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex >= V)
            throw new IllegalArgumentException("vertex " + vertex + " is not between 0 and " + (V - 1));
    }

    public void addEdge(DirectedEdge e) {
        int vertex = e.from();
        int current = e.to();
        validateVertex(vertex);
        validateVertex(current);
        adjacencyEdgesList[vertex].add(e);
        indegree[current]++;
        E++;
    }


    public Iterable<DirectedEdge> getAdjacencyEdgesList(int vertex) {
        validateVertex(vertex);
        return adjacencyEdgesList[vertex];
    }

    public int outdegree(int vertex) {
        validateVertex(vertex);
        return adjacencyEdgesList[vertex].size();
    }


    public int indegree(int vertex) {
        validateVertex(vertex);
        return indegree[vertex];
    }

    public Iterable<DirectedEdge> edges() {
        Bag<DirectedEdge> list = new Bag<DirectedEdge>();
        for (int vertex = 0; vertex < V; vertex++) {
            for (DirectedEdge e : getAdjacencyEdgesList(vertex)) {
                list.add(e);
            }
        }
        return list;
    }

    public String toString() {
        StringBuilder source = new StringBuilder();
        source.append(V + " " + E + NEWLINE);
        for (int vertex = 0; vertex < V; vertex++) {
            source.append(vertex + ": ");
            for (DirectedEdge e : adjacencyEdgesList[vertex]) {
                source.append(e + "  ");
            }
            source.append(NEWLINE);
        }
        return source.toString();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        StdOut.println(G);
    }
}
