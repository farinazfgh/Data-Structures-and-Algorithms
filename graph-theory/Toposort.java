import java.util.*;

public class Toposort {
    static Map<Integer, List<Integer>> adjacencyList;

    private static List<Integer> toposort() {
        DFSOdrer dfs = new DFSOdrer();
        return dfs.reversePostOrder();
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

        List<Integer> order = toposort();
        for (int x : order) {
            System.out.print((x + 1) + " ");
        }
    }

    static class DFSOdrer {
        private final boolean[] isVisited;          // isVisited[vertex] = has vertex been isVisited in dfs?
        private final int[] pre;                 // pre[vertex]    = preorder  number of vertex
        private final int[] post;                // post[vertex]   = postorder number of vertex
        private final Queue<Integer> preorder;   // vertices in preorder
        private final Queue<Integer> postorder;  // vertices in postorder
        private int preCounter;            // counter or preorder numbering
        private int postCounter;

        public DFSOdrer() {
            pre = new int[adjacencyList.size()];
            post = new int[adjacencyList.size()];
            postorder = new ArrayDeque<>();
            preorder = new ArrayDeque<>();
            isVisited = new boolean[adjacencyList.size()];
            for (int vertex = 0; vertex < adjacencyList.size(); vertex++) {
                if (!isVisited[vertex]) {
                    dfs(vertex);
                }
            }
        }

        private void dfs(int vertex) {
            isVisited[vertex] = true;

            pre[vertex] = preCounter++;
            preorder.add(vertex);

            for (int current : adjacencyList.get(vertex)) {
                if (!isVisited[current]) {
                    dfs(current);
                }
            }

            postorder.add(vertex);
            post[vertex] = postCounter++;
        }

        void printPreOrder() {
            System.out.println("Pre order");

            for (int x : preorder) {
                System.out.print((x + 1) + " ");
            }

        }

        void printPostOrder() {
            System.out.println("Post order");
            for (int x : postorder) {
                System.out.print((x + 1) + " ");
            }
        }

        List<Integer> reversePostOrder() {
            List<Integer> list = new ArrayList<>();
            while (!postorder.isEmpty()) {
                list.add(postorder.remove());
            }
            Collections.reverse(list);
            return list;
        }
    }
}

