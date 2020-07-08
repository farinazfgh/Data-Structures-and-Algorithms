
/******************************************************************************
 *  Compilation:  javac BreadthFirstPaths.java
 *  Execution:    java BreadthFirstPaths G source
 *  Dependencies: UndirectedGraph.java Queue.java Stack.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/41graph/tinyCG.txt
 *                https://algs4.cs.princeton.edu/41graph/tinyG.txt
 *                https://algs4.cs.princeton.edu/41graph/mediumG.txt
 *                https://algs4.cs.princeton.edu/41graph/largeG.txt
 *
 *  Run breadth first search on an undirected graph.
 *  Runs in O(E + V) time.
 *
 *  %  java UndirectedGraph tinyCG.txt
 *  6 8
 *  0: 2 1 5 
 *  1: 0 2 
 *  2: 0 1 3 4 
 *  3: 5 4 2 
 *  4: 3 2 
 *  5: 3 0 
 *
 *  %  java BreadthFirstPaths tinyCG.txt 0
 *  0 to 0 (0):  0
 *  0 to 1 (1):  0-1
 *  0 to 2 (1):  0-2
 *  0 to 3 (2):  0-2-3
 *  0 to 4 (2):  0-2-4
 *  0 to 5 (1):  0-5
 *
 *  %  java BreadthFirstPaths largeG.txt 0
 *  0 to 0 (0):  0
 *  0 to 1 (418):  0-932942-474885-82707-879889-971961-...
 *  0 to 2 (323):  0-460790-53370-594358-780059-287921-...
 *  0 to 3 (168):  0-713461-75230-953125-568284-350405-...
 *  0 to 4 (144):  0-460790-53370-310931-440226-380102-...
 *  0 to 5 (566):  0-932942-474885-82707-879889-971961-...
 *  0 to 6 (349):  0-932942-474885-82707-879889-971961-...
 *
 ******************************************************************************/


import util.In;
import util.Queue;
import util.StdOut;

import java.util.Stack;

public class BreadthFirstPaths {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] isVisited;  // isVisited[vertex] = is there an source-vertex path
    private int[] fromEdge;      // fromEdge[vertex] = previous edge on shortest source-vertex path
    private int[] distanceTo;      // distanceTo[vertex] = number of edges shortest source-vertex path


    public BreadthFirstPaths(UndirectedGraph G, int source) {
        isVisited = new boolean[G.getNumberOfVertices()];
        distanceTo = new int[G.getNumberOfVertices()];
        fromEdge = new int[G.getNumberOfVertices()];
        validateVertex(source);
        bfs(G, source);

        assert check(G, source);
    }

    /**
     * Computes the shortest path between any one of the source vertices in {@code sources}
     * and every other vertex in graph {@code G}.
     *
     * @param G       the graph
     * @param sources the source vertices
     * @throws IllegalArgumentException if {@code sources} is {@code null}
     * @throws IllegalArgumentException unless {@code 0 <= source < V} for each vertex
     *                                  {@code source} in {@code sources}
     */
    public BreadthFirstPaths(UndirectedGraph G, Iterable<Integer> sources) {
        isVisited = new boolean[G.getNumberOfVertices()];
        distanceTo = new int[G.getNumberOfVertices()];
        fromEdge = new int[G.getNumberOfVertices()];
        for (int vertex = 0; vertex < G.getNumberOfVertices(); vertex++)
            distanceTo[vertex] = INFINITY;
        validateVertices(sources);
        bfs(G, sources);
    }

    private void bfs(UndirectedGraph G, int source) {
        Queue<Integer> queue = new Queue<Integer>();
        for (int vertex = 0; vertex < G.getNumberOfVertices(); vertex++)
            distanceTo[vertex] = INFINITY;
        distanceTo[source] = 0;
        isVisited[source] = true;
        queue.enqueue(source);

        while (!queue.isEmpty()) {
            int vertex = queue.dequeue();
            for (int current : G.getAdjacencyList(vertex)) {
                if (!isVisited[current]) {
                    fromEdge[current] = vertex;
                    distanceTo[current] = distanceTo[vertex] + 1;
                    isVisited[current] = true;
                    queue.enqueue(current);
                }
            }
        }
    }

    // breadth-first search from multiple sources
    private void bfs(UndirectedGraph G, Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<Integer>();
        for (int source : sources) {
            isVisited[source] = true;
            distanceTo[source] = 0;
            q.enqueue(source);
        }
        while (!q.isEmpty()) {
            int vertex = q.dequeue();
            for (int current : G.getAdjacencyList(vertex)) {
                if (!isVisited[current]) {
                    fromEdge[current] = vertex;
                    distanceTo[current] = distanceTo[vertex] + 1;
                    isVisited[current] = true;
                    q.enqueue(current);
                }
            }
        }
    }

    /**
     * Is there a path between the source vertex {@code source} (or sources) and vertex {@code vertex}?
     *
     * @param vertex the vertex
     * @return {@code true} if there is a path, and {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public boolean hasPathTo(int vertex) {
        validateVertex(vertex);
        return isVisited[vertex];
    }

    /**
     * Returns the number of edges in a shortest path between the source vertex {@code source}
     * (or sources) and vertex {@code vertex}?
     *
     * @param vertex the vertex
     * @return the number of edges in a shortest path
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public int distTo(int vertex) {
        validateVertex(vertex);
        return distanceTo[vertex];
    }

    /**
     * Returns a shortest path between the source vertex {@code source} (or sources)
     * and {@code vertex}, or {@code null} if no such path.
     *
     * @param vertex the vertex
     * @return the sequence of vertices on a shortest path, as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public Iterable<Integer> pathTo(int vertex) {
        validateVertex(vertex);
        if (!hasPathTo(vertex)) return null;
        Stack<Integer> path = new Stack<Integer>();
        int x;
        for (x = vertex; distanceTo[x] != 0; x = fromEdge[x])
            path.push(x);
        path.push(x);
        return path;
    }


    // check optimality conditions for single source
    private boolean check(UndirectedGraph G, int source) {

        // check that the distance of source = 0
        if (distanceTo[source] != 0) {
            StdOut.println("distance of source " + source + " to itself = " + distanceTo[source]);
            return false;
        }

        // check that for each edge vertex-current dist[current] <= dist[vertex] + 1
        // provided vertex is reachable from source
        for (int vertex = 0; vertex < G.getNumberOfVertices(); vertex++) {
            for (int current : G.getAdjacencyList(vertex)) {
                if (hasPathTo(vertex) != hasPathTo(current)) {
                    StdOut.println("edge " + vertex + "-" + current);
                    StdOut.println("hasPathTo(" + vertex + ") = " + hasPathTo(vertex));
                    StdOut.println("hasPathTo(" + current + ") = " + hasPathTo(current));
                    return false;
                }
                if (hasPathTo(vertex) && (distanceTo[current] > distanceTo[vertex] + 1)) {
                    StdOut.println("edge " + vertex + "-" + current);
                    StdOut.println("distTo[" + vertex + "] = " + distanceTo[vertex]);
                    StdOut.println("distTo[" + current + "] = " + distanceTo[current]);
                    return false;
                }
            }
        }

        // check that vertex = fromEdge[current] satisfies distTo[current] = distTo[vertex] + 1
        // provided vertex is reachable from source
        for (int current = 0; current < G.getNumberOfVertices(); current++) {
            if (!hasPathTo(current) || current == source) continue;
            int vertex = fromEdge[current];
            if (distanceTo[current] != distanceTo[vertex] + 1) {
                StdOut.println("shortest path edge " + vertex + "-" + current);
                StdOut.println("distTo[" + vertex + "] = " + distanceTo[vertex]);
                StdOut.println("distTo[" + current + "] = " + distanceTo[current]);
                return false;
            }
        }

        return true;
    }

    // throw an IllegalArgumentException unless {@code 0 <= vertex < V}
    private void validateVertex(int vertex) {
        int V = isVisited.length;
        if (vertex < 0 || vertex >= V)
            throw new IllegalArgumentException("vertex " + vertex + " is not between 0 and " + (V - 1));
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
     * Unit tests the {@code BreadthFirstPaths} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        UndirectedGraph G = new UndirectedGraph(in);
        // StdOut.println(G);

        int source = Integer.parseInt(args[1]);
        BreadthFirstPaths bfs = new BreadthFirstPaths(G, source);

        for (int vertex = 0; vertex < G.getNumberOfVertices(); vertex++) {
            if (bfs.hasPathTo(vertex)) {
                StdOut.printf("%d to %d (%d):  ", source, vertex, bfs.distTo(vertex));
                for (int x : bfs.pathTo(vertex)) {
                    if (x == source) StdOut.print(x);
                    else StdOut.print("-" + x);
                }
                StdOut.println();
            } else {
                StdOut.printf("%d to %d (-):  not connected\n", source, vertex);
            }

        }
    }


}
