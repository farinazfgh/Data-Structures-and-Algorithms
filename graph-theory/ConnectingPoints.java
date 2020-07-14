
import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class ConnectingPoints {
    private static double minimumDistance(Point[] points) {
        double mstWeight = 0.0;
        Queue<Edge> mstQueueEdges = new ArrayDeque<>();
        int n = points.length;
        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>();
        for (int i = 0; i < points.length; i++)
            for (int j = 0; j < points.length && i != j; j++) {
                Edge edge = new Edge(points[i], points[j]);
                priorityQueue.offer(edge);
            }
        UF uf = new UF(n);
        while (!priorityQueue.isEmpty() && mstQueueEdges.size() < n - 1) {
            Edge e = priorityQueue.poll();
            int v = e.either();
            int w = e.edgeOtherVertex(v);
            if (uf.find(v) != uf.find(w)) { // they dont belong to the same set and v-w does not create a cycle
                uf.union(v, w);
                mstQueueEdges.offer(e);
                mstWeight += e.weight;
            }
        }
        return mstWeight;
    }

    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Point[] points = new Point[n];

        int[] x = new int[n];
        int[] y = new int[n];

        for (int i = 0; i < n; i++) {
            x[i] = scanner.nextInt();
            y[i] = scanner.nextInt();
            points[i] = new Point(x[i], y[i], i);
        }
        System.out.println(minimumDistance(points));
    }

    static class Point {
        final int x;
        final int y;
        final int label;

        public Point(int x, int y, int label) {
            this.x = x;
            this.y = y;
            this.label = label;
        }

        @Override
        public String toString() {
            return label + ": (" + x + "," + y + ')';
        }
    }

    static class Edge implements Comparable<Edge> {
        final Point x;
        final Point y;
        final double weight;

        public Edge(Point x, Point y) {
            this.x = x;
            this.y = y;
            weight = Math.sqrt(Math.pow((x.x - y.x), 2) + Math.pow((x.y - y.y), 2));
        }

        public int either() {
            return x.label;
        }

        public int edgeOtherVertex(int vertex) {
            if (vertex == x.label) return y.label;
            else if (vertex == y.label) return x.label;
            else throw new IllegalArgumentException("Illegal endpoint");
        }

        @Override
        public int compareTo(Edge edge) {
            if (edge.weight <= weight) return 1;
            return -1;
        }

        @Override
        public String toString() {
            return x + " -- " + y + " , weight: " + weight;
        }
    }

    static class UF {
        int[] parent;
        byte[] rank;
        int count;

        public UF(int n) {
            this.count = n;
            parent = new int[count];
            rank = new byte[count];
            for (int i = 0; i < count; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        public int find(int p) {
            //finds root of the rooted tree (set)
            while (p != parent[p]) {
                parent[p] = parent[parent[p]];
                p = parent[p];
            }
            return p;
        }

        public void union(int p, int q) {
            int rootP = find(p);
            int rootQ = find(q);

            if (rootP == rootQ) return;//already in the same set

            // make root of smaller rank point to root of larger rank
            if (rank[rootP] < rank[rootQ]) {
                parent[rootP] = rootQ;
            } else if (rank[rootP] > rank[rootQ]) {
                parent[rootQ] = rootP;
            } else {//rootp=rootq means they have the same tree height, still we need to: height=height+1
                parent[rootQ] = rootP;
                rank[rootP]++;
            }
            count--;
        }
    }
}

