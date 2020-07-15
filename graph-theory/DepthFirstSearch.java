 /******************************************************************************
 *  Compilation:  javac undirected.DepthFirstSearch.java
 *  Execution:    java undirected.DepthFirstSearch filename.txt source
 *  Dependencies: undirected.UndirectedGraph.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/41graph/tinyG.txt
 *                https://algs4.cs.princeton.edu/41graph/mediumG.txt
 *
 *  Run depth first search on an undirected graph.
 *  Runs in O(E + V) time.
 *
 *  % java undirected.DepthFirstSearch tinyG.txt 0
 *  0 1 2 3 4 5 6 
 *  NOT connected
 *
 *  % java undirected.DepthFirstSearch tinyG.txt 9
 *  9 10 11 12 
 *  NOT connected
 *
 ******************************************************************************/

import util.In;
import util.StdOut;

/**
 * The {@code undirected.DepthFirstSearch} class represents a data type for
 * determining the vertices connected to a given source vertex <em>source</em>
 * in an undirected graph. For versions that find the paths, see
 * {@link DepthFirstPaths} and {@link BreadthFirstPaths}.
 * <p>
 * This implementation uses depth-first search.
 * See {@link DFSNonRecursive} for a non-recursive version.
 * The constructor takes &Theta;(<em>V</em> + <em>E</em>) time in the worst
 * case, where <em>V</em> is the number of vertices and <em>E</em>
 * is the number of edges.
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
public class DepthFirstSearch {
    private boolean[] isVisited;    // isVisited[vertex] = is there an source-vertex path?
    private int count;           // number of vertices connected to source

    /**
     * Computes the vertices in graph {@code G} that are
     * connected to the source vertex {@code source}.
     *
     * @param G      the graph
     * @param source the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= source < V}
     */
    public DepthFirstSearch(UndirectedGraph G, int source) {
        isVisited = new boolean[G.getNumberOfVertices()];
        validateVertex(source);
        dfs(G, source);
    }

    // depth first search from vertex
    private void dfs(UndirectedGraph G, int vertex) {
        System.out.println("visiting:" + vertex);
        count++;
        isVisited[vertex] = true;
        for (int current : G.getAdjacencyList(vertex)) {
            if (!isVisited[current]) {
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
    public boolean isVisited(int vertex) {
        validateVertex(vertex);
        return isVisited[vertex];
    }

    /**
     * Returns the number of vertices connected to the source vertex {@code source}.
     *
     * @return the number of vertices connected to the source vertex {@code source}
     */
    public int count() {
        return count;
    }

    // throw an IllegalArgumentException unless {@code 0 <= vertex < V}
    private void validateVertex(int vertex) {
        int V = isVisited.length;
        if (vertex < 0 || vertex >= V)
            throw new IllegalArgumentException("vertex " + vertex + " is not between 0 and " + (V - 1));
    }

    /**
     * Unit tests the {@code undirected.DepthFirstSearch} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        UndirectedGraph G = new UndirectedGraph(in);
        int source = Integer.parseInt(args[1]);
        DepthFirstSearch search = new DepthFirstSearch(G, source);
        for (int vertex = 0; vertex < G.getNumberOfVertices(); vertex++) {
            if (search.isVisited(vertex))
                StdOut.print(vertex + " ");
        }

        StdOut.println();
        if (search.count() != G.getNumberOfVertices()) StdOut.println("NOT connected");
        else StdOut.println("connected");
    }

}

