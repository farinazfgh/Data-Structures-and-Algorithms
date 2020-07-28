
/**
 * The { MergeX} class provides static methods for sorting an array using an optimized version of mergesort.
 * In the worst case, this implementation takes O(n log n) time to sort an array of length n
 * (assuming comparisons take constant time).
 * This sorting algorithm is stable.
 * It uses O(n) extra memory (not including the input array).
 */
public class MergeX {
    private static final int CUTOFF = 7;  // cutoff to insertion sort

    // This class should not be instantiated.
    private MergeX() {
    }

    private static void merge(int[] src, int[] dst, int low, int mid, int high) {

        // precondition: src[low .. mid] and src[mid+1 .. high] are sorted subarrays
        assert isSorted(src, low, mid);
        assert isSorted(src, mid + 1, high);

        int i = low, j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) dst[k] = src[j++];
            else if (j > high) dst[k] = src[i++];
            else if (src[j] < src[i]) dst[k] = src[j++];   // to ensure stability
            else dst[k] = src[i++];
        }

        // postcondition: dst[low .. high] is sorted subarray
        assert isSorted(dst, low, high);
    }


    private static void sort(int[] src, int[] dst, int low, int high) {
        // if (high <= low) return;
        if (high <= low + CUTOFF) {
            insertionSort(dst, low, high);
            return;
        }
        int mid = low + (high - low) / 2;
        sort(dst, src, low, mid);
        sort(dst, src, mid + 1, high);

        // if (!less(src[mid+1], src[mid])) {
        //    for (int i = low; i <= high; i++) dst[i] = src[i];
        //    return;
        // }

        // using System.arraycopy() is a bit faster than the above loop
        if (!(src[mid + 1] < src[mid])) {
            System.arraycopy(src, low, dst, low, high - low + 1);
            return;
        }

        merge(src, dst, low, mid, high);
    }

    /**
     * Rearranges the array in ascending order, using the natural order.
     *
     * @param a the array to be sorted
     */
    public static void sort(int[] a) {
        int[] aux = a.clone();
        sort(aux, a, 0, a.length - 1);
        assert isSorted(a);
    }


    /*******************************************************************
     *  Utility methods.
     *******************************************************************/

    // exchange a[i] and a[j]
    private static void exch(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }


    // sort from a[low] to a[high] using insertion sort
    private static void insertionSort(int[] a, int low, int high) {
        for (int i = low; i <= high; i++)
            for (int j = i; j > low && (a[j] < a[j - 1]); j--)
                exch(a, j, j - 1);
    }


    /***************************************************************************
     *  Check if array is sorted - useful for debugging.
     ***************************************************************************/
    private static boolean isSorted(int[] a) {
        return isSorted(a, 0, a.length - 1);
    }

    private static boolean isSorted(int[] a, int low, int high) {
        for (int i = low + 1; i <= high; i++)
            if ((a[i] < a[i - 1])) return false;
        return true;
    }


    // print array to standard output
    private static void show(int[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }

    /**
     * Reads in a sequence of strings from standard input; mergesorts them
     * (using an optimized version of mergesort);
     * and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        int[] a = {};
        MergeX.sort(a);
        show(a);
    }
}


