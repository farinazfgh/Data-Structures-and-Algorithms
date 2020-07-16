import java.util.*;

public class Dijkstra {


    static Map<Integer, List<DirectedWeightedEdge>> adjacencyEdgesList;
    static PriorityQueue<Integer> priorityQueue; // priority queue of vertices
    static int[] distanceFromSource; // distTo[v] = distance of shortest s->v path

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[n];
        ArrayList<Integer>[] cost = (ArrayList<Integer>[]) new ArrayList[n];
        adjacencyEdgesList = new HashMap<>();
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y, w;
            x = scanner.nextInt();
            y = scanner.nextInt();
            w = scanner.nextInt();
            adj[x - 1].add(y - 1);
            cost[x - 1].add(w);

            DirectedWeightedEdge edge = new DirectedWeightedEdge(x, y, w);
            List<DirectedWeightedEdge> list = adjacencyEdgesList.get(x);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(edge);
            adjacencyEdgesList.put(x, list);
        }
        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;
        System.out.println(distance(adj, cost, x, y));
    }


    static class DirectedWeightedEdge {
        private final int from;
        private final int to;
        private final int weight;

        public DirectedWeightedEdge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }

        public int getWeight() {
            return weight;
        }

        @Override
        public String toString() {
            return "{" + from + "-> " + to + ", weight:" + weight + '}';
        }

    }

    private static void relax(DirectedWeightedEdge e, int from) {
        int to = e.getTo();
        //this means the old path from source to to is bigger than the new edge and new parent vertex that we are visiting
        // so we need to update the old path we the new shorter path
        if (distanceFromSource[to] > distanceFromSource[from] + e.getWeight()) {
            distanceFromSource[to] = distanceFromSource[from] + e.getWeight();
            if (priorityQueue.contains(to)) {
                priorityQueue.poll();//throw away the longer distance from source update it with shorter one
                priorityQueue.offer(to);//decrease priority
            } else {
                priorityQueue.offer(to);
            }
        }
    }

    static class Node {
        public int number;
        int distance;
        public Node(int s, int i) {
            this.number=s;
            this.distance=i;
        }
    }

    private static int distance(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s, int t) {
        PriorityQueue<Node> q = new PriorityQueue<>(adj.length, Comparator.comparingInt(a -> a.distance));
        Node start = new Node(s, 0);
        q.offer(start);
        int[] dist = new int[adj.length];
        for (int i = 0; i < adj.length; i++) dist[i] = -1;
        dist[s] = 0;
        while (!q.isEmpty()) {
            Node x = q.poll();
            if (x.number == t)
                return x.distance;
            Iterator<Integer> costIt = cost[x.number].iterator();
            for (int y : adj[x.number]) {
                int d = costIt.next();
                if (dist[y] == -1 || dist[y] > dist[x.number] + d) {
                    dist[y] = dist[x.number] + d;
                    Node newNode = new Node(y, dist[y]);
                    q.offer(newNode);
                }
            }
        }
        return -1;
    }

    private static int distance_(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s, int t) {
        priorityQueue = new PriorityQueue<>();
        int source = s + 1;
        int destination = t + 1;
        priorityQueue.offer(source);
        distanceFromSource = new int[adj.length + 1];
        Arrays.fill(distanceFromSource, Integer.MAX_VALUE);
        distanceFromSource[source] = 0;
        while (!priorityQueue.isEmpty()) {
            Integer v = priorityQueue.poll();
            List<DirectedWeightedEdge> edges = adjacencyEdgesList.get(v);
            if (edges != null) for (DirectedWeightedEdge e : edges) {
                relax(e, v);
            }
        }
        if (distanceFromSource[destination] == Integer.MAX_VALUE) return -1;
        else return distanceFromSource[destination];
    }
}
