import java.util.*;

public class Graph {
    public List<Integer>getAdj(int v) {
        return adj.get(v);
    }

    Map<Integer, List<Integer>> adj;

    public Graph() {
        this.adj = new HashMap<>();
    }

    public void addVertex(int vertex) {
        adj.putIfAbsent(vertex, new ArrayList<>());
    }

    public int V() {
        return adj.size();
    }

    public void addEdge(int src, int dst) {
        adj.get(src).add(dst);
    }

    private void visit(int vertex) {
        System.out.print(" " + vertex);
    }

    public void dfs(int source) {
        Stack<Integer> stack = new Stack<>();
        boolean[] isVisited = new boolean[V()];
        stack.push(source);
        while (!stack.isEmpty()) {
            int current = stack.pop();
            isVisited[current] = true;
            visit(current);
            for (Integer vertex : adj.get(current)) {
                if (!isVisited[vertex]) stack.push(vertex);
            }
        }
    }

    public void dfsR(int source) {
        boolean[] isVisited = new boolean[V()];
        dfsRecursive(source, isVisited);
    }

    public void dfsRecursive(int current, boolean[] isVisited) {
        isVisited[current] = true;
        visit(current);
        for (Integer vertex : adj.get(current)) {
            if (!isVisited[vertex])
                dfsRecursive(vertex, isVisited);
        }
    }

    public LinkedList<Integer> topologicalSort(int source) {
        LinkedList<Integer> result = new LinkedList<>();
        boolean[] isVisited = new boolean[V()];
        topologicalSortRecursive(source, isVisited, result);
        return result;

    }

    private void topologicalSortRecursive(int current, boolean[] isVisited, LinkedList<Integer> result) {
        isVisited[current] = true;
        for (int vertex : adj.get(current)) {
            if (!isVisited[vertex]) topologicalSortRecursive(vertex, isVisited, result);
        }
        result.addFirst(current);
    }

    private void bfs(int source) {
        boolean[] isVisited = new boolean[V()];

        Queue<Integer> queue = new LinkedList<>();
        isVisited[source] = true;
        queue.offer(source);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            visit(vertex);
            for (int current : adj.get(vertex)) {
                if (!isVisited[current]) {
                    isVisited[current] = true;
                    queue.offer(current);
                }
            }
        }
    }


    public static void main(String[] args) {
        Graph graph = new Graph();
        graph.addVertex(0);
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addVertex(5);
        graph.addVertex(6);
        graph.addEdge(0, 5);
        graph.addEdge(4, 3);
        graph.addEdge(0, 1);
        graph.addEdge(6, 4);
        graph.addEdge(5, 4);
        graph.addEdge(0, 2);
        graph.addEdge(0, 6);
        graph.addEdge(5, 3);
        graph.dfs(0);
        System.out.println();
        graph.bfs(0);
    }
}
