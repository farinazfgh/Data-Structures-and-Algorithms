import util.In;
import util.StdOut;

import java.util.Iterator;
import java.util.Stack;


public class DFSNonRecursive {
    private boolean[] isVisited;  // isVisited[vertex] = is there an source-vertex path?

    public DFSNonRecursive(UndirectedGraph G, int source) {
        isVisited = new boolean[G.getNumberOfVertices()];

        validateVertex(source);

        Iterator<Integer>[] adjacencyList = (Iterator<Integer>[]) new Iterator[G.getNumberOfVertices()];
        for (int vertex = 0; vertex < G.getNumberOfVertices(); vertex++)
            adjacencyList[vertex] = G.getAdjacencyList(vertex).iterator();
        //since it is not recursive we don't use program stack, therefore we need to use explicit stack
        Stack<Integer> stack = new Stack<Integer>();
        isVisited[source] = true;
        stack.push(source);
        while (!stack.isEmpty()) {
            int vertex = stack.peek();
            if (adjacencyList[vertex].hasNext()) {
                int current = adjacencyList[vertex].next();
                if (!isVisited[current]) {
                    isVisited[current] = true;
                    stack.push(current);
                }
            } else {
                stack.pop();
            }
        }
    }

    /**
     * Is vertex {@code vertex} connected to the source vertex {@code source}?
     *
     * @param vertex the vertex
     * @return {@code true} if vertex {@code vertex} is connected to the source vertex {@code source},
     * and {@code false} otherwise
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
            throw new IllegalArgumentException("vertex " + vertex + " is not between 0 and " + (V - 1));
    }

    /**
     * Unit tests the {@code DFSNonRecursive} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        UndirectedGraph G = new UndirectedGraph(in);
        int source = Integer.parseInt(args[1]);
        DFSNonRecursive dfs = new DFSNonRecursive(G, source);
        for (int vertex = 0; vertex < G.getNumberOfVertices(); vertex++)
            if (dfs.isVisited(vertex))
                StdOut.print(vertex + " ");
        StdOut.println();
    }
}
