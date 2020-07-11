import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class tree_height {
    class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    public class TreeHeight {
        int n;
        int parent[];
        Map<Integer, List<Integer>> tree = new HashMap<>();

        void read() throws IOException {
            FastScanner in = new FastScanner();
            n = in.nextInt();
            parent = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = in.nextInt();
            }
            for (int i = 0; i < parent.length; i++) tree.put(i, new ArrayList<>());
        }

        int computeHeight() {
            int maxHeight = 0;
            int root = -1;
            for (int child = 0; child < parent.length; child++) {
                if (parent[child] == -1) {
                    root = child;
                    continue;
                }
                tree.get(parent[child]).add(child);
            }
            int[] levels = bfs(root);

            return Arrays.stream(levels).max().getAsInt();
        }

        private int[] bfs(int source) {
            boolean[] isVisited = new boolean[parent.length];
            int[] level = new int[parent.length];
            Queue<Integer> queue = new ArrayDeque<>();
            isVisited[source] = true;
            level[source] = 1;
            queue.add(source);

            while (!queue.isEmpty()) {
                int vertex = queue.remove();
                for (int current : tree.get(vertex)) {
                    if (!isVisited[current]) {
                        isVisited[current] = true;
                        queue.add(current);
                        level[current] = level[vertex] + 1;
                    }
                }
            }
            return level;
        }
    }

    static public void main(String[] args) throws IOException {
        new Thread(null, new Runnable() {
            public void run() {
                try {
                    new tree_height().run();
                } catch (IOException e) {
                }
            }
        }, "1", 1 << 26).start();
    }

    public void run() throws IOException {
        TreeHeight tree = new TreeHeight();
        tree.read();
        System.out.println(tree.computeHeight());
    }
}
