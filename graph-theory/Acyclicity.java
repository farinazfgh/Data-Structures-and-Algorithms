import java.util.*;

public class Acyclicity {
    private static int acyclic(Map<Integer, List<Integer>> adjacencyList) {
        int round = 0;
        if (adjacencyList.isEmpty()) return 0;
        boolean[] isVisited = new boolean[adjacencyList.size()];
        boolean[] isRecursiveStack = new boolean[adjacencyList.size()];
        Set<Integer> vertices = adjacencyList.keySet();
        Stack<Integer> stack = new Stack<>();
        for (Integer source : vertices) {
            stack.push(source);
            while (!stack.isEmpty()) {
                int current = stack.pop();
                isVisited[current] = true;
                if (current == source && round != 0) return 1;
                for (Integer vertex : adjacencyList.get(current)) {
                    if (!isVisited[vertex]) stack.push(vertex);
                }
                round++;
            }
            round = 0;
        }
        return 0;
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
        Map<Integer, List<Integer>> adjacencyList = new HashMap<>();

        for (int i = 0; i < adj.length; i++) {
            adjacencyList.putIfAbsent(i, adj[i]);

        }

        System.out.println(acyclic(adjacencyList));
    }
}

