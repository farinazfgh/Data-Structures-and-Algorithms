import java.util.LinkedList;
import java.util.Queue;

/*
The BipartiteMatching class represents a data type for computing a maximum (cardinality) matching and a minimum (cardinality) vertex cover in a bipartite graph.
A bipartite graph in a graph whose vertices can be partitioned into two disjoint sets such that every edge has one endpoint in either set.
A matching in a graph is a subset of its edges with no common vertices.
A maximum matching is a matching with the maximum number of edges.
A perfect matching is a matching which matches all vertices in the graph.
A vertex cover in a graph is a subset of its vertices such that every edge is incident to at least one vertex.
A minimum vertex cover is a vertex cover with the minimum number of vertices.
By Konig's theorem, in any biparite graph, the maximum number of edges in matching equals the minimum number of vertices in a vertex cover.
The maximum matching problem in nonbipartite graphs is also important, but all known algorithms for this more general problem are substantially more complicated.
This implementation uses the alternating-path algorithm.
It is equivalent to reducing to the maximum-flow problem and running the augmenting-path algorithm on the resulting flow network, but it does so with less overhead.
The constructor takes O((E + V) V) time, where E is the number of edges and V is the number of vertices in the graph.
It uses Θ(V) extra space (not including the graph).
See also HopcroftKarp, which solves the problem in O(E sqrt(V)) using the Hopcroft-Karp algorithm and BipartiteMatchingToMaxflow, which solves the problem in O((E + V) V) time via a reduction to maxflow.
 */
public class BipartiteMatching {
    private static final int UNMATCHED = -1;

    private final int V;                 // number of vertices in the graph
    private BipartiteX bipartition;      // the bipartition
    private int cardinality;             // cardinality of current matching
    private int[] mate;                  // mate[v] =  w if v-w is an edge in current matching
    //         = -1 if v is not in current matching
    private boolean[] inMinVertexCover;  // inMinVertexCover[v] = true iff v is in min vertex cover
    private boolean[] isVisited;            // marked[v] = true iff v is reachable via alternating path
    private int[] fromEdge;                // edgeTo[v] = last edge on alternating path to v

    /**
     * Determines a maximum matching (and a minimum vertex cover)
     * in a bipartite graph.
     *
     * @param G the bipartite graph
     * @throws IllegalArgumentException if {@code G} is not bipartite
     */
    public BipartiteMatching(Graph G) {
        bipartition = new BipartiteX(G);
        System.out.println("Bipartite is calculated:");
        System.out.println(bipartition);

        if (!bipartition.isBipartite()) {
            throw new IllegalArgumentException("graph is not bipartite");
        }

        this.V = G.V();
        initializeMate();

        // alternating path algorithm
        while (hasAugmentingPath(G)) {

            // find one endpoint t in alternating path
            int t = -1;
            for (int v = 0; v < G.V(); v++) {
                if (!isMatched(v) && fromEdge[v] != -1) {
                    t = v;
                    break;
                }
            }

            // update the matching according to alternating path in edgeTo[] array
            for (int v = t; v != -1; v = fromEdge[fromEdge[v]]) {
                int w = fromEdge[v];
                mate[v] = w;
                mate[w] = v;
            }
            cardinality++;
        }

        // find min vertex cover from marked[] array
        inMinVertexCover = new boolean[V];
        for (int v = 0; v < V; v++) {
            if (bipartition.color(v) && !isVisited[v]) inMinVertexCover[v] = true;
            if (!bipartition.color(v) && isVisited[v]) inMinVertexCover[v] = true;
        }

        assert certifySolution(G);
    }

    private void initializeMate() {
        // initialize empty matching
        mate = new int[V];
        for (int v = 0; v < V; v++)
            mate[v] = UNMATCHED;
    }


    /*
     * is there an augmenting path?
     *   - if so, upon termination adj[] contains the level graph;
     *   - if not, upon termination marked[] specifies those vertices reachable via an alternating
     *     path from one side of the bipartition
     *
     * an alternating path is a path whose edges belong alternately to the matching and not
     * to the matching
     *
     * an augmenting path is an alternating path that starts and ends at unmatched vertices
     *
     * this implementation finds a shortest augmenting path (fewest number of edges), though there
     * is no particular advantage to do so here
     */
    private boolean hasAugmentingPath(Graph G) {
        isVisited = new boolean[V];

        fromEdge = new int[V];
        for (int v = 0; v < V; v++)
            fromEdge[v] = -1;

        // breadth-first search (starting from all unmatched vertices on one side of bipartition)
        Queue<Integer> queue = new LinkedList<>();
        for (int v = 0; v < V; v++) {
            if (bipartition.color(v) && !isMatched(v)) {
                queue.offer(v);
                isVisited[v] = true;
            }
        }

        // run BFS, stopping as soon as an alternating path is found
        while (!queue.isEmpty()) {
            int v = queue.poll();
            for (int w : G.getAdj(v)) {

                // either (1) forward edge not in matching or (2) backward edge in matching
                if (isResidualGraphEdge(v, w) && !isVisited[w]) {
                    fromEdge[w] = v;
                    isVisited[w] = true;
                    if (!isMatched(w)) return true;
                    queue.offer(w);
                }
            }
        }

        return false;
    }

    // is the edge v-w a forward edge not in the matching or a reverse edge in the matching?
    private boolean isResidualGraphEdge(int v, int w) {
        if ((mate[v] != w) && bipartition.color(v)) return true;
        return (mate[v] == w) && !bipartition.color(v);
    }

    /**
     * Returns the vertex to which the specified vertex is matched in
     * the maximum matching computed by the algorithm.
     *
     * @param v the vertex
     * @return the vertex to which vertex {@code v} is matched in the
     * maximum matching; {@code -1} if the vertex is not matched
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int mate(int v) {
        validate(v);
        return mate[v];
    }

    /**
     * Returns true if the specified vertex is matched in the maximum matching
     * computed by the algorithm.
     *
     * @param v the vertex
     * @return {@code true} if vertex {@code v} is matched in maximum matching;
     * {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean isMatched(int v) {
        validate(v);
        return mate[v] != UNMATCHED;
    }

    /**
     * Returns the number of edges in a maximum matching.
     *
     * @return the number of edges in a maximum matching
     */
    public int size() {
        return cardinality;
    }

    /**
     * Returns true if the graph contains a perfect matching.
     * That is, the number of edges in a maximum matching is equal to one half
     * of the number of vertices in the graph (so that every vertex is matched).
     *
     * @return {@code true} if the graph contains a perfect matching;
     * {@code false} otherwise
     */
    public boolean isPerfect() {
        return cardinality * 2 == V;
    }

    /**
     * Returns true if the specified vertex is in the minimum vertex cover
     * computed by the algorithm.
     *
     * @param v the vertex
     * @return {@code true} if vertex {@code v} is in the minimum vertex cover;
     * {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean inMinVertexCover(int v) {
        validate(v);
        return inMinVertexCover[v];
    }

    private void validate(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    /**************************************************************************
     *
     *  The code below is solely for testing correctness of the data type.
     *
     **************************************************************************/

// check that mate[] and inVertexCover[] define a max matching and min vertex cover, respectively
    private boolean certifySolution(Graph G) {

        // check that mate(v) = w iff mate(w) = v
        for (int v = 0; v < V; v++) {
            if (mate(v) == -1) continue;
            if (mate(mate(v)) != v) return false;
        }

        // check that size() is consistent with mate()
        int matchedVertices = 0;
        for (int v = 0; v < V; v++) {
            if (mate(v) != -1) matchedVertices++;
        }
        if (2 * size() != matchedVertices) return false;

        // check that size() is consistent with minVertexCover()
        int sizeOfMinVertexCover = 0;
        for (int v = 0; v < V; v++)
            if (inMinVertexCover(v)) sizeOfMinVertexCover++;
        if (size() != sizeOfMinVertexCover) return false;

        // check that mate() uses each vertex at most once
        boolean[] isMatched = new boolean[V];
        for (int v = 0; v < V; v++) {
            int w = mate[v];
            if (w == -1) continue;
            if (v == w) return false;
            if (v >= w) continue;
            if (isMatched[v] || isMatched[w]) return false;
            isMatched[v] = true;
            isMatched[w] = true;
        }

        // check that mate() uses only edges that appear in the graph
        for (int v = 0; v < V; v++) {
            if (mate(v) == -1) continue;
            boolean isEdge = false;
            for (int w : G.getAdj(v)) {
                if (mate(v) == w) isEdge = true;
            }
            if (!isEdge) return false;
        }

        // check that inMinVertexCover() is a vertex cover
        for (int v = 0; v < V; v++)
            for (int w : G.getAdj(v))
                if (!inMinVertexCover(v) && !inMinVertexCover(w)) return false;

        return true;
    }

    /**
     * Unit tests the {@code HopcroftKarp} data type.
     * Takes three command-line arguments {@code V1}, {@code V2}, and {@code E};
     * creates a random bipartite graph with {@code V1} + {@code V2} vertices
     * and {@code E} edges; computes a maximum matching and minimum vertex cover;
     * and prints the results.
     *
     * @param args the command-line arguments
     */
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
        /*
        Farinaz, [01.11.20 19:09]
        9: 7 2
         */
        graph.addEdge(1, 2);
        graph.addEdge(1, 7);
        graph.addEdge(1, 6);

        graph.addEdge(2, 1);
        graph.addEdge(2, 5);
        graph.addEdge(2, 4);
        graph.addEdge(2, 9);

        graph.addEdge(3, 8);

        graph.addEdge(4, 8);
        graph.addEdge(4, 6);
        graph.addEdge(4, 2);

        graph.addEdge(5, 6);
        graph.addEdge(5, 2);
        graph.addEdge(5, 8);

        graph.addEdge(6, 5);
        graph.addEdge(6, 4);
        graph.addEdge(6, 1);

        graph.addEdge(7, 1);
        graph.addEdge(7, 9);

        graph.addEdge(8, 5);
        graph.addEdge(8, 4);
        graph.addEdge(8, 3);

        graph.addEdge(9, 7);
        graph.addEdge(9, 2);

        System.out.println();

        BipartiteMatching matching = new BipartiteMatching(graph);

        // print maximum matching
        System.out.printf("Number of edges in max matching        = %d\n", matching.size());
        System.out.printf("Number of vertices in min vertex cover = %d\n", matching.size());
        System.out.printf("Graph has a perfect matching           = %b\n", matching.isPerfect());
        System.out.println();

        if (graph.V() >= 1000) return;

        System.out.print("Max matching: ");
        for (int v = 0; v < graph.V(); v++) {
            int w = matching.mate(v);
            if (matching.isMatched(v) && v < w)  // print each edge only once
                System.out.print(v + "-" + w + " ");
        }
        System.out.println();

        // print minimum vertex cover
        System.out.print("Min vertex cover: ");
        for (int v = 0; v < graph.V(); v++)
            if (matching.inMinVertexCover(v))
                System.out.print(v + " ");
        System.out.println();
    }

}
