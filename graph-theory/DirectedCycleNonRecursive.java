 /******************************************************************************
 *  Compilation:  javac directed.DirectedCycleNonRecursive.java
 *  Execution:    java directed.DirectedCycleNonRecursive V E F
 *  Dependencies: Queue.java directed.Digraph.java Stack.java
 *
 *  Find a directed cycle in a digraph, using a nonrecursive, queue-based
 *  algorithm. Runs in O(E + V) time.
 *
 ******************************************************************************/

import util.Queue;
import util.StdOut;
import util.StdRandom;

import java.util.Stack;

/**
 * This implementation uses a nonrecursive, queue-based algorithm.
 * The constructor takes time proportional to <em>V</em> + <em>E</em>
 * (in the worst case),
 * where <em>V</em> is the number of vertices and <em>E</em> is the
 * number of edges.
 * Each instance method takes &Theta;(1) time.
 * It uses &Theta;(<em>V</em>) extra space (not including the digraph).
 */

public class DirectedCycleNonRecursive {
    private Stack<Integer> cycle;

    public DirectedCycleNonRecursive(Digraph G) {

        // indegrees of remaining vertices
        int[] indegree = new int[G.getNumberofVertices()];
        for (int vertex = 0; vertex < G.getNumberofVertices(); vertex++) {
            indegree[vertex] = G.indegree(vertex);
        }
        Queue<Integer> queue = enqueueVerticesWithZeroIndegree(G, indegree);

        while (!queue.isEmpty()) {
            int v = queue.dequeue();
            for (int current : G.getAdjacencyList(v)) {
                indegree[current]--;
                if (indegree[current] == 0) queue.enqueue(current);//as soon as in degree of a vertex is zero add it to queue
            }
        }

        // there is a directed cycle in sub-graph of vertices with indegree >= 1.
        int[] fromEdge = new int[G.getNumberofVertices()];
        int root = -1;  // any vertex with indegree >= -1
        for (int vertex = 0; vertex < G.getNumberofVertices(); vertex++) {
            if (indegree[vertex] == 0) continue;
            else root = vertex;
            for (int current : G.getAdjacencyList(vertex)) {
                if (indegree[current] > 0) {
                    fromEdge[current] = vertex;
                }
            }
        }

        if (root != -1) {

            // find any vertex on cycle
            boolean[] visited = new boolean[G.getNumberofVertices()];
            while (!visited[root]) {
                visited[root] = true;
                root = fromEdge[root];
            }

            // extract cycle
            cycle = new Stack<Integer>();
            int v = root;
            do {
                cycle.push(v);
                v = fromEdge[v];
            } while (v != root);
            cycle.push(root);
        }

        assert check();
    }

    private Queue<Integer> enqueueVerticesWithZeroIndegree(Digraph G, int[] indegree) {
        // initialize queue to contain all vertices with indegree = 0
        Queue<Integer> queue = new Queue<>();
        for (int v = 0; v < G.getNumberofVertices(); v++)
            if (indegree[v] == 0) queue.enqueue(v);
        return queue;
    }

    /**
     * Returns a directed cycle if the digraph has a directed cycle, and {@code null} otherwise.
     *
     * @return a directed cycle (as an iterable) if the digraph has a directed cycle,
     * and {@code null} otherwise
     */
    public Iterable<Integer> cycle() {
        return cycle;
    }

    /**
     * Does the digraph have a directed cycle?
     *
     * @return {@code true} if the digraph has a directed cycle, {@code false} otherwise
     */
    public boolean hasCycle() {
        return cycle != null;
    }

    // certify that digraph has a directed cycle if it reports one
    private boolean check() {

        if (hasCycle()) {
            // verify cycle
            int first = -1, last = -1;
            for (int v : cycle()) {
                if (first == -1) first = v;
                last = v;
            }
            if (first != last) {
                System.err.printf("cycle begins with %d and ends with %d\n", first, last);
                return false;
            }
        }


        return true;
    }


    public static void main(String[] args) {

        // create random DAG with getNumberofVertices vertices and E edges; then add F random edges
        int getNumberofVertices = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        int F = Integer.parseInt(args[2]);
        Digraph G = DigraphGenerator.dag(getNumberofVertices, E);

        // add F extra edges
        for (int i = 0; i < F; i++) {
            int v = StdRandom.uniform(getNumberofVertices);
            int w = StdRandom.uniform(getNumberofVertices);
            G.addEdge(v, w);
        }

        StdOut.println(G);


        DirectedCycleNonRecursive finder = new DirectedCycleNonRecursive(G);
        if (finder.hasCycle()) {
            StdOut.print("Directed cycle: ");
            for (int v : finder.cycle()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        } else {
            StdOut.println("No directed cycle");
        }
        StdOut.println();
    }

}
