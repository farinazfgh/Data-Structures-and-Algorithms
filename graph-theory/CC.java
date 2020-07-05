/******************************************************************************
 *  Compilation:  javac CC.java
 *  Execution:    java CC filename.txt
 *  Dependencies: UndirectedGraph.java StdOut.java Queue.java
 *  Data files:   https://algs4.cs.princeton.edu/41graph/tinyG.txt
 *                https://algs4.cs.princeton.edu/41graph/mediumG.txt
 *                https://algs4.cs.princeton.edu/41graph/largeG.txt
 *
 *  Compute connected components using depth first search.
 *  Runs in O(E + V) time.
 *
 *  % java CC tinyG.txt
 *  3 components
 *  0 1 2 3 4 5 6
 *  7 8 
 *  9 10 11 12
 *
 *  % java CC mediumG.txt 
 *  1 components
 *  0 1 2 3 4 5 6 7 8 9 10 ...
 *
 *  % java -Xss50m CC largeG.txt 
 *  1 components
 *  0 1 2 3 4 5 6 7 8 9 10 ...
 *
 *  Note: This implementation uses a recursive DFS. To avoid needing
 *        a potentially very large stack size, replace with a non-recurisve
 *        DFS ala DFSNonRecursive.java.
 *
 ******************************************************************************/

import util.In;
import util.Queue;
import util.StdOut;

/**
 *  The {@code CC} class represents a data type for 
 *  determining the connected components in an undirected graph.
 *  The <em>id</em> operation determines in which connected component
 *  a given vertex lies; the <em>connected</em> operation
 *  determines whether two vertices are in the same connected component;
 *  the <em>count</em> operation determines the number of connected
 *  components; and the <em>size</em> operation determines the number
 *  of vertices in the connect component containing a given vertex.

 *  The <em>component identifier</em> of a connected component is one of the
 *  vertices in the connected component: two vertices have the same component
 *  identifier if and only if they are in the same connected component.

 *  <p>
 *  This implementation uses depth-first search.
 *  The constructor takes &Theta;(<em>V</em> + <em>E</em>) time,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the
 *  number of edges.
 *  Each instance method takes &Theta;(1) time.
 *  It uses &Theta;(<em>V</em>) extra space (not including the graph).
 *  <p>
 *  For additional documentation, see 
 *  <a href="https://algs4.cs.princeton.edu/41graph">Section 4.1</a>   
 *  of <em>Algorithms, 4th Edition</em> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class CC {
    private boolean[] isVisited;   // isVisited[vertex] = has vertex vertex been isVisited?
    private int[] id;           // id[vertex] = id of connected component containing vertex
    private int[] size;         // size[id] = number of vertices in given component
    private int count;          // number of connected components

    /**
     * Computes the connected components of the undirected graph {@code G}.
     *
     * @param G the undirected graph
     */
    public CC(UndirectedGraph G) {
        isVisited = new boolean[G.getNumberOfVertices()];
        id = new int[G.getNumberOfVertices()];
        size = new int[G.getNumberOfVertices()];
        for (int vertex = 0; vertex < G.getNumberOfVertices(); vertex++) {
            if (!isVisited[vertex]) {
                dfs(G, vertex);
                count++;
            }
        }
    }

    /**
     * Computes the connected components of the edge-weighted graph {@code G}.
     *
     * @param G the edge-weighted graph
     */
    public CC(EdgeWeightedGraph G) {
        isVisited = new boolean[G.V()];
        id = new int[G.V()];
        size = new int[G.V()];
        for (int vertex = 0; vertex < G.V(); vertex++) {
            if (!isVisited[vertex]) {
                dfs(G, vertex);
                count++;
            }
        }
    }

    // depth-first search for a UndirectedGraph
    private void dfs(UndirectedGraph G, int vertex) {
        isVisited[vertex] = true;
        id[vertex] = count;
        size[count]++;
        for (int current : G.getAdjacencyList(vertex)) {
            if (!isVisited[current]) {
                dfs(G, current);
            }
        }
    }

    // depth-first search for an EdgeWeightedGraph
    private void dfs(EdgeWeightedGraph G, int vertex) {
        isVisited[vertex] = true;
        id[vertex] = count;
        size[count]++;
        for (Edge e : G.adj(vertex)) {
            int current = e.other(vertex);
            if (!isVisited[current]) {
                dfs(G, current);
            }
        }
    }


    /**
     * Returns the component id of the connected component containing vertex {@code vertex}.
     *
     * @param  vertex the vertex
     * @return the component id of the connected component containing vertex {@code vertex}
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public int id(int vertex) {
        validateVertex(vertex);
        return id[vertex];
    }

    /**
     * Returns the number of vertices in the connected component containing vertex {@code vertex}.
     *
     * @param  vertex the vertex
     * @return the number of vertices in the connected component containing vertex {@code vertex}
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public int size(int vertex) {
        validateVertex(vertex);
        return size[id[vertex]];
    }

    /**
     * Returns the number of connected components in the graph {@code G}.
     *
     * @return the number of connected components in the graph {@code G}
     */
    public int count() {
        return count;
    }

    /**
     * Returns true if vertices {@code vertex} and {@code current} are in the same
     * connected component.
     *
     * @param  vertex one vertex
     * @param  current the other vertex
     * @return {@code true} if vertices {@code vertex} and {@code current} are in the same
     *         connected component; {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     * @throws IllegalArgumentException unless {@code 0 <= current < V}
     */
    public boolean connected(int vertex, int current) {
        validateVertex(vertex);
        validateVertex(current);
        return id(vertex) == id(current);
    }

    /**
     * Returns true if vertices {@code vertex} and {@code current} are in the same
     * connected component.
     *
     * @param  vertex one vertex
     * @param  current the other vertex
     * @return {@code true} if vertices {@code vertex} and {@code current} are in the same
     *         connected component; {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     * @throws IllegalArgumentException unless {@code 0 <= current < V}
     * @deprecated Replaced by {@link #connected(int, int)}.
     */
    @Deprecated
    public boolean areConnected(int vertex, int current) {
        validateVertex(vertex);
        validateVertex(current);
        return id(vertex) == id(current);
    }

    // throw an IllegalArgumentException unless {@code 0 <= vertex < V}
    private void validateVertex(int vertex) {
        int V = isVisited.length;
        if (vertex < 0 || vertex >= V)
            throw new IllegalArgumentException("vertex " + vertex + " is not between 0 and " + (V-1));
    }

    /**
     * Unit tests the {@code CC} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        UndirectedGraph G = new UndirectedGraph(in);
        CC cc = new CC(G);

        // number of connected components
        int m = cc.count();
        StdOut.println(m + " components");

        // compute list of vertices in each connected component
        Queue<Integer>[] components = (Queue<Integer>[]) new Queue[m];
        for (int i = 0; i < m; i++) {
            components[i] = new Queue<Integer>();
        }
        for (int vertex = 0; vertex < G.getNumberOfVertices(); vertex++) {
            components[cc.id(vertex)].enqueue(vertex);
        }

        // print results
        for (int i = 0; i < m; i++) {
            for (int vertex : components[i]) {
                StdOut.print(vertex + " ");
            }
            StdOut.println();
        }
    }
}
