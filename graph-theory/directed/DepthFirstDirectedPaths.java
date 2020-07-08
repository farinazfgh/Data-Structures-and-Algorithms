package directed;

import util.In;
import util.StdOut;
import java.util.Stack;
/******************************************************************************
 *  Compilation:  javac directed.DepthFirstDirectedPaths.java
 *  Execution:    java directed.DepthFirstDirectedPaths digraph.txt source
 *  Dependencies: directed.Digraph.java Stack.java
 *  Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/mediumDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/largeDG.txt
 *
 *  Determine reachability in a digraph from a given vertex using
 *  depth-first search.
 *  Runs in O(E + V) time.
 *
 *  % java directed.DepthFirstDirectedPaths tinyDG.txt 3
 *  3 to 0:  3-5-4-2-0
 *  3 to 1:  3-5-4-2-0-1
 *  3 to 2:  3-5-4-2
 *  3 to 3:  3
 *  3 to 4:  3-5-4
 *  3 to 5:  3-5
 *  3 to 6:  not connected
 *  3 to 7:  not connected
 *  3 to 8:  not connected
 *  3 to 9:  not connected
 *  3 to 10:  not connected
 *  3 to 11:  not connected
 *  3 to 12:  not connected
 *
 ******************************************************************************/

/**
 * The {@code directed.DepthFirstDirectedPaths} class represents a data type for
 * finding directed paths from a source vertex <em>source</em> to every
 * other vertex in the digraph.
 * <p>
 * This implementation uses depth-first search.
 * The constructor takes &Theta;(<em>V</em> + <em>E</em>) time in the
 * worst case, where <em>V</em> is the number of vertices and <em>E</em>
 * is the number of edges.
 * Each instance method takes &Theta;(1) time.
 * It uses &Theta;(<em>V</em>) extra space (not including the digraph).
 * <p>
 * See {@link DepthFirstDirectedPaths} for a nonrecursive implementation.
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class DepthFirstDirectedPaths {
    private boolean[] isVisited;  // isVisited[vertex] = true iff vertex is reachable from source
    private int[] fromEdge;      // fromEdge[vertex] = last edge on path from source to vertex
    private final int source;       // source vertex

    /**
     * Computes a directed path from {@code source} to every other vertex in digraph {@code G}.
     *
     * @param G the digraph
     * @param source the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= source < V}
     */
    public DepthFirstDirectedPaths(Digraph G, int source) {
        isVisited = new boolean[G.getNumberofVertices()];
        fromEdge = new int[G.getNumberofVertices()];
        this.source = source;
        validateVertex(source);
        dfs(G, source);
    }

    private void dfs(Digraph G, int vertex) {
        isVisited[vertex] = true;
        for (int current : G.getAdjacencyList(vertex)) {
            if (!isVisited[current]) {
                fromEdge[current] = vertex;
                dfs(G, current);
            }
        }
    }

    /**
     * Is there a directed path from the source vertex {@code source} to vertex {@code vertex}?
     *
     * @param vertex the vertex
     * @return {@code true} if there is a directed path from the source
     * vertex {@code source} to vertex {@code vertex}, {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public boolean hasPathTo(int vertex) {
        validateVertex(vertex);
        return isVisited[vertex];
    }


    /**
     * Returns a directed path from the source vertex {@code source} to vertex {@code vertex}, or
     * {@code null} if no such path.
     *
     * @param vertex the vertex
     * @return the sequence of vertices on a directed path from the source vertex
     * {@code source} to vertex {@code vertex}, as an Iterable
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
     * Unit tests the {@code directed.DepthFirstDirectedPaths} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        // StdOut.println(G);

        int source = Integer.parseInt(args[1]);
        DepthFirstDirectedPaths dfs = new DepthFirstDirectedPaths(G, source);

        for (int vertex = 0; vertex < G.getNumberofVertices(); vertex++) {
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
