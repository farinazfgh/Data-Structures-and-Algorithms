
/******************************************************************************
 *  Compilation:  javac DepthFirstPaths.java
 *  Execution:    java DepthFirstPaths G source
 *  Dependencies: UndirectedGraph.java Stack.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/41graph/tinyCG.txt
 *                https://algs4.cs.princeton.edu/41graph/tinyG.txt
 *                https://algs4.cs.princeton.edu/41graph/mediumG.txt
 *                https://algs4.cs.princeton.edu/41graph/largeG.txt
 *
 *  Run depth-first search on an undirected graph.
 *
 *  %  java UndirectedGraph tinyCG.txt
 *  6 8
 *  0: 2 1 5 
 *  1: 0 2 
 *  2: 0 1 3 4 
 *  3: 5 4 2 
 *  4: 3 2 
 *  5: 3 0 
 *
 *  % java DepthFirstPaths tinyCG.txt 0
 *  0 to 0:  0
 *  0 to 1:  0-2-1
 *  0 to 2:  0-2
 *  0 to 3:  0-2-3
 *  0 to 4:  0-2-3-4
 *  0 to 5:  0-2-3-5
 *
 ******************************************************************************/

import util.In;
import util.StdOut;

import java.util.Stack;

/**
 * The {@code DepthFirstPaths} class represents a data type for finding
 * paths from a source vertex <em>source</em> to every other vertex
 * in an undirected graph.
 * <p>
 * This implementation uses depth-first search.
 * The constructor takes &Theta;(<em>V</em> + <em>E</em>) time in the
 * worst case, where <em>V</em> is the number of vertices and
 * <em>E</em> is the number of edges.
 * Each instance method takes &Theta;(1) time.
 * It uses &Theta;(<em>V</em>) extra space (not including the graph).
 * <p>
 * For additional documentation, see
 * <a href="https://algs4.cs.princeton.edu/41graph">Section 4.1</a>
 * of <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class DepthFirstPaths {
    private boolean[] isVisited;    // isVisited[vertex] = is there an source-vertex path?
    private int[] fromEdge;        // fromEdge[vertex] = last edge on source-vertex path
    private final int source;         // source vertex

    /**
     * Computes a path between {@code source} and every other vertex in graph {@code G}.
     *
     * @param G      the graph
     * @param source the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= source < V}
     */
    public DepthFirstPaths(UndirectedGraph G, int source) {
        this.source = source;
        fromEdge = new int[G.getNumberOfVertices()];
        isVisited = new boolean[G.getNumberOfVertices()];
        validateVertex(source);
        dfs(G, source);
    }

    // depth first search from vertex
    private void dfs(UndirectedGraph G, int vertex) {
        isVisited[vertex] = true;
        for (int current : G.getAdjacencyList(vertex)) {
            if (!isVisited[current]) {
                fromEdge[current] = vertex;
                dfs(G, current);
            }
        }
    }

    /**
     * Is there a path between the source vertex {@code source} and vertex {@code vertex}?
     *
     * @param vertex the vertex
     * @return {@code true} if there is a path, {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public boolean hasPathTo(int vertex) {
        validateVertex(vertex);
        return isVisited[vertex];
    }

    /**
     * Returns a path between the source vertex {@code source} and vertex {@code vertex}, or
     * {@code null} if no such path.
     *
     * @param vertex the vertex
     * @return the sequence of vertices on a path between the source vertex
     * {@code source} and vertex {@code vertex}, as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public Iterable<Integer> pathTo(int vertex) {
        validateVertex(vertex);
        if (!hasPathTo(vertex)) return null;
        Stack<Integer> path = new Stack<Integer>();
        for (int x = vertex; x != source; x = fromEdge[x])
            path.push(x);
        path.push(source);
        return path;
    }

    // throw an IllegalArgumentException unless {@code 0 <= vertex < V}
    private void validateVertex(int vertex) {
        int V = isVisited.length;
        if (vertex < 0 || vertex >= V)
            throw new IllegalArgumentException("vertex " + vertex + " is not between 0 and " + (V - 1));
    }

    /**
     * Unit tests the {@code DepthFirstPaths} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        UndirectedGraph G = new UndirectedGraph(in);
        int source = Integer.parseInt(args[1]);
        DepthFirstPaths dfs = new DepthFirstPaths(G, source);

        for (int vertex = 0; vertex < G.getNumberOfVertices(); vertex++) {
            if (dfs.hasPathTo(vertex)) {
                StdOut.printf("%d to %d:  ", source, vertex);
                for (int x : dfs.pathTo(vertex)) {
                    if (x == source) StdOut.print(x);
                    else StdOut.print("-" + x);
                }
                StdOut.println();
            } else {
                StdOut.printf("%d to %d:  not connected\n", source, vertex);
            }
        }
    }
}
