import util.Queue;

import java.util.*;

public class Graph {
    Map<Integer, List<Integer>> adjacentVertices;

    public Graph() {
        this.adjacentVertices = new HashMap<>();
    }

    public void addVertex(int vertex) {
        adjacentVertices.putIfAbsent(vertex, new ArrayList<>());
    }

    public int V() {
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
        boolean[] isVisited = new boolean[V()];
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

    private void bfs(int source) {
        boolean[] isVisited = new boolean[adjacentVertices.size()];

        Queue<Integer> queue = new Queue<Integer>();
        isVisited[source] = true;
        queue.enqueue(source);

        while (!queue.isEmpty()) {
            int vertex = queue.dequeue();
            visit(vertex);
            for (int current : adjacentVertices.get(vertex)) {
                if (!isVisited[current]) {
                    isVisited[current] = true;
                    queue.enqueue(current);
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
