/******************************************************************************
 *  Sort an array of strings or integers using MSD radix sort.
 *
 *  are
 *  by
 *  sea
 *  seashells
 *  seashells
 *  sells
 *  sells
 *  she
 *  she
 *  shells
 *  shore
 *  surely
 *  the
 *  the
 *
 ******************************************************************************/

/**
 * The MSD class provides static methods for sorting an array of extended ASCII strings or integers using MSD radix sort.
 */
public class MSD {
    private static final int BITS_PER_BYTE = 8;
    private static final int BITS_PER_INT = 32;   // each Java int is 32 bits
    private static final int R = 256;   // extended ASCII alphabet size
    private static final int CUTOFF = 15;   // cutoff to insertion sort small sub-arrays

    // do not instantiate
    private MSD() {
    }

    /**
     * Rearranges the array of extended ASCII strings in ascending order.
     *
     * @param array the array to be sorted
     */
    public static void sort(String[] array) {
        String[] aux = new String[array.length];
        sort(array, 0, array.length - 1, 0, aux);
    }

    // return dth character of s, -1 if d = length of string
    private static int charAt(String s, int d) {
        assert d >= 0 && d <= s.length();
        if (d == s.length()) return -1;
        return s.charAt(d);
    }

    // sort from array[low] to array[high], starting at the dth character
    private static void sort(String[] array, int low, int high, int d, String[] aux) {

        // cutoff to insertion sort for small sub-arrays
        if (high <= low + CUTOFF) {
            insertion(array, low, high, d);
            return;
        }

        // compute frequency counts
        int[] count = new int[R + 2];
        for (int i = low; i <= high; i++) {
            int c = charAt(array[i], d);
            count[c + 2]++;
        }

        // transform counts to indicies
        for (int r = 0; r < R + 1; r++)
            count[r + 1] += count[r];

        // distribute
        for (int i = low; i <= high; i++) {
            int c = charAt(array[i], d);
            aux[count[c + 1]++] = array[i];
        }

        // copy back
        for (int i = low; i <= high; i++)
            array[i] = aux[i - low];


        // recursively sort for each character (excludes sentinel -1)
        for (int r = 0; r < R; r++)
            sort(array, low + count[r], low + count[r + 1] - 1, d + 1, aux);
    }


    // insertion sort array[low..hi], starting at dth character
    private static void insertion(String[] array, int low, int high, int dthCharacter) {
        for (int i = low; i <= high; i++)
            for (int j = i; j > low && less(array[j], array[j - 1], dthCharacter); j--)
                exch(array, j, j - 1);
    }

    // exchange array[i] and array[j]
    private static void exch(String[] array, int i, int j) {
        String temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // is v less than w, starting at character d
    private static boolean less(String v, String w, int d) {
        // assert v.substring(0, d).equals(w.substring(0, d));
        for (int i = d; i < Math.min(v.length(), w.length()); i++) {
            if (v.charAt(i) < w.charAt(i)) return true;
            if (v.charAt(i) > w.charAt(i)) return false;
        }
        return v.length() < w.length();
    }


    /**
     * Rearranges the array of 32-bit integers in ascending order.
     * Currently assumes that the integers are nonnegative.
     *
     * @param array the array to be sorted
     */
    public static void sort(int[] array) {
        int n = array.length;
        int[] aux = new int[n];
        sort(array, 0, n - 1, 0, aux);
    }

    // MSD sort from array[lo] to array[hi], starting at the dth byte
    private static void sort(int[] array, int lo, int hi, int d, int[] aux) {

        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(array, lo, hi, d);
            return;
        }

        // compute frequency counts (need R = 256)
        int[] count = new int[R + 1];
        int mask = R - 1;   // 0xFF;
        int shift = BITS_PER_INT - BITS_PER_BYTE * d - BITS_PER_BYTE;
        for (int i = lo; i <= hi; i++) {
            int c = (array[i] >> shift) & mask;
            count[c + 1]++;
        }

        // transform counts to indicies
        for (int r = 0; r < R; r++)
            count[r + 1] += count[r];

/************* BUGGGY CODE.
 // for most significant byte, 0x80-0xFF comes before 0x00-0x7F
 if (d == 0) {
 int shift1 = count[R] - count[R/2];
 int shift2 = count[R/2];
 for (int r = 0; r < R/2; r++)
 count[r] += shift1;
 for (int r = R/2; r < R; r++)
 count[r] -= shift2;
 }
 ************************************/
        // distribute
        for (int i = lo; i <= hi; i++) {
            int c = (array[i] >> shift) & mask;
            aux[count[c]++] = array[i];
        }

        // copy back
        for (int i = lo; i <= hi; i++)
            array[i] = aux[i - lo];

        // no more bits
        if (d == 4) return;

        // recursively sort for each character
        if (count[0] > 0)
            sort(array, lo, lo + count[0] - 1, d + 1, aux);
        for (int r = 0; r < R; r++)
            if (count[r + 1] > count[r])
                sort(array, lo + count[r], lo + count[r + 1] - 1, d + 1, aux);
    }

    // TODO: insertion sort array[lo..hi], starting at dth character
    private static void insertion(int[] array, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && array[j] < array[j - 1]; j--)
                exch(array, j, j - 1);
    }

    // exchange array[i] and array[j]
    private static void exch(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }


    /**
     * Reads in array sequence of extended ASCII strings from standard input;
     * MSD radix sorts them;
     * and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String[] array = {"she", "sells", "seashells", "by", "the", "sea", "shore", "the", "shells", "she", "sells", "are", "surely", "seashells"};
        sort(array);
        for (String s : array) System.out.print(s + " , ");
        System.out.println();
    }
}

