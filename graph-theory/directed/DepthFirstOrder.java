package directed; /******************************************************************************
 *  Compute preorder and postorder for a digraph or edge-weighted digraph.
 *  Runs in O(E + getNumberofVertices) time.
 ******************************************************************************/

import util.In;
import util.Queue;
import util.StdOut;

import java.util.Stack;

/**
 * The  directed.DepthFirstOrder class represents a data type for
 * determining depth-first search ordering of the vertices in a digraph
 * or edge-weighted digraph, including preorder, postorder, and reverse postorder.
 */
public class DepthFirstOrder {
    private final boolean[] isVisited;          // isVisited[vertex] = has vertex been isVisited in dfs?
    private final int[] pre;                 // pre[vertex]    = preorder  number of vertex
    private final int[] post;                // post[vertex]   = postorder number of vertex
    private final Queue<Integer> preorder;   // vertices in preorder
    private final Queue<Integer> postorder;  // vertices in postorder
    private int preCounter;            // counter or preorder numbering
    private int postCounter;           // counter for postorder numbering

    public DepthFirstOrder(Digraph G) {
        pre = new int[G.getNumberofVertices()];
        post = new int[G.getNumberofVertices()];
        postorder = new Queue<>();
        preorder = new Queue<>();
        isVisited = new boolean[G.getNumberofVertices()];
        for (int vertex = 0; vertex < G.getNumberofVertices(); vertex++) {
            if (!isVisited[vertex]) {
                dfs(G, vertex);
            }
        }

        assert check();
    }



    /**
     * Unit tests the {@code directed.DepthFirstOrder} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);

        DepthFirstOrder dfs = new DepthFirstOrder(G);
        StdOut.println("   vertex  pre post");
        StdOut.println("--------------");
        for (int vertex = 0; vertex < G.getNumberofVertices(); vertex++) {
            StdOut.printf("%4d %4d %4d\n", vertex, dfs.pre(vertex), dfs.post(vertex));
        }

        StdOut.print("Preorder:  ");
        for (int vertex : dfs.pre()) {
            StdOut.print(vertex + " ");
        }
        StdOut.println();

        StdOut.print("Postorder: ");
        for (int vertex : dfs.post()) {
            StdOut.print(vertex + " ");
        }
        StdOut.println();

        StdOut.print("Reverse postorder: ");
        for (int vertex : dfs.reversePost()) {
            StdOut.print(vertex + " ");
        }
        StdOut.println();
    }

    private void dfs(Digraph G, int vertex) {
        isVisited[vertex] = true;

        pre[vertex] = preCounter++;
        preorder.enqueue(vertex);

        for (int current : G.getAdjacencyList(vertex)) {
            if (!isVisited[current]) {
                dfs(G, current);
            }
        }

        postorder.enqueue(vertex);
        post[vertex] = postCounter++;
    }



    /**
     * Returns the preorder number of vertex {@code vertex}.
     *
     * @param vertex the vertex
     * @return the preorder number of vertex {@code vertex}
     * @throws IllegalArgumentException unless {@code 0 <= vertex < getNumberofVertices}
     */
    public int pre(int vertex) {
        validateVertex(vertex);
        return pre[vertex];
    }

    /**
     * Returns the postorder number of vertex {@code vertex}.
     *
     * @param vertex the vertex
     * @return the postorder number of vertex {@code vertex}
     * @throws IllegalArgumentException unless {@code 0 <= vertex < getNumberofVertices}
     */
    public int post(int vertex) {
        validateVertex(vertex);
        return post[vertex];
    }

    /**
     * Returns the vertices in postorder.
     *
     * @return the vertices in postorder, as an iterable of vertices
     */
    public Iterable<Integer> post() {
        return postorder;
    }

    /**
     * Returns the vertices in preorder.
     *
     * @return the vertices in preorder, as an iterable of vertices
     */
    public Iterable<Integer> pre() {
        return preorder;
    }

    /**
     * Returns the vertices in reverse postorder.
     *
     * @return the vertices in reverse postorder, as an iterable of vertices
     */
    public Iterable<Integer> reversePost() {
        Stack<Integer> reverse = new Stack<Integer>();
        for (int vertex : postorder)
            reverse.push(vertex);
        return reverse;
    }

    // check that pre() and post() are consistent with pre(vertex) and post(vertex)
    private boolean check() {

        // check that post(vertex) is consistent with post()
        int r = 0;
        for (int vertex : post()) {
            if (post(vertex) != r) {
                StdOut.println("post(vertex) and post() inconsistent");
                return false;
            }
            r++;
        }

        // check that pre(vertex) is consistent with pre()
        r = 0;
        for (int vertex : pre()) {
            if (pre(vertex) != r) {
                StdOut.println("pre(vertex) and pre() inconsistent");
                return false;
            }
            r++;
        }

        return true;
    }

    // throw an IllegalArgumentException unless {@code 0 <= vertex < getNumberofVertices}
    private void validateVertex(int vertex) {
        int getNumberofVertices = isVisited.length;
        if (vertex < 0 || vertex >= getNumberofVertices)
            throw new IllegalArgumentException("vertex " + vertex + " is not between 0 and " + (getNumberofVertices - 1));
    }
}
