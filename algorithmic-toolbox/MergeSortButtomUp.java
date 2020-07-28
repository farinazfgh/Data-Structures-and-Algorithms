/**
 * The { MergeBU} class provides static methods for sorting an array using bottom-up mergesort.
 * It is non-recursive.
 * This implementation takes  Θ(n log n)  time
 * to sort any array of length n (assuming comparisons take constant time).
 * It makes between ~ ½ n log2 n and ~ 1 n log2 n compares.
 * This sorting algorithm is stable.
 * It uses O(n) extra memory (not including the input array).
 */
public class MergeSortButtomUp {

    private MergeSortButtomUp() {
    }

    // stably merge a[low..mid] with a[mid+1..high] using aux[low..high]
    private static void merge(int[] a, int[] aux, int low, int mid, int high) {

        // copy to aux[]
        if (high + 1 - low >= 0) System.arraycopy(a, low, aux, low, high + 1 - low);

        // merge back to a[]
        int i = low, j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) a[k] = aux[j++];  // this copying is unneccessary
            else if (j > high) a[k] = aux[i++];
            else if (aux[j] < aux[i]) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
    }

    public static void sort(int[] a) {
        int n = a.length;
        int[] aux = new int[n];
        for (int i = 1; i < n; i *= 2) {
            for (int low = 0; low < n - i; low += i + i) {
                int mid = low + i - 1;
                int high = Math.min(low + i + i - 1, n - 1);
                merge(a, aux, low, mid, high);
            }
        }
        assert isSorted(a);
    }

    private static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++)
            if (a[i] < a[i - 1]) return false;
        return true;
    }

    private static void show(int[] a) {
        for (int value : a) {
            System.out.print(value + " , ");
        }
    }

    public static void main(String[] args) {
        int[] a = {0, 13, 2, 12, 11, 5, 9, 7, 8, 6, 10, 4, 3, 1};
        MergeSortButtomUp.sort(a);
        show(a);
    }
}
