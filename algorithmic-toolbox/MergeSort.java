/**
 * The Merge class provides static methods for sorting an array using a top-down, recursive version of mergesort.
 * This implementation takes Θ(n log n) time to sort any array of length n
 * (assuming comparisons take constant time).
 * It makes between ~ ½ n log2 n and ~ 1 n log2 n compares.
 * This sorting algorithm is stable. It uses Θ(n) extra memory (not including the input array)
 */
public class MergeSort {

    private static void merge(int[] a, int[] aux, int low, int mid, int high) {
        // precondition: a[low .. mid] and a[mid+1 .. high] are sorted sub-arrays
        if (!isSorted(a, low, mid)) return;
        if (!isSorted(a, mid + 1, high)) return;

        if (high + 1 - low >= 0) System.arraycopy(a, low, aux, low, high + 1 - low);

        int i = low;
        int j = mid + 1;

        for (int k = low; k <= high; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > high) a[k] = aux[i++];
            else if (aux[j] < aux[i]) a[k] = aux[j++];
            else a[k] = aux[i++];
        }

        // postcondition: a[low .. high] is sorted
        assert isSorted(a, low, high);
    }

    private static void sort(int[] a, int[] aux, int low, int high) {
        if (high <= low) return;
        int mid = low + (high - low) / 2;
        sort(a, aux, low, mid);
        sort(a, aux, mid + 1, high);
        merge(a, aux, low, mid, high);
    }

    public static void sort(int[] a) {
        int[] aux = new int[a.length];
        sort(a, aux, 0, a.length - 1);
        assert isSorted(a);
    }


    private static boolean isSorted(int[] a) {
        return isSorted(a, 0, a.length - 1);
    }

    private static boolean isSorted(int[] a, int low, int high) {
        for (int i = low + 1; i <= high; i++)
            if (a[i] < a[i - 1]) return false;
        return true;
    }

    // stably merge a[low .. mid] with a[mid+1 .. high] using aux[low .. high]
    private static void merge(int[] a, int[] index, int[] aux, int low, int mid, int high) {

        // copy to aux[]
        if (high + 1 - low >= 0) System.arraycopy(index, low, aux, low, high + 1 - low);

        // merge back to a[]
        int i = low, j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) index[k] = aux[j++];
            else if (j > high) index[k] = aux[i++];
            else if (a[aux[j]] < a[aux[i]]) index[k] = aux[j++];
            else index[k] = aux[i++];
        }
    }

    /**
     * Returns a permutation that gives the elements in the array in ascending order.
     *
     * @param a the array
     * @return a permutation {p[]} such that {a[p[0]]}, {a[p[1]]},
     * ..., {a[p[N-1]]} are in ascending order
     */
    public static int[] indexSort(int[] a) {
        int n = a.length;
        int[] index = new int[n];
        for (int i = 0; i < n; i++)
            index[i] = i;

        int[] aux = new int[n];
        sort(a, index, aux, 0, n - 1);
        return index;
    }

    // mergesort a[low..high] using auxiliary array aux[low..high]
    private static void sort(int[] a, int[] index, int[] aux, int low, int high) {
        if (high <= low) return;
        int mid = low + (high - low) / 2;
        sort(a, index, aux, low, mid);
        sort(a, index, aux, mid + 1, high);
        merge(a, index, aux, low, mid, high);
    }

    private static void show(int[] a) {
        for (int value : a) {
            System.out.print(value + " , ");
        }
    }


    public static void main(String[] args) {
        int[] a = {0, 13, 2, 12, 11, 5, 9, 7, 8, 6, 10, 4, 3, 1};
        MergeSort.sort(a);
        show(a);
    }
}

