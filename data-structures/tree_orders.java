import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class tree_orders {
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

    public class TreeOrders {
        int n;
        int[] key;
        int[] left;
        int[] right;
        List<Integer> inOrderList = new ArrayList<>();
        List<Integer> preOrderList = new ArrayList<>();
        List<Integer> postOrderList = new ArrayList<>();

        void read() throws IOException {
            FastScanner in = new FastScanner();
            n = in.nextInt();
            key = new int[n];
            left = new int[n];
            right = new int[n];
            for (int i = 0; i < n; i++) {
                key[i] = in.nextInt();
                left[i] = in.nextInt();
                right[i] = in.nextInt();
            }
        }

        List<Integer> inOrder() {
            return inOrder(0);
        }

        List<Integer> inOrder(int x) {
            if (x == -1) return null;
            inOrder(left[x]);
            inOrderList.add(key[x]);
            inOrder(right[x]);
            return inOrderList;
        }

        List<Integer> preOrder(int x) {
            if (x == -1) return null;
            preOrderList.add(key[x]);
            preOrder(left[x]);
            preOrder(right[x]);
            return preOrderList;
        }

        List<Integer> preOrder() {
            return preOrder(0);
        }

        List<Integer> postOrder() {

            return postOrder(0);
        }

        List<Integer> postOrder(int x) {
            if (x == -1) return null;
            postOrder(left[x]);
            postOrder(right[x]);
            postOrderList.add(key[x]);
            return postOrderList;
        }
    }

    static public void main(String[] args) throws IOException {
        new Thread(null, new Runnable() {
            public void run() {
                try {
                    new tree_orders().run();
                } catch (IOException e) {
                }
            }
        }, "1", 1 << 26).start();
    }

    public void print(List<Integer> x) {
        for (Integer a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        TreeOrders tree = new TreeOrders();
        tree.read();
        print(tree.inOrder());
        print(tree.preOrder());
        print(tree.postOrder());
    }
}
