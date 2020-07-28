import util.StdOut;
import util.StdRandom;

public class QuickSort {

    private QuickSort() {
    }

    public static void sort(Comparable[] a) {
        StdRandom.shuffle(a);
        printArray(a);
        sort(a, 0, a.length - 1);
        assert isSorted(a);
    }

    // quicksort the subarray from a[low] to a[high]
    private static void sort(Comparable[] a, int low, int high) {
        if (high <= low) return;
        int j = partition(a, low, high);
        sort(a, low, j - 1);
        sort(a, j + 1, high);
        assert isSorted(a, low, high);
    }

    // partition the subarray a[low..high] so that a[low..j-1] <= a[j] <= a[j+1..high]
    // and return the index j.
    private static int partition(Comparable[] a, int low, int high) {
        int i = low;
        int j = high + 1;
        Comparable v = a[low];
        while (true) {

            // find item on low to swap
            while (less(a[++i], v)) {
                if (i == high) break;
            }

            // find item on high to swap
            while (less(v, a[--j])) {
                if (j == low) break;      // redundant since a[low] acts as sentinel
            }

            // check if pointers cross
            if (i >= j) break;

            exchange(a, i, j);
        }

        exchange(a, low, j);

        // now, a[low .. j-1] <= a[j] <= a[j+1 .. high]
        return j;
    }

    /**
     * Rearranges the array so that { a[k]} contains the kth smallest key;
     * { a[0]} through { a[k-1]} are less than (or equal to) { a[k]}; and
     * { a[k+1]} through { a[n-1]} are greater than (or equal to) { a[k]}.
     *
     * @param a the array
     * @param k the rank of the key
     * @return the key of rank { k}
     * @throws IllegalArgumentException unless { 0 <= k < a.length}
     */
    public static Comparable select(Comparable[] a, int k) {
        if (k < 0 || k >= a.length) {
            throw new IllegalArgumentException("index is not between 0 and " + a.length + ": " + k);
        }
        StdRandom.shuffle(a);
        int low = 0, high = a.length - 1;
        while (high > low) {
            int i = partition(a, low, high);
            if (i > k) high = i - 1;
            else if (i < k) low = i + 1;
            else return a[i];
        }
        return a[low];
    }


    /***************************************************************************
     *  Helper sorting functions.
     ***************************************************************************/

    // is v < w ?
    private static boolean less(Comparable v, Comparable w) {
        if (v == w) return false;   // optimization when reference equals
        return v.compareTo(w) < 0;
    }

    // exchange a[i] and a[j]
    private static void exchange(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }


    /***************************************************************************
     *  Check if array is sorted - useful for debugging.
     ***************************************************************************/
    private static boolean isSorted(Comparable[] a) {
        return isSorted(a, 0, a.length - 1);
    }

    private static boolean isSorted(Comparable[] a, int low, int high) {
        for (int i = low + 1; i <= high; i++)
            if (less(a[i], a[i - 1])) return false;
        return true;
    }


    // print array to standard output
    private static void printArray(Comparable[] a) {
        for (Comparable comparable : a) {
            StdOut.println(comparable);
        }
    }

    /**
     * Reads in a sequence of strings from standard input; quicksorts them;
     * and prints them to standard output in ascending order.
     * Shuffles the array and then prints the strings again to
     * standard output, but this time, using the select method.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        System.out.println();

        // String[] a = StdIn.readAllStrings();
        String[] a = {"bed", "bug", "dad", "yes", "zoo"};
        QuickSort.sort(a);
        printArray(a);
        assert isSorted(a);

        // shuffle
        StdRandom.shuffle(a);

        // display results again using select
        StdOut.println();
        for (int i = 0; i < a.length; i++) {
            String ith = (String) QuickSort.select(a, i);
            StdOut.println(ith);
        }
    }

}

