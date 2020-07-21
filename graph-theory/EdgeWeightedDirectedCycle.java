import util.StdOut;
import util.StdRandom;

import java.util.Stack;

/**
 * The {EdgeWeightedDirectedCycle} class represents a data type for determining whether an edge-weighted digraph has a directed cycle.
 * The hasCycle operation determines whether the edge-weighted digraph has a directed cycle and, if so, the cycle operation returns one.
 * This implementation uses depth-first search.
 * The constructor takes O(V + E) time in the worst case, where V is the number of vertices and E is the number of edges.
 * Each instance method takes O(1) time.
 * It uses O(V) extra space (not including the edge-weighted digraph).
 */
public class EdgeWeightedDirectedCycle {
    private boolean[] isVisited;             // isVisited[vertex] = has vertex vertex been isVisited?
    private DirectedEdge[] fromEdge;        // fromEdge[vertex] = previous edge on path to vertex
    private boolean[] isOnStack;            // onStack[vertex] = is vertex on the stack?
    private Stack<DirectedEdge> cycle;    // directed cycle (or null if no such cycle)

    public EdgeWeightedDirectedCycle(EdgeWeightedDigraph G) {
        isVisited = new boolean[G.getNumberofVertices()];
        isOnStack = new boolean[G.getNumberofVertices()];
        fromEdge = new DirectedEdge[G.getNumberofVertices()];
        for (int vertex = 0; vertex < G.getNumberofVertices(); vertex++)
            if (!isVisited[vertex]) dfs(G, vertex);

        // check that digraph has a cycle
        assert check();
    }

    // check that algorithm computes either the topological order or finds a directed cycle
    private void dfs(EdgeWeightedDigraph G, int vertex) {
        isOnStack[vertex] = true;
        isVisited[vertex] = true;
        for (DirectedEdge edge : G.getAdjacencyEdgesList(vertex)) {
            int current = edge.to();

            // short circuit if directed cycle found
            if (cycle != null) return;

                // found new vertex, so recur
            else if (!isVisited[current]) {
                fromEdge[current] = edge;
                dfs(G, current);
            }

            // trace back directed cycle
            else if (isOnStack[current]) {
                cycle = new Stack<>();

                DirectedEdge f = edge;
                while (f.from() != current) {
                    cycle.push(f);
                    f = fromEdge[f.from()];
                }
                cycle.push(f);

                return;
            }
        }

        isOnStack[vertex] = false;
    }

    /**
     * Does the edge-weighted digraph have a directed cycle?
     *
     * @return {true} if the edge-weighted digraph has a directed cycle,
     * {false} otherwise
     */
    public boolean hasCycle() {
        return cycle != null;
    }

    /**
     * Returns a directed cycle if the edge-weighted digraph has a directed cycle,
     * and {null} otherwise.
     *
     * @return a directed cycle (as an iterable) if the edge-weighted digraph
     * has a directed cycle, and {null} otherwise
     */
    public Iterable<DirectedEdge> cycle() {
        return cycle;
    }


    // certify that digraph is either acyclic or has a directed cycle
    private boolean check() {

        // edge-weighted digraph is cyclic
        if (hasCycle()) {
            // verify cycle
            DirectedEdge first = null, last = null;
            for (DirectedEdge e : cycle()) {
                if (first == null) first = e;
                if (last != null) {
                    if (last.to() != e.from()) {
                        System.err.printf("cycle edges %source and %source not incident\n", last, e);
                        return false;
                    }
                }
                last = e;
            }

            if (last.to() != first.from()) {
                System.err.printf("cycle edges %source and %source not incident\n", last, first);
                return false;
            }
        }


        return true;
    }

    /**
     * Unit tests the {EdgeWeightedDirectedCycle} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        // create random DAG with getNumberofVertices vertices and E edges; then add F random edges
        int getNumberofVertices = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        int F = Integer.parseInt(args[2]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(getNumberofVertices);
        int[] vertices = new int[getNumberofVertices];
        for (int i = 0; i < getNumberofVertices; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);
        for (int i = 0; i < E; i++) {
            int vertex, current;
            do {
                vertex = StdRandom.uniform(getNumberofVertices);
                current = StdRandom.uniform(getNumberofVertices);
            } while (vertex >= current);
            double weight = StdRandom.uniform();
            G.addEdge(new DirectedEdge(vertex, current, weight));
        }

        // add F extra edges
        for (int i = 0; i < F; i++) {
            int vertex = StdRandom.uniform(getNumberofVertices);
            int current = StdRandom.uniform(getNumberofVertices);
            double weight = StdRandom.uniform(0.0, 1.0);
            G.addEdge(new DirectedEdge(vertex, current, weight));
        }

        StdOut.println(G);

        // find a directed cycle
        EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(G);
        if (finder.hasCycle()) {
            StdOut.print("undirected.Cycle: ");
            for (DirectedEdge e : finder.cycle()) {
                StdOut.print(e + " ");
            }
            StdOut.println();
        }

        // or give topologial sort
        else {
            StdOut.println("No directed cycle");
        }
    }

}

