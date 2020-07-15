
/******************************************************************************
 *  Weighted quick-union (without path compression).
 ******************************************************************************/

import util.StdIn;
import util.StdOut;

/**
 * This implementation uses  weighted quick union by size (without path compression).
 * The constructor takes O(n), where  n  is the number of elements.
 * The  union  and  find operations  take O(log n) time in the worst case. which is the heigh of the tree
 * The  count  operation takes O(1) time.
 */
public class WeightedQuickUnionUF {
    private int[] parent;   // parent[i] = parent of i
    private int[] subTreeSize;     // size[i] = number of elements in subtree rooted at i
    private int numberOfComponents;      // number of components

    public WeightedQuickUnionUF(int n) {
        numberOfComponents = n;
        parent = new int[n];
        subTreeSize = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            subTreeSize[i] = 1;
        }
    }


    public int getNumberOfComponents() {
        return numberOfComponents;
    }

    /**
     * get root of p
     */
    public int find(int p) {
        validate(p);
        while (p != parent[p])
            p = parent[p];
        return p;
    }

    /**
     * Returns true if the two elements are in the same set.
     */
    @Deprecated
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    // validate that p is a valid index
    private void validate(int p) {
        int n = parent.length;
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
        }
    }

    /**
     * Merges the set containing element p with the set containing element q.
     */
    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;

        // make smaller root point to larger one; larger subtree becomes root
        if (subTreeSize[rootP] < subTreeSize[rootQ]) {
            parent[rootP] = rootQ;
            subTreeSize[rootQ] += subTreeSize[rootP];
        } else {
            parent[rootQ] = rootP;
            subTreeSize[rootP] += subTreeSize[rootQ];
        }
        numberOfComponents--;
    }


    public static void main(String[] args) {
        int n = StdIn.readInt();
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(n);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (uf.find(p) == uf.find(q)) continue;
            uf.union(p, q);
            StdOut.println(p + " " + q);
        }
        StdOut.println(uf.getNumberOfComponents() + " components");
    }
}
