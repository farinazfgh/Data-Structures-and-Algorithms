package undirected;

import util.In;
import util.Queue;
import util.StdOut;

public class CC {
    private boolean[] isVisited;   // isVisited[vertex] = has vertex vertex been isVisited?
    private int[] id;           // id[vertex] = id of connected component containing vertex
    private int[] size;         // size[id] = number of vertices in given component
    private int count;          // number of connected components

    public CC(UndirectedGraph G) {
        isVisited = new boolean[G.getNumberOfVertices()];
        id = new int[G.getNumberOfVertices()];
        size = new int[G.getNumberOfVertices()];
        for (int vertex = 0; vertex < G.getNumberOfVertices(); vertex++) {
            if (!isVisited[vertex]) {
                dfs(G, vertex);
                count++;
            }
        }
    }

/*    public CC(EdgeWeightedGraph G) {
        isVisited = new boolean[G.V()];
        id = new int[G.V()];
        size = new int[G.V()];
        for (int vertex = 0; vertex < G.V(); vertex++) {
            if (!isVisited[vertex]) {
                dfs(G, vertex);
                count++;
            }
        }
    }
*/
    // depth-first search for a undirected.UndirectedGraph
    private void dfs(UndirectedGraph G, int vertex) {
        isVisited[vertex] = true;
        id[vertex] = count;
        size[count]++;
        for (int current : G.getAdjacencyList(vertex)) {
            if (!isVisited[current]) {
                dfs(G, current);
            }
        }
    }




    public int id(int vertex) {
        validateVertex(vertex);
        return id[vertex];
    }

    public int size(int vertex) {
        validateVertex(vertex);
        return size[id[vertex]];
    }

    public int count() {
        return count;
    }

    public boolean connected(int vertex, int current) {
        validateVertex(vertex);
        validateVertex(current);
        return id(vertex) == id(current);
    }

    @Deprecated
    public boolean areConnected(int vertex, int current) {
        validateVertex(vertex);
        validateVertex(current);
        return id(vertex) == id(current);
    }

    // throw an IllegalArgumentException unless {@code 0 <= vertex < V}
    private void validateVertex(int vertex) {
        int V = isVisited.length;
        if (vertex < 0 || vertex >= V)
            throw new IllegalArgumentException("vertex " + vertex + " is not between 0 and " + (V-1));
    }

    public static void main(String[] args) {
        System.out.println("undirected.CC");
        In in = new In(args[0]);
        UndirectedGraph G = new UndirectedGraph(in);
        CC connectedComponent = new CC(G);

        // number of connected components
        int numberOfConnectedComponents = connectedComponent.count();
        StdOut.println(numberOfConnectedComponents + " components");

        // compute list of vertices in each connected component
        Queue<Integer>[] components = (Queue<Integer>[]) new Queue[numberOfConnectedComponents];
        for (int i = 0; i < numberOfConnectedComponents; i++) {
            components[i] = new Queue<Integer>();
        }
        for (int vertex = 0; vertex < G.getNumberOfVertices(); vertex++) {
            components[connectedComponent.id(vertex)].enqueue(vertex);
        }

        // print results
        for (int i = 0; i < numberOfConnectedComponents; i++) {
            for (int vertex : components[i]) {
                StdOut.print(vertex + " ");
            }
            StdOut.println();
        }
    }
}
