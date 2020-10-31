import java.util.LinkedList;
import java.util.Queue;

public class FordFulkerson {
    /*
    Notice that, when this terminates it terminated because we couldn't find an augmenting path,
    and we couldn't find an augmenting path meant that, essentially, we did a graph search that
    got stuck with finding all full-forward edges or empty backward edges before getting to the target
    and that's precisely the computation that we needed to do to compute the cut.
    So to tell whether a vertex is reachable from s.
    We just checkmarked v so we can tell the client which vertices are in the cut.
    And then we can find the, the edges that leave that vertex with the edges that comprise the cut.
    so now, all we have to do to finish is to look that it has augmenting path, which is the graph search in the residual network.
    And for this example we'll use breadth-first search although the other search algorithms that we've studied can be adapted
    in the same way depth-first search or using a priority queue as in Prim's algorithm or Dijkstra's algorithm.
     */


    private static final double FLOATING_POINT_EPSILON = 1E-11;

    private final int V;          // number of vertices
    private boolean[] isVisited;     // marked[v] = true iff s->v path in residual graph
    private FlowEdge[] fromEdge;    // edgeTo[v] = last edge on shortest residual s->v path
    private double value;         // current value of max flow


    public FordFulkerson(FlowNetwork G, int source, int target) {
        V = G.V();
        validate(source);
        validate(target);
        if (source == target) throw new IllegalArgumentException("Source equals sink");
        if (!isFeasible(G, source, target)) throw new IllegalArgumentException("Initial flow is infeasible");

        // while there exists an augmenting path, use it
        value = excess(G, target);
        while (hasAugmentingPath(G, source, target)) {

            // compute bottleneck capacity
            double bottleneck = Double.POSITIVE_INFINITY;
            for (int current = target; current != source; current = fromEdge[current].other(current)) {
                bottleneck = Math.min(bottleneck, fromEdge[current].residualCapacityTo(current));
            }

            // augment flow
            for (int current = target; current != source; current = fromEdge[current].other(current)) {
                fromEdge[current].addResidualFlowTo(current, bottleneck);
            }

            value += bottleneck;
        }

        // check optimality conditions
        assert check(G, source, target);
    }

    public double value() {
        return value;
    }

    public boolean inCut(int v) {
        validate(v);
        return isVisited[v];
    }

    // throw an IllegalArgumentException if v is outside prescibed range
    private void validate(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }


    // is there an augmenting path?
    // if so, upon termination edgeTo[] will contain a parent-link representation of such a path
    // this implementation finds a shortest augmenting path (fewest number of edges),
    // which performs well both in theory and in practice
    private boolean hasAugmentingPath(FlowNetwork G, int s, int t) {
        fromEdge = new FlowEdge[G.V()];
        isVisited = new boolean[G.V()];

        // breadth-first search
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(s);
        isVisited[s] = true;
        while (!queue.isEmpty() && !isVisited[t]) {
            int v = queue.poll();

            for (FlowEdge e : G.getAdj(v)) {
                int w = e.other(v);

                // if residual capacity from v to w
                if (e.residualCapacityTo(w) > 0) {
                    if (!isVisited[w]) {
                        fromEdge[w] = e;
                        isVisited[w] = true;
                        queue.offer(w);
                    }
                }
            }
        }

        // is there an augmenting path?
        return isVisited[t];
    }


    // return excess flow at vertex v
    private double excess(FlowNetwork G, int v) {
        double excess = 0.0;
        for (FlowEdge e : G.getAdj(v)) {
            if (v == e.from()) excess -= e.flow();
            else excess += e.flow();
        }
        return excess;
    }

    // return excess flow at vertex v
    private boolean isFeasible(FlowNetwork G, int s, int t) {

        // check that capacity constraints are satisfied
        for (int v = 0; v < G.V(); v++) {
            for (FlowEdge e : G.getAdj(v)) {
                if (e.flow() < -FLOATING_POINT_EPSILON || e.flow() > e.capacity() + FLOATING_POINT_EPSILON) {
                    System.err.println("Edge does not satisfy capacity constraints: " + e);
                    return false;
                }
            }
        }

        // check that net flow into a vertex equals zero, except at source and sink
        if (Math.abs(value + excess(G, s)) > FLOATING_POINT_EPSILON) {
            System.err.println("Excess at source = " + excess(G, s));
            System.err.println("Max flow         = " + value);
            return false;
        }
        if (Math.abs(value - excess(G, t)) > FLOATING_POINT_EPSILON) {
            System.err.println("Excess at sink   = " + excess(G, t));
            System.err.println("Max flow         = " + value);
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s || v == t) continue;
            else if (Math.abs(excess(G, v)) > FLOATING_POINT_EPSILON) {
                System.err.println("Net flow out of " + v + " doesn't equal zero");
                return false;
            }
        }
        return true;
    }


    // check optimality conditions
    private boolean check(FlowNetwork G, int s, int t) {

        // check that flow is feasible
        if (!isFeasible(G, s, t)) {
            System.err.println("Flow is infeasible");
            return false;
        }

        // check that s is on the source side of min cut and that t is not on source side
        if (!inCut(s)) {
            System.err.println("source " + s + " is not on source side of min cut");
            return false;
        }
        if (inCut(t)) {
            System.err.println("sink " + t + " is on source side of min cut");
            return false;
        }

        // check that value of min cut = value of max flow
        double mincutValue = 0.0;
        for (int v = 0; v < G.V(); v++) {
            for (FlowEdge e : G.getAdj(v)) {
                if ((v == e.from()) && inCut(e.from()) && !inCut(e.to()))
                    mincutValue += e.capacity();
            }
        }

        if (Math.abs(mincutValue - value) > FLOATING_POINT_EPSILON) {
            System.err.println("Max flow value = " + value + ", min cut value = " + mincutValue);
            return false;
        }

        return true;
    }


    /**
     * Unit tests the {@code FordFulkerson} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        // create flow network with V vertices and E edges
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        int s = 0, t = V - 1;
        FlowNetwork G = new FlowNetwork(V, E);
        System.out.println(G);

        // compute maximum flow and minimum cut
        FordFulkerson maxflow = new FordFulkerson(G, s, t);
        System.out.println("Max flow from " + s + " to " + t);
        for (int v = 0; v < G.V(); v++) {
            for (FlowEdge e : G.getAdj(v)) {
                if ((v == e.from()) && e.flow() > 0)
                    System.out.println("   " + e);
            }
        }

        // print min-cut
        System.out.print("Min cut: ");
        for (int v = 0; v < G.V(); v++) {
            if (maxflow.inCut(v)) System.out.print(v + " ");
        }
        System.out.println();

        System.out.println("Max flow value = " + maxflow.value());
    }

}
