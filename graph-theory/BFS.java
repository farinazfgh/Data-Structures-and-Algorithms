
import java.util.*;

public class BFS {
    static Map<Integer, List<Integer>> adjacentVertices;
    static final int INFINITY = Integer.MAX_VALUE;
    static boolean[] isVisited;  // isVisited[vertex] = is there an source-vertex path
    static int[] fromEdge;      // fromEdge[vertex] = previous edge on shortest source-vertex path
    static int[] distanceTo;      // distanceTo[vertex] = number of edges shortest source-vertex path

    public BFS() {

    }

    private static int distance(int source, int t) {
        bfs(source);
        if (hasPathTo(t)) return distTo(t);
        else return -1;
    }

    static boolean hasPathTo(int vertex) {
        return isVisited[vertex];
    }

    static int distTo(int vertex) {
        return distanceTo[vertex];
    }

    private static void bfs(int source) {
        Queue<Integer> q = new ArrayDeque<>();
        isVisited[source] = true;
        distanceTo[source] = 0;
        q.add(source);
        while (!q.isEmpty()) {
            int vertex = q.remove();
            for (int current : adjacentVertices.get(vertex)) {
                if (!isVisited[current]) {
                    fromEdge[current] = vertex;
                    distanceTo[current] = distanceTo[vertex] + 1;
                    isVisited[current] = true;
                    q.add(current);
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adj[x - 1].add(y - 1);
            adj[y - 1].add(x - 1);
        }
        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;
        adjacentVertices = new HashMap<>();

        for (int i = 0; i < adj.length; i++) {
            adjacentVertices.putIfAbsent(i, adj[i]);
        }
        isVisited = new boolean[adjacentVertices.size()];
        distanceTo = new int[adjacentVertices.size()];
        fromEdge = new int[adjacentVertices.size()];
        Arrays.fill(distanceTo, INFINITY);
        System.out.println(distance(x, y));
    }
}

