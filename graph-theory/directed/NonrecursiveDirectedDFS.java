package directed; /******************************************************************************
 *  Compilation:  javac directed.NonrecursiveDirectedDFS.java
 *  Execution:    java directed.NonrecursiveDirectedDFS digraph.txt source
 *  Dependencies: directed.Digraph.java Queue.java Stack.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/mediumDG.txt
 *                https://algs4.cs.princeton.edu/42digraph/largeDG.txt
 *
 *  Run nonrecurisve depth-first search on an directed graph.
 *  Runs in O(E + V) time.
 *
 *  Explores the vertices in exactly the same order as directed.DirectedDFS.java.
 *
 *
 *  % java directed.NonrecursiveDirectedDFS tinyDG.txt 1
 *  1
 *
 *  % java directed.NonrecursiveDirectedDFS tinyDG.txt 2
 *  0 1 2 3 4 5
 *
 ******************************************************************************/

import util.In;
import util.StdOut;

import java.util.Iterator;
import java.util.Stack;

/**
 *  The {@code directed.NonrecursiveDirectedDFS} class represents a data type for finding
 *  the vertices reachable from a source vertex <em>source</em> in the digraph.
 *  <p>
 *  This implementation uses a nonrecursive version of depth-first search
 *  with an explicit stack.
 *  The constructor takes &Theta;(<em>V</em> + <em>E</em>) time in the
 *  worst case, where <em>V</em> is the number of vertices and <em>E</em>
 *  is the number of edges.
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
public class NonrecursiveDirectedDFS {
    private boolean[] isVisited;  // isVisited[vertex] = is there an source->vertex path?
    /**
     * Computes the vertices reachable from the source vertex {@code source} in the digraph {@code G}.
     * @param  G the digraph
     * @param  source the source vertex
     * @throws IllegalArgumentException unless {@code 0 <= source < V}
     */
    public NonrecursiveDirectedDFS(Digraph G, int source) {
        isVisited = new boolean[G.getNumberofVertices()];
        validateVertex(source);

        // to be able to iterate over each adjacency list, keeping track of which
        // vertex in each adjacency list needs to be explored next
        Iterator<Integer>[] adj = (Iterator<Integer>[]) new Iterator[G.getNumberofVertices()];
        for (int vertex = 0; vertex < G.getNumberofVertices(); vertex++)
            adj[vertex] = G.getAdjacencyList(vertex).iterator();

        // depth-first search using an explicit stack
        Stack<Integer> stack = new Stack<Integer>();
        isVisited[source] = true;
        stack.push(source);
        while (!stack.isEmpty()) {
            int vertex = stack.peek();
            if (adj[vertex].hasNext()) {
                int current = adj[vertex].next();
                // StdOut.printf("check %d\n", current);
                if (!isVisited[current]) {
                    // discovered vertex current for the first time
                    isVisited[current] = true;
                    // fromEdge[current] = vertex;
                    stack.push(current);
                    // StdOut.printf("dfs(%d)\n", current);
                }
            }
            else {
                // StdOut.printf("%d done\n", vertex);
                stack.pop();
            }
        }
    }

    /**
     * Is vertex {@code vertex} reachable from the source vertex {@code source}?
     * @param  vertex the vertex
     * @return {@code true} if vertex {@code vertex} is reachable from the source vertex {@code source},
     *         and {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public boolean isVisited(int vertex) {
        validateVertex(vertex);
        return isVisited[vertex];
    }

    // throw an IllegalArgumentException unless {@code 0 <= vertex < V}
    private void validateVertex(int vertex) {
        int V = isVisited.length;
        if (vertex < 0 || vertex >= V)
            throw new IllegalArgumentException("vertex " + vertex + " is not between 0 and " + (V-1));
    }

    /**
     * Unit tests the {@code directed.NonrecursiveDirectedDFS} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        int source = Integer.parseInt(args[1]);
        NonrecursiveDirectedDFS dfs = new NonrecursiveDirectedDFS(G, source);
        for (int vertex = 0; vertex < G.getNumberofVertices(); vertex++)
            if (dfs.isVisited(vertex))
                StdOut.print(vertex + " ");
        StdOut.println();
    }

}
