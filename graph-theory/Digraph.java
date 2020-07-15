

import util.Bag;
import util.In;
import util.StdOut;

import java.util.NoSuchElementException;
import java.util.Stack;

public class Digraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;           // number of vertices in this digraph
    private int E;                 // number of edges in this digraph
    private Bag<Integer>[] adjacencyList;    // adj[vertex] = adjacency list for vertex vertex
    private int[] indegree;        // indegree[vertex] = indegree of vertex vertex

    public Digraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices in a directed.Digraph must be nonnegative");
        this.V = V;
        this.E = 0;
        indegree = new int[V];
        adjacencyList = (Bag<Integer>[]) new Bag[V];
        for (int vertex = 0; vertex < V; vertex++) {
            adjacencyList[vertex] = new Bag<>();
        }
    }

    public Digraph(In in) {
        if (in == null) throw new IllegalArgumentException("argument is null");
        try {
            this.V = in.readInt();
            if (V < 0) throw new IllegalArgumentException("number of vertices in a directed.Digraph must be nonnegative");
            indegree = new int[V];
            adjacencyList = (Bag<Integer>[]) new Bag[V];
            for (int vertex = 0; vertex < V; vertex++) {
                adjacencyList[vertex] = new Bag<Integer>();
            }
            int E = in.readInt();
            if (E < 0) throw new IllegalArgumentException("number of edges in a directed.Digraph must be nonnegative");
            for (int i = 0; i < E; i++) {
                int vertex = in.readInt();
                int current = in.readInt();
                addEdge(vertex, current);
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in directed.Digraph constructor", e);
        }
    }

    public Digraph(Digraph G) {
        if (G == null) throw new IllegalArgumentException("argument is null");

        this.V = G.getNumberofVertices();
        this.E = G.getNumberOfEdges();
        if (V < 0) throw new IllegalArgumentException("Number of vertices in a directed.Digraph must be nonnegative");

        // update indegrees
        indegree = new int[V];
        for (int vertex = 0; vertex < V; vertex++)
            this.indegree[vertex] = G.indegree(vertex);

        // update adjacency lists
        adjacencyList = (Bag<Integer>[]) new Bag[V];
        for (int vertex = 0; vertex < V; vertex++) {
            adjacencyList[vertex] = new Bag<Integer>();
        }

        for (int vertex = 0; vertex < G.getNumberofVertices(); vertex++) {
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


    public int getNumberofVertices() {
        return V;
    }

    public int getNumberOfEdges() {
        return E;
    }


    // throw an IllegalArgumentException unless 0 <= vertex < V
    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex >= V)
            throw new IllegalArgumentException("vertex " + vertex + " is not between 0 and " + (V - 1));
    }

    public void addEdge(int from, int to) {
        validateVertex(from);
        validateVertex(to);
        //that is the only difference we have one directional edge from to to and not reverse like undirectedgrapg
        adjacencyList[from].add(to);
        indegree[to]++;
        E++;
    }

    public Iterable<Integer> getAdjacencyList(int vertex) {
        validateVertex(vertex);
        return adjacencyList[vertex];
    }

    //outdegree is the same as neighbors, that's why we didnt define a separate array like inDegeree for it
    public int outdegree(int vertex) {
        validateVertex(vertex);
        return adjacencyList[vertex].size();
    }

    public int indegree(int vertex) {
        validateVertex(vertex);
        return indegree[vertex];
    }

    public Digraph reverse() {
        Digraph reverse = new Digraph(V);
        for (int vertex = 0; vertex < V; vertex++) {
            for (int current : getAdjacencyList(vertex)) {
                reverse.addEdge(current, vertex);
            }
        }
        return reverse;
    }

    public String toString() {
        StringBuilder source = new StringBuilder();
        source.append(V + " vertices, " + E + " edges " + NEWLINE);
        for (int vertex = 0; vertex < V; vertex++) {
            source.append(String.format("%d: ", vertex));
            for (int current : adjacencyList[vertex]) {
                source.append(String.format("%d ", current));
            }
            source.append(NEWLINE);
        }
        return source.toString();
    }

    /**
     * Unit tests the {@code directed.Digraph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        StdOut.println(G);
    }
}
