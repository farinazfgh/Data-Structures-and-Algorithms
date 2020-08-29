import java.util.Scanner;

public class Knapsack {

    static int optimalWeight(int m, int[] weights) {
        int[] wt = new int[weights.length + 1];
        wt[0] = 0;
        System.arraycopy(weights, 0, wt, 1, weights.length);

        int[] p = {0, 1, 2, 5, 6};
        int n = weights.length;
        int[][] k = new int[weights.length + 1][m + 1];

        for (int i = 0; i <= n; i++)
            for (int w = 0; w <= m; w++) {
                if (i == 0 || w == 0) k[i][w] = 0;
                else if (wt[i] <= w)
                    k[i][w] = Math.max(p[i] + k[i - 1][w - wt[i]], k[i - 1][w]);
                else
                    k[i][w] = k[i - 1][w];

            }
        for (int l = 0; l < (weights.length + 1); l++) {
            System.out.println();
            for (int b = 0; b < (m + 1); b++) {
                System.out.print(k[l][b] + " , ");
            }
        }
        int i = n;
        int optimalW = 0;
        int j = m;
        while (i > 0 && j > 0) {
            if (k[i][j] == k[i - 1][j]) i--;
            else {
                optimalW += wt[i];
                i--;
                j = j - wt[i];
            }

        }
        return optimalW;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int capacity, n;
        capacity = scanner.nextInt();
        n = scanner.nextInt();
        int[] weights = new int[n];
        for (int i = 0; i < n; i++) {
            weights[i] = scanner.nextInt();
        }
        System.out.println(optimal_weight(capacity, weights));
    }

    private static int optimal_weight(int W, int[] w) {
        int w_size = w.length;
        int[][] weights = new int[w.length + 1][W + 1];

        for (int i = 0; i <= w_size; i++) {
            for (int j = 0; j <= W; j++) {
                if (i == 0 || j == 0) {
                    weights[i][j] = 0;
                } else if (w[i - 1] <= j)
                    weights[i][j] = Math.max(w[i - 1] + weights[i - 1][j - w[i - 1]], weights[i - 1][j]);
                else
                    weights[i][j] = weights[i - 1][j];
            }
        }


        return weights[w_size][W];
    }
}

