/******************************************************************************
 *  Compilation:  javac BreadthFirstDirectedPaths.java
 *  Execution:    java BreadthFirstDirectedPaths digraph.txt source
 *  Dependencies: Digraph.java Queue.java Stack.java
 *  Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/mediumDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/largeDG.txt
 *
 *  Run breadth-first search on a digraph.
 *  Runs in O(E + V) time.
 *
 *  % java BreadthFirstDirectedPaths tinyDG.txt 3
 *  3 to 0 (2):  3->2->0
 *  3 to 1 (3):  3->2->0->1
 *  3 to 2 (1):  3->2
 *  3 to 3 (0):  3
 *  3 to 4 (2):  3->5->4
 *  3 to 5 (1):  3->5
 *  3 to 6 (-):  not connected
 *  3 to 7 (-):  not connected
 *  3 to 8 (-):  not connected
 *  3 to 9 (-):  not connected
 *  3 to 10 (-):  not connected
 *  3 to 11 (-):  not connected
 *  3 to 12 (-):  not connected
 *
 ******************************************************************************/

import util.In;
import util.Queue;
import util.StdOut;

import java.util.Stack;

/**
 * The {@code BreadthDirectedFirstPaths} class represents a data type for
 * finding shortest paths (number of edges) from a source vertex <em>source</em>
 * (or set of source vertices) to every other vertex in the digraph.
 * <p>
 * This implementation uses breadth-first search.
 * The constructor takes &Theta;(<em>V</em> + <em>E</em>) time in the
 * worst case, where <em>V</em> is the number of vertices and <em>E</em> is
 * the number of edges.
 * Each instance method takes &Theta;(1) time.
 * It uses &Theta;(<em>V</em>) extra space (not including the digraph).
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class BreadthFirstDirectedPaths {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] isVisited;  // isVisited[vertex] = is there an source->vertex path?
    private int[] fromEdge;      // fromEdge[vertex] = last edge on shortest source->vertex path
    private int[] distTo;      // distTo[vertex] = length of shortest source->vertex path

    /**
     * Computes the shortest path from {@code source} and every other vertex in graph {@code G}.
     *
     * @param G the digraph
     * @param source the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public BreadthFirstDirectedPaths(Digraph G, int source) {
        isVisited = new boolean[G.V()];
        distTo = new int[G.V()];
        fromEdge = new int[G.V()];
        for (int vertex = 0; vertex < G.V(); vertex++)
            distTo[vertex] = INFINITY;
        validateVertex(source);
        bfs(G, source);
    }

    /**
     * Computes the shortest path from any one of the source vertices in {@code sources}
     * to every other vertex in graph {@code G}.
     *
     * @param G       the digraph
     * @param sources the source vertices
     * @throws IllegalArgumentException if {@code sources} is {@code null}
     * @throws IllegalArgumentException unless each vertex {@code vertex} in
     *                                  {@code sources} satisfies {@code 0 <= vertex < V}
     */
    public BreadthFirstDirectedPaths(Digraph G, Iterable<Integer> sources) {
        isVisited = new boolean[G.V()];
        distTo = new int[G.V()];
        fromEdge = new int[G.V()];
        for (int vertex = 0; vertex < G.V(); vertex++)
            distTo[vertex] = INFINITY;
        validateVertices(sources);
        bfs(G, sources);
    }

    // BFS from single source
    private void bfs(Digraph G, int source) {
        Queue<Integer> q = new Queue<Integer>();
        isVisited[source] = true;
        distTo[source] = 0;
        q.enqueue(source);
        while (!q.isEmpty()) {
            int vertex = q.dequeue();
            for (int current : G.adj(vertex)) {
                if (!isVisited[current]) {
                    fromEdge[current] = vertex;
                    distTo[current] = distTo[vertex] + 1;
                    isVisited[current] = true;
                    q.enqueue(current);
                }
            }
        }
    }

    // BFS from multiple sources
    private void bfs(Digraph G, Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<Integer>();
        for (int source : sources) {
            isVisited[source] = true;
            distTo[source] = 0;
            q.enqueue(source);
        }
        while (!q.isEmpty()) {
            int vertex = q.dequeue();
            for (int current : G.adj(vertex)) {
                if (!isVisited[current]) {
                    fromEdge[current] = vertex;
                    distTo[current] = distTo[vertex] + 1;
                    isVisited[current] = true;
                    q.enqueue(current);
                }
            }
        }
    }

    /**
     * Is there a directed path from the source {@code source} (or sources) to vertex {@code vertex}?
     *
     * @param vertex the vertex
     * @return {@code true} if there is a directed path, {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public boolean hasPathTo(int vertex) {
        validateVertex(vertex);
        return isVisited[vertex];
    }

    /**
     * Returns the number of edges in a shortest path from the source {@code source}
     * (or sources) to vertex {@code vertex}?
     *
     * @param vertex the vertex
     * @return the number of edges in a shortest path
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public int distTo(int vertex) {
        validateVertex(vertex);
        return distTo[vertex];
    }

    /**
     * Returns a shortest path from {@code source} (or sources) to {@code vertex}, or
     * {@code null} if no such path.
     *
     * @param vertex the vertex
     * @return the sequence of vertices on a shortest path, as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public Iterable<Integer> pathTo(int vertex) {
        validateVertex(vertex);

        if (!hasPathTo(vertex)) return null;
        Stack<Integer> path = new Stack<Integer>();
        int x;
        for (x = vertex; distTo[x] != 0; x = fromEdge[x])
            path.push(x);
        path.push(x);
        return path;
    }

    // throw an IllegalArgumentException unless {@code 0 <= vertex < V}
    private void validateVertex(int vertex) {
        int V = isVisited.length;
        if (vertex < 0 || vertex >= V)
            throw new IllegalArgumentException("vertex " + vertex + " is not between 0 and " + (V - 1));
    }

    // throw an IllegalArgumentException unless {@code 0 <= vertex < V}
    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        for (Integer vertex : vertices) {
            if (vertex == null) {
                throw new IllegalArgumentException("vertex is null");
            }
            validateVertex(vertex);
        }
    }


    /**
     * Unit tests the {@code BreadthFirstDirectedPaths} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        // StdOut.println(G);

        int source = Integer.parseInt(args[1]);
        BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(G, source);

        for (int vertex = 0; vertex < G.V(); vertex++) {
            if (bfs.hasPathTo(vertex)) {
                StdOut.printf("%d to %d (%d):  ", source, vertex, bfs.distTo(vertex));
                for (int x : bfs.pathTo(vertex)) {
                    if (x == source) StdOut.print(x);
                    else StdOut.print("->" + x);
                }
                StdOut.println();
            } else {
                StdOut.printf("%d to %d (-):  not connected\n", source, vertex);
            }

        }
    }


}
