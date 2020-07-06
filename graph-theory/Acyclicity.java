import java.util.*;

public class Acyclicity {
    static Map<Integer, List<Integer>> adjacencyList;
    static Stack<Integer> cycle = null;

    private static int acyclic() {
        if (adjacencyList == null || adjacencyList.size() == 0) return 0;
        boolean[] isVisited = new boolean[adjacencyList.size()];
        boolean[] isOnStack = new boolean[adjacencyList.size()];
        Set<Integer> vertices = adjacencyList.keySet();
        for (Integer vertex : vertices) {
            if (!isVisited[vertex] && cycle == null) {
                isVisited[vertex] = true;
                dfs(vertex, isVisited, isOnStack);
            }

        }
        if (cycle == null) return 0;
        else return 1;
    }

    static public void dfs(int vertex, boolean[] isVisited, boolean[] isOnStack) {
        isVisited[vertex] = true;
        isOnStack[vertex] = true;
        for (Integer current : adjacencyList.get(vertex)) {
            if (cycle != null) return;
            else if (!isVisited[current])
                dfs(current, isVisited, isOnStack);
            else if (isOnStack[current]) { // it is already visited; is on stack still-> cycle detected
                cycle = new Stack<>();
            }
        }
        isOnStack[vertex] = false;
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
        }
        adjacencyList = new HashMap<>();

        for (int i = 0; i < adj.length; i++) {
            adjacencyList.putIfAbsent(i, adj[i]);
        }

        System.out.println(acyclic());
    }
}

