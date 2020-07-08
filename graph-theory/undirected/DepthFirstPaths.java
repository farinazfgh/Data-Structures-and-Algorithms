package undirected;

import util.In;
import util.StdOut;

import java.util.Stack;


public class DepthFirstPaths {
    private boolean[] isVisited;    // isVisited[vertex] = is there an source-vertex path?
    private int[] fromEdge;        // fromEdge[vertex] = a vertex that gives us the vertex that took us here. somehow like a parent node is a tree
    private final int source;         // source vertex

    public DepthFirstPaths(UndirectedGraph G, int source) {
        this.source = source;
        //since every vertex is viited once, so there is only one node that took us to this node is that one visit
        fromEdge = new int[G.getNumberOfVertices()];
        isVisited = new boolean[G.getNumberOfVertices()];
        validateVertex(source);
        dfs(G, source);
    }

    private void dfs(UndirectedGraph G, int vertex) {
        isVisited[vertex] = true;
        for (int current : G.getAdjacencyList(vertex)) {
            if (!isVisited[current]) {
                fromEdge[current] = vertex;
                dfs(G, current);
            }
        }
    }

    public boolean hasPathTo(int vertex) {
        validateVertex(vertex);
        return isVisited[vertex];
    }

    public Iterable<Integer> pathTo(int vertex) {
        validateVertex(vertex);
        if (!hasPathTo(vertex)) return null;
        Stack<Integer> path = new Stack<Integer>();
        for (int current = vertex; current != source; current = fromEdge[current])//backtrack from current node to the source
            path.push(current);
        path.push(source);
        return path;
    }

    // throw an IllegalArgumentException unless  0 <= source < V
    private void validateVertex(int vertex) {
        int V = isVisited.length;
        if (vertex < 0 || vertex >= V)
            throw new IllegalArgumentException("vertex " + vertex + " is not between 0 and " + (V - 1));
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        UndirectedGraph G = new UndirectedGraph(in);
        int source = Integer.parseInt(args[1]);
        DepthFirstPaths dfs = new DepthFirstPaths(G, source);

        for (int vertex = 0; vertex < G.getNumberOfVertices(); vertex++) {
            if (dfs.hasPathTo(vertex)) {
                StdOut.printf("%d to %d:  ", source, vertex);
                Iterable<Integer> path = dfs.pathTo(vertex);
                for (int node : path) {
                    if (node == source) StdOut.print(node);
                    else StdOut.print("-" + node);
                }
                StdOut.println();
            } else {
                StdOut.printf("%d to %d:  not connected\n", source, vertex);
            }
        }
    }
}
