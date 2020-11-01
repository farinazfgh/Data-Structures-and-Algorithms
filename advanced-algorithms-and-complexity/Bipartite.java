import java.util.Stack;

/*
The Bipartite class represents a data type for determining whether an undirected graph is bipartite or whether it has an odd-length cycle.
A graph is bipartite if and only if it has no odd-length cycle.
The isBipartite operation determines whether the graph is bipartite.
If so, the color operation determines a bipartition;
if not, the oddCycle operation determines a cycle with an odd number of edges.
This implementation uses depth-first search.
The constructor takes Θ(V + E) time in the worst case, where V is the number of vertices and E is the number of edges.
Each instance method takes Θ(1) time. It uses Θ(V) extra space (not including the graph).
See BipartiteX for a nonrecursive version that uses breadth-first search.
 */
public class Bipartite {
    private boolean isBipartite;   // is the graph bipartite?
    private boolean[] color;       // color[v] gives vertices on one side of bipartition
    private boolean[] isVisited;      // marked[v] = true iff v has been visited in DFS
    private int[] fromEdge;          // edgeTo[v] = last edge on path to v
    private Stack<Integer> cycle;  // odd-length cycle

    /**
     * Determines whether an undirected graph is bipartite and finds either a
     * bipartition or an odd-length cycle.
     *
     * @param G the graph
     */
    public Bipartite(Graph G) {
        isBipartite = true;
        color = new boolean[G.V()];
        isVisited = new boolean[G.V()];
        fromEdge = new int[G.V()];

        for (int v = 0; v < G.V(); v++) {
            if (!isVisited[v]) {
                dfs(G, v);
            }
        }
        assert check(G);
    }

    private void dfs(Graph G, int v) {
        isVisited[v] = true;
        for (int w : G.getAdj(v)) {

            // short circuit if odd-length cycle found
            if (cycle != null) return;

            // found uncolored vertex, so recur
            if (!isVisited[w]) {
                fromEdge[w] = v;
                color[w] = !color[v];
                dfs(G, w);
            }

            // if v-w create an odd-length cycle, find it
            else if (color[w] == color[v]) {
                isBipartite = false;
                cycle = new Stack<Integer>();
                cycle.push(w);  // don't need this unless you want to include start vertex twice
                for (int x = v; x != w; x = fromEdge[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
            }
        }
    }

    /**
     * Returns true if the graph is bipartite.
     *
     * @return {@code true} if the graph is bipartite; {@code false} otherwise
     */
    public boolean isBipartite() {
        return isBipartite;
    }

    /**
     * Returns the side of the bipartite that vertex {@code v} is on.
     *
     * @param v the vertex
     * @return the side of the bipartition that vertex {@code v} is on; two vertices
     * are in the same side of the bipartition if and only if they have the
     * same color
     * @throws IllegalArgumentException      unless {@code 0 <= v < V}
     * @throws UnsupportedOperationException if this method is called when the graph
     *                                       is not bipartite
     */
    public boolean color(int v) {
        validateVertex(v);
        if (!isBipartite)
            throw new UnsupportedOperationException("graph is not bipartite");
        return color[v];
    }

    /**
     * Returns an odd-length cycle if the graph is not bipartite, and
     * {@code null} otherwise.
     *
     * @return an odd-length cycle if the graph is not bipartite
     * (and hence has an odd-length cycle), and {@code null}
     * otherwise
     */
    public Iterable<Integer> oddCycle() {
        return cycle;
    }

    private boolean check(Graph G) {
        // graph is bipartite
        if (isBipartite) {
            for (int v = 0; v < G.V(); v++) {
                for (int w : G.getAdj(v)) {
                    if (color[v] == color[w]) {
                        System.err.printf("edge %d-%d with %d and %d in same side of bipartition\n", v, w, v, w);
                        return false;
                    }
                }
            }
        }

        // graph has an odd-length cycle
        else {
            // verify cycle
            int first = -1, last = -1;
            for (int v : oddCycle()) {
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

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = isVisited.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }
    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addVertex(5);
        graph.addVertex(6);
        graph.addVertex(7);
        graph.addVertex(8);
        graph.addVertex(9);
        graph.addEdge(0, 5);
        graph.addEdge(0, 6);
        graph.addEdge(0, 8);
        graph.addEdge(1, 5);
        graph.addEdge(1, 6);

        graph.addEdge(2, 5);
        graph.addEdge(2, 7);
        graph.addEdge(2, 8);

        graph.addEdge(3, 6);
        graph.addEdge(3, 9);

        graph.addEdge(4, 6);
        graph.addEdge(4, 9);

        Bipartite b = new Bipartite(graph);
        if (b.isBipartite()) {
            System.out.println("Graph is bipartite");
            for (int v = 0; v < graph.V(); v++) {
                System.out.println(v + ": " + b.color(v));
            }
        } else {
            System.out.print("Graph has an odd-length cycle: ");
            for (int x : b.oddCycle()) {
                System.out.print(x + " ");
            }
            System.out.println();
        }
    }

}
