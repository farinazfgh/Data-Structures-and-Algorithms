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
    private double maxFlow;         // current value of max flow
    static int count = 0;

    public FordFulkerson(FlowNetwork G, int source, int target) {
        V = G.V();
        validateAll(G, source, target);

        // while there exists an augmenting path, use it
        maxFlow = excess(G, target);
        while (hasAugmentingPath(G, source, target)) {
            double bottleneck = getBottleneckCapacity(source, target);
            augmentFlow(source, target, bottleneck);
            maxFlow += bottleneck;
        }

        // check optimality conditions
        assert check(G, source, target);
    }

    private void augmentFlow(int source, int target, double bottleneck) {
        // augment flow
        for (int current = target; current != source; current = fromEdge[current].other(current)) {
            fromEdge[current].addResidualFlowTo(current, bottleneck);
        }
    }

    private double getBottleneckCapacity(int source, int target) {
        // compute bottleneck capacity
        double bottleneck = Double.POSITIVE_INFINITY;
        for (int current = target; current != source; current = fromEdge[current].other(current)) {
            bottleneck = Math.min(bottleneck, fromEdge[current].residualCapacityTo(current));
        }
        return bottleneck;
    }

    private void validateAll(FlowNetwork g, int source, int target) {
        validate(source);
        validate(target);
        if (source == target) throw new IllegalArgumentException("Source equals sink");
        if (!isFeasible(g, source, target)) throw new IllegalArgumentException("Initial flow is infeasible");
    }

    public double value() {
        return maxFlow;
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
    private boolean hasAugmentingPath(FlowNetwork G, int source, int target) {
        count++;
        System.out.println("BFS: " + count);
        System.out.println("maxFlow: " + maxFlow);
        printFromege(fromEdge);
        System.out.println(G);
        System.out.println("************************");
        fromEdge = new FlowEdge[G.V()];
        isVisited = new boolean[G.V()];

        // breadth-first search
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(source);
        isVisited[source] = true;
        while (!queue.isEmpty() && !isVisited[target]) {
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
        return isVisited[target];
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
    private boolean isFeasible(FlowNetwork G, int source, int target) {

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
        if (Math.abs(maxFlow + excess(G, source)) > FLOATING_POINT_EPSILON) {
            System.err.println("Excess at source = " + excess(G, source));
            System.err.println("Max flow         = " + maxFlow);
            return false;
        }
        if (Math.abs(maxFlow - excess(G, target)) > FLOATING_POINT_EPSILON) {
            System.err.println("Excess at sink   = " + excess(G, target));
            System.err.println("Max flow         = " + maxFlow);
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == source || v == target) continue;
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

        if (Math.abs(mincutValue - maxFlow) > FLOATING_POINT_EPSILON) {
            System.err.println("Max flow value = " + maxFlow + ", min cut value = " + mincutValue);
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
        int V = 8;
        int E = 15;
        int source = 0, target = V - 1;
        FlowNetwork G = new FlowNetwork(V, E);
        System.out.println(G);

        // compute maximum flow and minimum cut
        FordFulkerson maxflow = new FordFulkerson(G, source, target);
        System.out.println("Max flow from " + source + " to " + target);
        for (int v = 0; v < G.V(); v++) {
            for (FlowEdge e : G.getAdj(v)) {
                if ((v == e.from()) && e.flow() > 0)
                    System.out.println("   " + e);
            }
        }
        System.out.println("BFS is called:" + count);
        // print min-cut
        System.out.print("Min cut: ");
        for (int v = 0; v < G.V(); v++) {
            if (maxflow.inCut(v)) System.out.print(v + " ");
        }
        System.out.println();

        System.out.println("Max flow value = " + maxflow.value());
    }

    void printFromege(FlowEdge[] flowEdges) {
        if (flowEdges == null || flowEdges.length == 0) return;
        System.out.println("------------ from edges ------------------");
        for (FlowEdge edge : flowEdges) {
            System.out.println(edge);
        }
        System.out.println("--------------------------------------------");
    }

}

