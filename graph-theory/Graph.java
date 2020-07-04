import java.util.*;

public class Graph {
    Map<Integer, List<Integer>> adjacentVertices;

    public Graph() {
        this.adjacentVertices = new HashMap<>();
    }

    public void addVertex(int vertex) {
        adjacentVertices.putIfAbsent(vertex, new ArrayList<>());
    }

    public int getNumberOfVerrices() {
        return adjacentVertices.size();
    }

    public void addEdge(int src, int dst) {
        adjacentVertices.get(src).add(dst);
    }

    private void visit(int vertex) {
        System.out.print(" " + vertex);
    }

    public void dfs(int source) {
        Stack<Integer> stack = new Stack<>();
        boolean[] isVisited = new boolean[getNumberOfVerrices()];
        stack.push(source);
        while (!stack.isEmpty()) {
            int current = stack.pop();
            isVisited[current] = true;
            visit(current);
            for (Integer vertex : adjacentVertices.get(current)) {
                if (!isVisited[vertex]) stack.push(vertex);
            }
        }
    }

    public void dfsR(int source) {
        boolean[] isVisited = new boolean[adjacentVertices.size()];
        dfsRecursive(source, isVisited);
    }

    public void dfsRecursive(int current, boolean[] isVisited) {
        isVisited[current] = true;
        visit(current);
        for (Integer vertex : adjacentVertices.get(current)) {
            if (!isVisited[vertex])
                dfsRecursive(vertex, isVisited);
        }
    }

    public LinkedList<Integer> topologicalSort(int source) {
        LinkedList<Integer> result = new LinkedList<>();
        boolean[] isVisited = new boolean[adjacentVertices.size()];
        topologicalSortRecursive(source, isVisited, result);
        return result;

    }

    private void topologicalSortRecursive(int current, boolean[] isVisited, LinkedList<Integer> result) {
        isVisited[current] = true;
        for (int vertex : adjacentVertices.get(current)) {
            if (!isVisited[vertex]) topologicalSortRecursive(vertex, isVisited, result);
        }
        result.addFirst(current);
    }
}
