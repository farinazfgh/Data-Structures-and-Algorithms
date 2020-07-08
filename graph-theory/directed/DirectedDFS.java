package directed; /******************************************************************************
 *  Compilation:  javac directed.DirectedDFS.java
 *  Execution:    java directed.DirectedDFS digraph.txt source
 *  Dependencies: directed.Digraph.java util.Bag.java In.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/mediumDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/largeDG.txt
 *
 *  Determine single-source or multiple-source reachability in a digraph
 *  using depth first search.
 *  Runs in O(E + V) time.
 *
 *  % java directed.DirectedDFS tinyDG.txt 1
 *  1
 *
 *  % java directed.DirectedDFS tinyDG.txt 2
 *  0 1 2 3 4 5
 *
 *  % java directed.DirectedDFS tinyDG.txt 1 2 6
 *  0 1 2 3 4 5 6 8 9 10 11 12 
 *
 ******************************************************************************/

import util.Bag;
import util.In;
import util.StdOut;

/**
 *  The {@code directed.DirectedDFS} class represents a data type for
 *  determining the vertices reachable from a given source vertex <em>source</em>
 *  (or set of source vertices) in a digraph. For versions that find the paths,
 *  see {@link DepthFirstDirectedPaths} and {@link BreadthFirstDirectedPaths}.
 *  <p>
 *  This implementation uses depth-first search.
 *  The constructor takes time proportional to <em>V</em> + <em>E</em>
 *  (in the worst case),
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  Each instance method takes &Theta;(1) time.
 *  It uses &Theta;(<em>V</em>) extra space (not including the digraph).
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class DirectedDFS {
    private boolean[] isVisited;  // isVisited[vertex] = true iff vertex is reachable from source(source)
    private int count;         // number of vertices reachable from source(source)

    /**
     * Computes the vertices in digraph {@code G} that are
     * reachable from the source vertex {@code source}.
     * @param G the digraph
     * @param source the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= source < V}
     */
    public DirectedDFS(Digraph G, int source) {
        isVisited = new boolean[G.getNumberofVertices()];
        validateVertex(source);
        dfs(G, source);
    }

    /**
     * Computes the vertices in digraph {@code G} that are
     * connected to any of the source vertices {@code sources}.
     * @param G the graph
     * @param sources the source vertices
     * @throws IllegalArgumentException if {@code sources} is {@code null}
     * @throws IllegalArgumentException unless {@code 0 <= source < V}
     *         for each vertex {@code source} in {@code sources}
     */
    public DirectedDFS(Digraph G, Iterable<Integer> sources) {
        isVisited = new boolean[G.getNumberofVertices()];
        validateVertices(sources);
        for (int vertex : sources) {
            if (!isVisited[vertex]) dfs(G, vertex);
        }
    }

    private void dfs(Digraph G, int vertex) {
        count++;
        isVisited[vertex] = true;
        for (int current : G.getAdjacencyList(vertex)) {
            if (!isVisited[current]) dfs(G, current);
        }
    }

    /**
     * Is there a directed path from the source vertex (or any
     * of the source vertices) and vertex {@code vertex}?
     * @param  vertex the vertex
     * @return {@code true} if there is a directed path, {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public boolean isVisited(int vertex) {
        validateVertex(vertex);
        return isVisited[vertex];
    }

    /**
     * Returns the number of vertices reachable from the source vertex
     * (or source vertices).
     * @return the number of vertices reachable from the source vertex
     *   (or source vertices)
     */
    public int count() {
        return count;
    }

    // throw an IllegalArgumentException unless {@code 0 <= vertex < V}
    private void validateVertex(int vertex) {
        int V = isVisited.length;
        if (vertex < 0 || vertex >= V)
            throw new IllegalArgumentException("vertex " + vertex + " is not between 0 and " + (V-1));
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
     * Unit tests the {@code directed.DirectedDFS} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        // read in digraph from command-line argument
        In in = new In(args[0]);
        Digraph G = new Digraph(in);

        // read in sources from command-line arguments
        Bag<Integer> sources = new Bag<Integer>();
        for (int i = 1; i < args.length; i++) {
            int source = Integer.parseInt(args[i]);
            sources.add(source);
        }

        // multiple-source reachability
        DirectedDFS dfs = new DirectedDFS(G, sources);

        // print out vertices reachable from sources
        for (int vertex = 0; vertex < G.getNumberofVertices(); vertex++) {
            if (dfs.isVisited(vertex)) StdOut.print(vertex + " ");
        }
        StdOut.println();
    }

}
