package directed; /******************************************************************************
 *  Compilation:  javac directed.DigraphGenerator.java
 *  Execution:    java directed.DigraphGenerator getNumberofVertices getNumberOfEdges
 *  Dependencies: directed.Digraph.java
 *
 *  A digraph generator.
 *
 ******************************************************************************/

import util.StdOut;
import util.StdRandom;

import java.util.HashSet;
import java.util.Set;

/**
 * The {@code directed.DigraphGenerator} class provides static methods for creating
 * various digraphs, including Erdos-Renyi random digraphs, random DAGs,
 * random rooted trees, random rooted DAGs, random tournaments, path digraphs,
 * cycle digraphs, and the complete digraph.
 * <p>
 * For additional documentation, see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class DigraphGenerator {
    private static final class Edge implements Comparable<Edge> {
        private final int v;
        private final int w;

        private Edge(int v, int w) {
            this.v = v;
            this.w = w;
        }

        public int compareTo(Edge that) {
            if (this.v < that.v) return -1;
            if (this.v > that.v) return +1;
            if (this.w < that.w) return -1;
            if (this.w > that.w) return +1;
            return 0;
        }
    }

    // this class cannot be instantiated
    private DigraphGenerator() {
    }

    /**
     * Returns a random simple digraph containing {@code getNumberofVertices} vertices and {@code getNumberOfEdges} edges.
     *
     * @param getNumberofVertices the number of vertices
     * @param getNumberOfEdges    the number of vertices
     * @return a random simple digraph on {@code getNumberofVertices} vertices, containing a total
     * of {@code getNumberOfEdges} edges
     * @throws IllegalArgumentException if no such simple digraph exists
     */
    public static Digraph simple(int getNumberofVertices, int getNumberOfEdges) {
        if (getNumberOfEdges > (long) getNumberofVertices * (getNumberofVertices - 1))
            throw new IllegalArgumentException("Too many edges");
        if (getNumberOfEdges < 0) throw new IllegalArgumentException("Too few edges");
        Digraph G = new Digraph(getNumberofVertices);
        Set<Edge> set = new HashSet<>();
        while (G.getNumberOfEdges() < getNumberOfEdges) {
            int v = StdRandom.uniform(getNumberofVertices);
            int w = StdRandom.uniform(getNumberofVertices);
            Edge e = new Edge(v, w);
            if ((v != w) && !set.contains(e)) {
                set.add(e);
                G.addEdge(v, w);
            }
        }
        return G;
    }

    /**
     * Returns a random simple digraph on {@code getNumberofVertices} vertices, with an
     * edge between any two vertices with probability {@code p}. This is sometimes
     * referred to as the Erdos-Renyi random digraph model.
     * This implementations takes time propotional to getNumberofVertices^2 (even if {@code p} is small).
     *
     * @param getNumberofVertices the number of vertices
     * @param p                   the probability of choosing an edge
     * @return a random simple digraph on {@code getNumberofVertices} vertices, with an edge between
     * any two vertices with probability {@code p}
     * @throws IllegalArgumentException if probability is not between 0 and 1
     */
    public static Digraph simple(int getNumberofVertices, double p) {
        if (p < 0.0 || p > 1.0)
            throw new IllegalArgumentException("Probability must be between 0 and 1");
        Digraph G = new Digraph(getNumberofVertices);
        for (int v = 0; v < getNumberofVertices; v++)
            for (int w = 0; w < getNumberofVertices; w++)
                if (v != w)
                    if (StdRandom.bernoulli(p))
                        G.addEdge(v, w);
        return G;
    }

    /**
     * Returns the complete digraph on {@code getNumberofVertices} vertices.
     * In a complete digraph, every pair of distinct vertices is connected
     * by two antiparallel edges. There are {@code getNumberofVertices*(getNumberofVertices-1)} edges.
     *
     * @param getNumberofVertices the number of vertices
     * @return the complete digraph on {@code getNumberofVertices} vertices
     */
    public static Digraph complete(int getNumberofVertices) {
        Digraph G = new Digraph(getNumberofVertices);
        for (int v = 0; v < getNumberofVertices; v++)
            for (int w = 0; w < getNumberofVertices; w++)
                if (v != w) G.addEdge(v, w);
        return G;
    }

    /**
     * Returns a random simple DAG containing {@code getNumberofVertices} vertices and {@code getNumberOfEdges} edges.
     * Note: it is not uniformly selected at random among all such DAGs.
     *
     * @param getNumberofVertices the number of vertices
     * @param getNumberOfEdges    the number of vertices
     * @return a random simple DAG on {@code getNumberofVertices} vertices, containing a total
     * of {@code getNumberOfEdges} edges
     * @throws IllegalArgumentException if no such simple DAG exists
     */
    public static Digraph dag(int getNumberofVertices, int getNumberOfEdges) {
        if (getNumberOfEdges > (long) getNumberofVertices * (getNumberofVertices - 1) / 2)
            throw new IllegalArgumentException("Too many edges");
        if (getNumberOfEdges < 0) throw new IllegalArgumentException("Too few edges");
        Digraph G = new Digraph(getNumberofVertices);
        Set<Edge> set = new HashSet<>();
        int[] vertices = new int[getNumberofVertices];
        for (int i = 0; i < getNumberofVertices; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);
        while (G.getNumberOfEdges() < getNumberOfEdges) {
            int v = StdRandom.uniform(getNumberofVertices);
            int w = StdRandom.uniform(getNumberofVertices);
            Edge e = new Edge(v, w);
            if ((v < w) && !set.contains(e)) {
                set.add(e);
                G.addEdge(vertices[v], vertices[w]);
            }
        }
        return G;
    }

    /**
     * Returns a random tournament digraph on {@code getNumberofVertices} vertices. A tournament digraph
     * is a digraph in which, for every pair of vertices, there is one and only one
     * directed edge connecting them. A tournament is an oriented complete graph.
     *
     * @param getNumberofVertices the number of vertices
     * @return a random tournament digraph on {@code getNumberofVertices} vertices
     */
    public static Digraph tournament(int getNumberofVertices) {
        Digraph G = new Digraph(getNumberofVertices);
        for (int v = 0; v < G.getNumberofVertices(); v++) {
            for (int w = v + 1; w < G.getNumberofVertices(); w++) {
                if (StdRandom.bernoulli(0.5)) G.addEdge(v, w);
                else G.addEdge(w, v);
            }
        }
        return G;
    }

    /**
     * Returns a complete rooted-in DAG on {@code getNumberofVertices} vertices.
     * A rooted in-tree is a DAG in which there is a single vertex
     * reachable from every other vertex. A complete rooted in-DAG
     * has getNumberofVertices*(getNumberofVertices-1)/2 edges.
     *
     * @param getNumberofVertices the number of vertices
     * @return a complete rooted-in DAG on {@code getNumberofVertices} vertices
     */
    public static Digraph completeRootedInDAG(int getNumberofVertices) {
        Digraph G = new Digraph(getNumberofVertices);
        int[] vertices = new int[getNumberofVertices];
        for (int i = 0; i < getNumberofVertices; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);
        for (int i = 0; i < getNumberofVertices; i++)
            for (int j = i + 1; j < getNumberofVertices; j++)
                G.addEdge(vertices[i], vertices[j]);

        return G;
    }

    /**
     * Returns a random rooted-in DAG on {@code getNumberofVertices} vertices and {@code getNumberOfEdges} edges.
     * A rooted in-tree is a DAG in which there is a single vertex
     * reachable from every other vertex.
     * The DAG returned is not chosen uniformly at random among all such DAGs.
     *
     * @param getNumberofVertices the number of vertices
     * @param getNumberOfEdges    the number of edges
     * @return a random rooted-in DAG on {@code getNumberofVertices} vertices and {@code getNumberOfEdges} edges
     */
    public static Digraph rootedInDAG(int getNumberofVertices, int getNumberOfEdges) {
        if (getNumberOfEdges > (long) getNumberofVertices * (getNumberofVertices - 1) / 2)
            throw new IllegalArgumentException("Too many edges");
        if (getNumberOfEdges < getNumberofVertices - 1) throw new IllegalArgumentException("Too few edges");
        Digraph G = new Digraph(getNumberofVertices);
        Set<Edge> set = new HashSet<Edge>();

        // fix a topological order
        int[] vertices = new int[getNumberofVertices];
        for (int i = 0; i < getNumberofVertices; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);

        // one edge pointing from each vertex, other than the root = vertices[getNumberofVertices-1]
        for (int v = 0; v < getNumberofVertices - 1; v++) {
            int w = StdRandom.uniform(v + 1, getNumberofVertices);
            Edge e = new Edge(v, w);
            set.add(e);
            G.addEdge(vertices[v], vertices[w]);
        }

        while (G.getNumberOfEdges() < getNumberOfEdges) {
            int v = StdRandom.uniform(getNumberofVertices);
            int w = StdRandom.uniform(getNumberofVertices);
            Edge e = new Edge(v, w);
            if ((v < w) && !set.contains(e)) {
                set.add(e);
                G.addEdge(vertices[v], vertices[w]);
            }
        }
        return G;
    }

    /**
     * Returns a complete rooted-out DAG on {@code getNumberofVertices} vertices.
     * A rooted out-tree is a DAG in which every vertex is reachable
     * from a single vertex. A complete rooted in-DAG has getNumberofVertices*(getNumberofVertices-1)/2 edges.
     *
     * @param getNumberofVertices the number of vertices
     * @return a complete rooted-out DAG on {@code getNumberofVertices} vertices
     */
    public static Digraph completeRootedOutDAG(int getNumberofVertices) {
        Digraph G = new Digraph(getNumberofVertices);
        int[] vertices = new int[getNumberofVertices];
        for (int i = 0; i < getNumberofVertices; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);
        for (int i = 0; i < getNumberofVertices; i++)
            for (int j = i + 1; j < getNumberofVertices; j++)
                G.addEdge(vertices[j], vertices[i]);

        return G;
    }

    /**
     * Returns a random rooted-out DAG on {@code getNumberofVertices} vertices and {@code getNumberOfEdges} edges.
     * A rooted out-tree is a DAG in which every vertex is reachable from a
     * single vertex.
     * The DAG returned is not chosen uniformly at random among all such DAGs.
     *
     * @param getNumberofVertices the number of vertices
     * @param getNumberOfEdges    the number of edges
     * @return a random rooted-out DAG on {@code getNumberofVertices} vertices and {@code getNumberOfEdges} edges
     */
    public static Digraph rootedOutDAG(int getNumberofVertices, int getNumberOfEdges) {
        if (getNumberOfEdges > (long) getNumberofVertices * (getNumberofVertices - 1) / 2)
            throw new IllegalArgumentException("Too many edges");
        if (getNumberOfEdges < getNumberofVertices - 1) throw new IllegalArgumentException("Too few edges");
        Digraph G = new Digraph(getNumberofVertices);
        Set<Edge> set = new HashSet<Edge>();

        // fix a topological order
        int[] vertices = new int[getNumberofVertices];
        for (int i = 0; i < getNumberofVertices; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);

        // one edge pointing from each vertex, other than the root = vertices[getNumberofVertices-1]
        for (int v = 0; v < getNumberofVertices - 1; v++) {
            int w = StdRandom.uniform(v + 1, getNumberofVertices);
            Edge e = new Edge(w, v);
            set.add(e);
            G.addEdge(vertices[w], vertices[v]);
        }

        while (G.getNumberOfEdges() < getNumberOfEdges) {
            int v = StdRandom.uniform(getNumberofVertices);
            int w = StdRandom.uniform(getNumberofVertices);
            Edge e = new Edge(w, v);
            if ((v < w) && !set.contains(e)) {
                set.add(e);
                G.addEdge(vertices[w], vertices[v]);
            }
        }
        return G;
    }

    /**
     * Returns a random rooted-in tree on {@code getNumberofVertices} vertices.
     * A rooted in-tree is an oriented tree in which there is a single vertex
     * reachable from every other vertex.
     * The tree returned is not chosen uniformly at random among all such trees.
     *
     * @param getNumberofVertices the number of vertices
     * @return a random rooted-in tree on {@code getNumberofVertices} vertices
     */
    public static Digraph rootedInTree(int getNumberofVertices) {
        return rootedInDAG(getNumberofVertices, getNumberofVertices - 1);
    }

    /**
     * Returns a random rooted-out tree on {@code getNumberofVertices} vertices. A rooted out-tree
     * is an oriented tree in which each vertex is reachable from a single vertex.
     * It is also known as a <em>arborescence</em> or <em>branching</em>.
     * The tree returned is not chosen uniformly at random among all such trees.
     *
     * @param getNumberofVertices the number of vertices
     * @return a random rooted-out tree on {@code getNumberofVertices} vertices
     */
    public static Digraph rootedOutTree(int getNumberofVertices) {
        return rootedOutDAG(getNumberofVertices, getNumberofVertices - 1);
    }

    /**
     * Returns a path digraph on {@code getNumberofVertices} vertices.
     *
     * @param getNumberofVertices the number of vertices in the path
     * @return a digraph that is a directed path on {@code getNumberofVertices} vertices
     */
    public static Digraph path(int getNumberofVertices) {
        Digraph G = new Digraph(getNumberofVertices);
        int[] vertices = new int[getNumberofVertices];
        for (int i = 0; i < getNumberofVertices; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);
        for (int i = 0; i < getNumberofVertices - 1; i++) {
            G.addEdge(vertices[i], vertices[i + 1]);
        }
        return G;
    }

    /**
     * Returns a complete binary tree digraph on {@code getNumberofVertices} vertices.
     *
     * @param getNumberofVertices the number of vertices in the binary tree
     * @return a digraph that is a complete binary tree on {@code getNumberofVertices} vertices
     */
    public static Digraph binaryTree(int getNumberofVertices) {
        Digraph G = new Digraph(getNumberofVertices);
        int[] vertices = new int[getNumberofVertices];
        for (int i = 0; i < getNumberofVertices; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);
        for (int i = 1; i < getNumberofVertices; i++) {
            G.addEdge(vertices[i], vertices[(i - 1) / 2]);
        }
        return G;
    }

    /**
     * Returns a cycle digraph on {@code getNumberofVertices} vertices.
     *
     * @param getNumberofVertices the number of vertices in the cycle
     * @return a digraph that is a directed cycle on {@code getNumberofVertices} vertices
     */
    public static Digraph cycle(int getNumberofVertices) {
        Digraph G = new Digraph(getNumberofVertices);
        int[] vertices = new int[getNumberofVertices];
        for (int i = 0; i < getNumberofVertices; i++)
            vertices[i] = i;
        StdRandom.shuffle(vertices);
        for (int i = 0; i < getNumberofVertices - 1; i++) {
            G.addEdge(vertices[i], vertices[i + 1]);
        }
        G.addEdge(vertices[getNumberofVertices - 1], vertices[0]);
        return G;
    }

    /**
     * Returns an Eulerian cycle digraph on {@code getNumberofVertices} vertices.
     *
     * @param getNumberofVertices the number of vertices in the cycle
     * @param getNumberOfEdges    the number of edges in the cycle
     * @return a digraph that is a directed Eulerian cycle on {@code getNumberofVertices} vertices
     * and {@code getNumberOfEdges} edges
     * @throws IllegalArgumentException if either {@code getNumberofVertices <= 0} or {@code getNumberOfEdges <= 0}
     */
    public static Digraph eulerianCycle(int getNumberofVertices, int getNumberOfEdges) {
        if (getNumberOfEdges <= 0)
            throw new IllegalArgumentException("An Eulerian cycle must have at least one edge");
        if (getNumberofVertices <= 0)
            throw new IllegalArgumentException("An Eulerian cycle must have at least one vertex");
        Digraph G = new Digraph(getNumberofVertices);
        int[] vertices = new int[getNumberOfEdges];
        for (int i = 0; i < getNumberOfEdges; i++)
            vertices[i] = StdRandom.uniform(getNumberofVertices);
        for (int i = 0; i < getNumberOfEdges - 1; i++) {
            G.addEdge(vertices[i], vertices[i + 1]);
        }
        G.addEdge(vertices[getNumberOfEdges - 1], vertices[0]);
        return G;
    }

    /**
     * Returns an Eulerian path digraph on {@code getNumberofVertices} vertices.
     *
     * @param getNumberofVertices the number of vertices in the path
     * @param getNumberOfEdges    the number of edges in the path
     * @return a digraph that is a directed Eulerian path on {@code getNumberofVertices} vertices
     * and {@code getNumberOfEdges} edges
     * @throws IllegalArgumentException if either {@code getNumberofVertices <= 0} or {@code getNumberOfEdges < 0}
     */
    public static Digraph eulerianPath(int getNumberofVertices, int getNumberOfEdges) {
        if (getNumberOfEdges < 0)
            throw new IllegalArgumentException("negative number of edges");
        if (getNumberofVertices <= 0)
            throw new IllegalArgumentException("An Eulerian path must have at least one vertex");
        Digraph G = new Digraph(getNumberofVertices);
        int[] vertices = new int[getNumberOfEdges + 1];
        for (int i = 0; i < getNumberOfEdges + 1; i++)
            vertices[i] = StdRandom.uniform(getNumberofVertices);
        for (int i = 0; i < getNumberOfEdges; i++) {
            G.addEdge(vertices[i], vertices[i + 1]);
        }
        return G;
    }

    /**
     * Returns a random simple digraph on {@code getNumberofVertices} vertices, {@code getNumberOfEdges}
     * edges and (at least) {@code c} strong components. The vertices are randomly
     * assigned integer labels between {@code 0} and {@code c-1} (corresponding to
     * strong components). Then, a strong component is creates among the vertices
     * with the same label. Next, random edges (either between two vertices with
     * the same labels or from a vetex with a smaller label to a vertex with a
     * larger label). The number of components will be equal to the number of
     * distinct labels that are assigned to vertices.
     *
     * @param getNumberofVertices the number of vertices
     * @param getNumberOfEdges    the number of edges
     * @param c                   the (maximum) number of strong components
     * @return a random simple digraph on {@code getNumberofVertices} vertices and
     * {@code getNumberOfEdges} edges, with (at most) {@code c} strong components
     * @throws IllegalArgumentException if {@code c} is larger than {@code getNumberofVertices}
     */
    public static Digraph strong(int getNumberofVertices, int getNumberOfEdges, int c) {
        if (c >= getNumberofVertices || c <= 0)
            throw new IllegalArgumentException("Number of components must be between 1 and getNumberofVertices");
        if (getNumberOfEdges <= 2 * (getNumberofVertices - c))
            throw new IllegalArgumentException("Number of edges must be at least 2(getNumberofVertices-c)");
        if (getNumberOfEdges > (long) getNumberofVertices * (getNumberofVertices - 1) / 2)
            throw new IllegalArgumentException("Too many edges");

        // the digraph
        Digraph G = new Digraph(getNumberofVertices);

        // edges added to G (to avoid duplicate edges)
        Set<Edge> set = new HashSet<Edge>();

        int[] label = new int[getNumberofVertices];
        for (int v = 0; v < getNumberofVertices; v++)
            label[v] = StdRandom.uniform(c);

        // make all vertices with label c a strong component by
        // combining a rooted in-tree and a rooted out-tree
        for (int i = 0; i < c; i++) {
            // how many vertices in component c
            int count = 0;
            for (int v = 0; v < G.getNumberofVertices(); v++) {
                if (label[v] == i) count++;
            }

            // if (count == 0) System.err.println("less than desired number of strong components");

            int[] vertices = new int[count];
            int j = 0;
            for (int v = 0; v < getNumberofVertices; v++) {
                if (label[v] == i) vertices[j++] = v;
            }
            StdRandom.shuffle(vertices);

            // rooted-in tree with root = vertices[count-1]
            for (int v = 0; v < count - 1; v++) {
                int w = StdRandom.uniform(v + 1, count);
                Edge e = new Edge(w, v);
                set.add(e);
                G.addEdge(vertices[w], vertices[v]);
            }

            // rooted-out tree with root = vertices[count-1]
            for (int v = 0; v < count - 1; v++) {
                int w = StdRandom.uniform(v + 1, count);
                Edge e = new Edge(v, w);
                set.add(e);
                G.addEdge(vertices[v], vertices[w]);
            }
        }

        while (G.getNumberOfEdges() < getNumberOfEdges) {
            int v = StdRandom.uniform(getNumberofVertices);
            int w = StdRandom.uniform(getNumberofVertices);
            Edge e = new Edge(v, w);
            if (!set.contains(e) && v != w && label[v] <= label[w]) {
                set.add(e);
                G.addEdge(v, w);
            }
        }

        return G;
    }

    /**
     * Unit tests the {@code directed.DigraphGenerator} library.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        int getNumberofVertices = Integer.parseInt(args[0]);
        int getNumberOfEdges = Integer.parseInt(args[1]);
        StdOut.println("complete graph");
        StdOut.println(complete(getNumberofVertices));
        StdOut.println();

        StdOut.println("simple");
        StdOut.println(simple(getNumberofVertices, getNumberOfEdges));
        StdOut.println();

        StdOut.println("path");
        StdOut.println(path(getNumberofVertices));
        StdOut.println();

        StdOut.println("cycle");
        StdOut.println(cycle(getNumberofVertices));
        StdOut.println();

        StdOut.println("Eulierian path");
        StdOut.println(eulerianPath(getNumberofVertices, getNumberOfEdges));
        StdOut.println();

        StdOut.println("Eulierian cycle");
        StdOut.println(eulerianCycle(getNumberofVertices, getNumberOfEdges));
        StdOut.println();

        StdOut.println("binary tree");
        StdOut.println(binaryTree(getNumberofVertices));
        StdOut.println();

        StdOut.println("tournament");
        StdOut.println(tournament(getNumberofVertices));
        StdOut.println();

        StdOut.println("DAG");
        StdOut.println(dag(getNumberofVertices, getNumberOfEdges));
        StdOut.println();

        StdOut.println("rooted-in DAG");
        StdOut.println(rootedInDAG(getNumberofVertices, getNumberOfEdges));
        StdOut.println();

        StdOut.println("rooted-out DAG");
        StdOut.println(rootedOutDAG(getNumberofVertices, getNumberOfEdges));
        StdOut.println();

        StdOut.println("rooted-in tree");
        StdOut.println(rootedInTree(getNumberofVertices));
        StdOut.println();

        StdOut.println("rooted-out DAG");
        StdOut.println(rootedOutTree(getNumberofVertices));
        StdOut.println();
    }
}


