package directed; /******************************************************************************
 *  Compilation:  javac directed.TopologicalX.java
 *  Execution:    java directed.TopologicalX getNumberofVertices E F
 *  Dependencies: Queue.java directed.Digraph.java
 *
 *  Compute topological ordering of a DAG using queue-based algorithm.
 *  Runs in O(E + getNumberofVertices) time.
 *
 ******************************************************************************/

import mst.EdgeWeightedDigraph;
import util.Queue;
import util.StdOut;
import util.StdRandom;

/**
 * The {@code directed.TopologicalX} class represents a data type for
 * determining a topological order of a <em>directed acyclic graph</em> (DAG).
 * A digraph has a topological order if and only if it is a DAG.
 * The <em>hasOrder</em> operation determines whether the digraph has
 * a topological order, and if so, the <em>order</em> operation
 * returns one.
 * <p>
 * This implementation uses a nonrecursive, queue-based algorithm.
 * The constructor takes &Theta;(<em>getNumberofVertices</em> + <em>E</em>) time in the worst
 * case, where <em>getNumberofVertices</em> is the number of vertices and <em>E</em>
 * is the number of edges.
 * Each instance method takes &Theta;(1) time.
 * It uses &Theta;(<em>getNumberofVertices</em>) extra space (not including the digraph).
 * <p>
 * See {@link DirectedCycle}, {@link DirectedCycleNonRecursive}, and
 * {@link EdgeWeightedDirectedCycle} to compute a
 * directed cycle if the digraph is not a DAG.
 * See {@link Topological} for a recursive version that uses depth-first search.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class TopologicalX {
    private Queue<Integer> order;     // vertices in topological order
    private int[] ranks;              // ranks[v] = order where vertex v appers in order

    /**
     * Determines whether the digraph {@code G} has a topological order and, if so,
     * finds such a topological order.
     *
     * @param G the digraph
     */
    public TopologicalX(Digraph G) {

        // indegrees of remaining vertices
        int[] indegree = new int[G.getNumberofVertices()];
        for (int v = 0; v < G.getNumberofVertices(); v++) {
            indegree[v] = G.indegree(v);
        }

        // initialize 
        ranks = new int[G.getNumberofVertices()];
        order = new Queue<Integer>();
        int count = 0;

        // initialize queue to contain all vertices with indegree = 0
        Queue<Integer> queue = new Queue<Integer>();
        for (int v = 0; v < G.getNumberofVertices(); v++)
            if (indegree[v] == 0) queue.enqueue(v);

        while (!queue.isEmpty()) {
            int v = queue.dequeue();
            order.enqueue(v);
            ranks[v] = count++;
            for (int w : G.getAdjacencyList(v)) {
                indegree[w]--;
                if (indegree[w] == 0) queue.enqueue(w);
            }
        }

        // there is a directed cycle in subgraph of vertices with indegree >= 1.
        if (count != G.getNumberofVertices()) {
            order = null;
        }

        assert check(G);
    }

    /**
     * Determines whether the edge-weighted digraph {@code G} has a
     * topological order and, if so, finds such a topological order.
     *
     * @param G the digraph
     */
    public TopologicalX(EdgeWeightedDigraph G) {

        // indegrees of remaining vertices
        int[] indegree = new int[G.getNumberofVertices()];
        for (int v = 0; v < G.getNumberofVertices(); v++) {
            indegree[v] = G.indegree(v);
        }

        // initialize 
        ranks = new int[G.getNumberofVertices()];
        order = new Queue<Integer>();
        int count = 0;

        // initialize queue to contain all vertices with indegree = 0
        Queue<Integer> queue = new Queue<Integer>();
        for (int v = 0; v < G.getNumberofVertices(); v++)
            if (indegree[v] == 0) queue.enqueue(v);

        while (!queue.isEmpty()) {
            int v = queue.dequeue();
            order.enqueue(v);
            ranks[v] = count++;
            for (DirectedEdge e : G.getAdjacencyList(v)) {
                int w = e.to();
                indegree[w]--;
                if (indegree[w] == 0) queue.enqueue(w);
            }
        }

        // there is a directed cycle in subgraph of vertices with indegree >= 1.
        if (count != G.getNumberofVertices()) {
            order = null;
        }

        assert check(G);
    }

    /**
     * Returns a topological order if the digraph has a topologial order,
     * and {@code null} otherwise.
     *
     * @return a topological order of the vertices (as an interable) if the
     * digraph has a topological order (or equivalently, if the digraph is a DAG),
     * and {@code null} otherwise
     */
    public Iterable<Integer> order() {
        return order;
    }

    /**
     * Does the digraph have a topological order?
     *
     * @return {@code true} if the digraph has a topological order (or equivalently,
     * if the digraph is a DAG), and {@code false} otherwise
     */
    public boolean hasOrder() {
        return order != null;
    }

    /**
     * The the rank of vertex {@code v} in the topological order;
     * -1 if the digraph is not a DAG
     *
     * @param v vertex
     * @return the position of vertex {@code v} in a topological order
     * of the digraph; -1 if the digraph is not a DAG
     * @throws IllegalArgumentException unless {@code 0 <= v < getNumberofVertices}
     */
    public int rank(int v) {
        validateVertex(v);
        if (hasOrder()) return ranks[v];
        else return -1;
    }

    // certify that digraph is acyclic
    private boolean check(Digraph G) {

        // digraph is acyclic
        if (hasOrder()) {
            // check that ranks are a permutation of 0 to getNumberofVertices-1
            boolean[] found = new boolean[G.getNumberofVertices()];
            for (int i = 0; i < G.getNumberofVertices(); i++) {
                found[rank(i)] = true;
            }
            for (int i = 0; i < G.getNumberofVertices(); i++) {
                if (!found[i]) {
                    System.err.println("No vertex with rank " + i);
                    return false;
                }
            }

            // check that ranks provide a valid topological order
            for (int v = 0; v < G.getNumberofVertices(); v++) {
                for (int w : G.getAdjacencyList(v)) {
                    if (rank(v) > rank(w)) {
                        System.err.printf("%d-%d: rank(%d) = %d, rank(%d) = %d\n",
                                v, w, v, rank(v), w, rank(w));
                        return false;
                    }
                }
            }

            // check that order() is consistent with rank()
            int r = 0;
            for (int v : order()) {
                if (rank(v) != r) {
                    System.err.println("order() and rank() inconsistent");
                    return false;
                }
                r++;
            }
        }


        return true;
    }

    // certify that digraph is acyclic
    private boolean check(EdgeWeightedDigraph G) {

        // digraph is acyclic
        if (hasOrder()) {
            // check that ranks are a permutation of 0 to getNumberofVertices-1
            boolean[] found = new boolean[G.getNumberofVertices()];
            for (int i = 0; i < G.getNumberofVertices(); i++) {
                found[rank(i)] = true;
            }
            for (int i = 0; i < G.getNumberofVertices(); i++) {
                if (!found[i]) {
                    System.err.println("No vertex with rank " + i);
                    return false;
                }
            }

            // check that ranks provide a valid topological order
            for (int v = 0; v < G.getNumberofVertices(); v++) {
                for (DirectedEdge e : G.getAdjacencyList(v)) {
                    int w = e.to();
                    if (rank(v) > rank(w)) {
                        System.err.printf("%d-%d: rank(%d) = %d, rank(%d) = %d\n",
                                v, w, v, rank(v), w, rank(w));
                        return false;
                    }
                }
            }

            // check that order() is consistent with rank()
            int r = 0;
            for (int v : order()) {
                if (rank(v) != r) {
                    System.err.println("order() and rank() inconsistent");
                    return false;
                }
                r++;
            }
        }


        return true;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < getNumberofVertices}
    private void validateVertex(int v) {
        int getNumberofVertices = ranks.length;
        if (v < 0 || v >= getNumberofVertices)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (getNumberofVertices - 1));
    }

    /**
     * Unit tests the {@code directed.TopologicalX} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        // create random DAG with getNumberofVertices vertices and E edges; then add F random edges
        int getNumberofVertices = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        int F = Integer.parseInt(args[2]);

        Digraph G1 = DigraphGenerator.dag(getNumberofVertices, E);

        // corresponding edge-weighted digraph
        EdgeWeightedDigraph G2 = new EdgeWeightedDigraph(getNumberofVertices);
        for (int v = 0; v < G1.getNumberofVertices(); v++)
            for (int w : G1.getAdjacencyList(v))
                G2.addEdge(new DirectedEdge(v, w, 0.0));

        // add F extra edges
        for (int i = 0; i < F; i++) {
            int v = StdRandom.uniform(getNumberofVertices);
            int w = StdRandom.uniform(getNumberofVertices);
            G1.addEdge(v, w);
            G2.addEdge(new DirectedEdge(v, w, 0.0));
        }

        StdOut.println(G1);
        StdOut.println();
        StdOut.println(G2);

        // find a directed cycle
        TopologicalX topological1 = new TopologicalX(G1);
        if (!topological1.hasOrder()) {
            StdOut.println("Not a DAG");
        }

        // or give topologial sort
        else {
            StdOut.print("directed.Topological order: ");
            for (int v : topological1.order()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        }

        // find a directed cycle
        TopologicalX topological2 = new TopologicalX(G2);
        if (!topological2.hasOrder()) {
            StdOut.println("Not a DAG");
        }

        // or give topologial sort
        else {
            StdOut.print("directed.Topological order: ");
            for (int v : topological2.order()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        }
    }
}