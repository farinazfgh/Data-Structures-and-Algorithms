import util.In;
import util.StdOut;

import java.util.Stack;

public class DirectedCycle {
    private final boolean[] isVisited;        // isVisited[vertex] = has vertex vertex been isVisited?
    private final int[] fromEdge;            // fromEdge[vertex] = previous vertex on path to vertex
    private final boolean[] isOnStack;       // onStack[vertex] = is vertex on the stack?
    private Stack<Integer> cycle;    // directed cycle (or null if no such cycle)

    public DirectedCycle(Digraph G) {
        isVisited = new boolean[G.getNumberofVertices()];
        isOnStack = new boolean[G.getNumberofVertices()];
        fromEdge = new int[G.getNumberofVertices()];
        for (int vertex = 0; vertex < G.getNumberofVertices(); vertex++) {
            if (!isVisited[vertex] && cycle == null) {
                dfs(G, vertex);
            }
        }
    }

    private void dfs(Digraph G, int vertex) {
        isOnStack[vertex] = true;
        isVisited[vertex] = true;
        for (int current : G.getAdjacencyList(vertex)) {
            if (cycle != null) return; // short circuit if directed cycle found do not continue
            else if (!isVisited[current]) {
                fromEdge[current] = vertex;
                dfs(G, current);
            }
            // it is already visited; is on stack still-> cycle detected
            else if (isOnStack[current]) {
                cycle = new Stack<>();
                for (int x = vertex; x != current; x = fromEdge[x]) {
                    cycle.push(x);
                }
                cycle.push(current);
                cycle.push(vertex);
                assert check();
            }
        }
        isOnStack[vertex] = false;
    }

    public boolean hasCycle() {
        return cycle != null;
    }

    public Iterable<Integer> cycle() {
        return cycle;
    }


    private boolean check() {
        if (hasCycle()) {
            // verify cycle
            int first = -1, last = -1;
            for (int vertex : cycle()) {
                if (first == -1) first = vertex;
                last = vertex;
            }
            if (first != last) {
                System.err.printf("cycle begins with %d and ends with %d\n", first, last);
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("DirectedCycle");
        In in = new In(args[0]);
        Digraph G = new Digraph(in);

        DirectedCycle finder = new DirectedCycle(G);
        if (finder.hasCycle()) {
            StdOut.print("Directed cycle: ");
            for (int vertex : finder.cycle()) {
                StdOut.print(vertex + " ");
            }
            StdOut.println();
        } else {
            StdOut.println("No directed cycle");
        }
        StdOut.println();
    }
}