import java.util.*;

public class Reachability {

    private static int reach(Map<Integer, List<Integer>> adjacencyList, int x, int y) {

        Stack<Integer> stack = new Stack<>();
        boolean[] isVisited = new boolean[adjacencyList.size()];
        stack.push(x);
        //iterative DFS
        while (!stack.isEmpty()) {
            int current = stack.pop();
            isVisited[current] = true;
            if (current == y) return 1;

            for (Integer vertex : adjacencyList.get(current)) {
                if (!isVisited[vertex]) stack.push(vertex);
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int V = scanner.nextInt();
        int E = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[V];
        for (int i = 0; i < V; i++) {
            adj[i] = new ArrayList<>();
        }
        for (int i = 0; i < E; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adj[x - 1].add(y - 1);
            adj[y - 1].add(x - 1);
        }
        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;
        Map<Integer, List<Integer>> adjacencyList = new HashMap<>();

        for (int i = 0; i < adj.length; i++) {
            adjacencyList.putIfAbsent(i, adj[i]);

        }
        System.out.println(reach(adjacencyList, x, y));
    }
}