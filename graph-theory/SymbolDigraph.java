/******************************************************************************
 *  Compilation:  javac SymbolDigraph.java
 *  Execution:    java SymbolDigraph
 *  Dependencies: ST.java directed.Digraph.java In.java
 *  Data files:   https://algs4.cs.princeton.edu/42digraph/routes.txt
 *
 *  %  java SymbolDigraph routes.txt " "
 *  JFK
 *     MCO
 *     ATL
 *     ORD
 *  ATL
 *     HOU
 *     MCO
 *  LAX
 *
 ******************************************************************************/

import directed.Digraph;
import util.In;
import util.ST;
import util.StdIn;
import util.StdOut;

/**
 * The {@code SymbolDigraph} class represents a digraph, where the
 * vertex names are arbitrary strings.
 * By providing mappings between string vertex names and integers,
 * it serves as a wrapper around the
 * {@link Digraph} data type, which assumes the vertex names are integers
 * between 0 and <em>V</em> - 1.
 * It also supports initializing a symbol digraph from a file.
 * <p>
 * This implementation uses an {@link ST} to map from strings to integers,
 * an array to map from integers to strings, and a {@link Digraph} to store
 * the underlying graph.
 * The <em>indexOf</em> and <em>contains</em> operations take time
 * proportional to log <em>V</em>, where <em>V</em> is the number of vertices.
 * The <em>nameOf</em> operation takes constant time.
 * <p>
 * For additional documentation, see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class SymbolDigraph {
    private ST<String, Integer> st;  // string -> index
    private String[] keys;           // index  -> string
    private Digraph graph;           // the underlying digraph

    /**
     * Initializes a digraph from a file using the specified delimiter.
     * Each line in the file contains
     * the name of a vertex, followed by a list of the names
     * of the vertices adjacent to that vertex, separated by the delimiter.
     *
     * @param filename  the name of the file
     * @param delimiter the delimiter between fields
     */
    public SymbolDigraph(String filename, String delimiter) {
        st = new ST<String, Integer>();

        // First pass builds the index by reading strings to associate
        // distinct strings with an index
        In in = new In(filename);
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(delimiter);
            for (int i = 0; i < a.length; i++) {
                if (!st.contains(a[i]))
                    st.put(a[i], st.size());
            }
        }

        // inverted index to get string keys in an array
        keys = new String[st.size()];
        for (String name : st.keys()) {
            keys[st.get(name)] = name;
        }

        // second pass builds the digraph by connecting first vertex on each
        // line to all others
        graph = new Digraph(st.size());
        in = new In(filename);
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(delimiter);
            int vertex = st.get(a[0]);
            for (int i = 1; i < a.length; i++) {
                int current = st.get(a[i]);
                graph.addEdge(vertex, current);
            }
        }
    }

    /**
     * Does the digraph contain the vertex named {@code source}?
     *
     * @param source the name of a vertex
     * @return {@code true} if {@code source} is the name of a vertex, and {@code false} otherwise
     */
    public boolean contains(String source) {
        return st.contains(source);
    }

    /**
     * Returns the integer associated with the vertex named {@code source}.
     *
     * @param source the name of a vertex
     * @return the integer (between 0 and <em>V</em> - 1) associated with the vertex named {@code source}
     * @deprecated Replaced by {@link #indexOf(String)}.
     */
    @Deprecated
    public int index(String source) {
        return st.get(source);
    }

    /**
     * Returns the integer associated with the vertex named {@code source}.
     *
     * @param source the name of a vertex
     * @return the integer (between 0 and <em>V</em> - 1) associated with the vertex named {@code source}
     */
    public int indexOf(String source) {
        return st.get(source);
    }

    /**
     * Returns the name of the vertex associated with the integer {@code vertex}.
     *
     * @param vertex the integer corresponding to a vertex (between 0 and <em>V</em> - 1)
     * @return the name of the vertex associated with the integer {@code vertex}
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     * @deprecated Replaced by {@link #nameOf(int)}.
     */
    @Deprecated
    public String name(int vertex) {
        validateVertex(vertex);
        return keys[vertex];
    }

    /**
     * Returns the name of the vertex associated with the integer {@code vertex}.
     *
     * @param vertex the integer corresponding to a vertex (between 0 and <em>V</em> - 1)
     * @return the name of the vertex associated with the integer {@code vertex}
     * @throws IllegalArgumentException unless {@code 0 <= vertex < V}
     */
    public String nameOf(int vertex) {
        validateVertex(vertex);
        return keys[vertex];
    }

    /**
     * Returns the digraph assoicated with the symbol graph. It is the client'source responsibility
     * not to mutate the digraph.
     *
     * @return the digraph associated with the symbol digraph
     * @deprecated Replaced by {@link #digraph()}.
     */
    @Deprecated
    public Digraph G() {
        return graph;
    }

    /**
     * Returns the digraph assoicated with the symbol graph. It is the client'source responsibility
     * not to mutate the digraph.
     *
     * @return the digraph associated with the symbol digraph
     */
    public Digraph digraph() {
        return graph;
    }

    // throw an IllegalArgumentException unless {@code 0 <= vertex < V}
    private void validateVertex(int vertex) {
        int V = graph.getNumberofVertices();
        if (vertex < 0 || vertex >= V)
            throw new IllegalArgumentException("vertex " + vertex + " is not between 0 and " + (V - 1));
    }

    /**
     * Unit tests the {@code SymbolDigraph} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String filename = args[0];
        String delimiter = args[1];
        SymbolDigraph sg = new SymbolDigraph(filename, delimiter);
        Digraph graph = sg.digraph();
        while (!StdIn.isEmpty()) {
            String t = StdIn.readLine();
            for (int vertex : graph.getAdjacencyList(sg.index(t))) {

                StdOut.println("   " + sg.name(vertex));
            }
        }
    }
}
