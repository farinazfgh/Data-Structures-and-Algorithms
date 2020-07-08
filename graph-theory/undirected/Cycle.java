package undirected;

import util.In;
import util.StdOut;

import java.util.Stack;

/******************************************************************************
 *  Identifies a cycle.
 *  Runs in O(E + V) time.
 ******************************************************************************/


public class Cycle {
    private boolean[] isVisited;
    private int[] fromEdge;
    private Stack<Integer> cycleStack;

    public Cycle(UndirectedGraph G) {
        if (hasSelfLoop(G)) return;
        if (hasParallelEdges(G)) return;
        isVisited = new boolean[G.getNumberOfVertices()];
        fromEdge = new int[G.getNumberOfVertices()];
        for (int vertex = 0; vertex < G.getNumberOfVertices(); vertex++)
            if (!isVisited[vertex])
                dfs(G, -1, vertex);
    }

    /**
     * Unit tests the {@code undirected.Cycle} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        UndirectedGraph G = new UndirectedGraph(in);
        Cycle finder = new Cycle(G);
        if (finder.hasCycle()) {
            for (int vertex : finder.cycle()) {
                StdOut.print(vertex + " ");
            }
            StdOut.println();
        } else {
            StdOut.println("undirected.UndirectedGraph is acyclic");
        }
    }

    // side effect: initialize cycle to be self loop
    private boolean hasSelfLoop(UndirectedGraph G) {
        for (int vertex = 0; vertex < G.getNumberOfVertices(); vertex++) {
            for (int current : G.getAdjacencyList(vertex)) {
                if (vertex == current) {
                    cycleStack = new Stack<>();
                    cycleStack.push(vertex);
                    cycleStack.push(vertex);
                    return true;
                }
            }
        }
        return false;
    }

    // side effect: initialize cycle to be two parallel edges
    private boolean hasParallelEdges(UndirectedGraph G) {
        isVisited = new boolean[G.getNumberOfVertices()];

        for (int vertex = 0; vertex < G.getNumberOfVertices(); vertex++) {
            for (int current : G.getAdjacencyList(vertex)) {
                if (isVisited[current]) {
                    cycleStack = new Stack<>();
                    cycleStack.push(vertex);
                    cycleStack.push(current);
                    cycleStack.push(vertex);
                    return true;
                }
                isVisited[current] = true;
            }

            // reset so isVisited[vertex] = false for all vertex
            for (int current : G.getAdjacencyList(vertex)) {
                isVisited[current] = false;
            }
        }
        return false;
    }

    /**
     * Returns true if the graph {@code G} has a cycle.
     *
     * @return {@code true} if the graph has a cycle; {@code false} otherwise
     */
    public boolean hasCycle() {
        return cycleStack != null;
    }

    /**
     * Returns a cycle in the graph {@code G}.
     *
     * @return a cycle if the graph {@code G} has a cycle,
     * and {@code null} otherwise
     */
    public Iterable<Integer> cycle() {
        return cycleStack;
    }

    private void dfs(UndirectedGraph G, int u, int vertex) {
        isVisited[vertex] = true;
        for (int current : G.getAdjacencyList(vertex)) {

            // short circuit if cycle already found
            if (cycleStack != null) return;

            if (!isVisited[current]) {
                fromEdge[current] = vertex;
                dfs(G, vertex, current);
            }

            // check for cycle (but disregard reverse of edge leading to vertex)
            else if (current != u) {
                cycleStack = new Stack<Integer>();
                for (int x = vertex; x != current; x = fromEdge[x]) {
                    cycleStack.push(x);
                }
                cycleStack.push(current);
                cycleStack.push(vertex);
            }
        }
    }
}



