/******************************************************************************
 *  LSD radix sort
 *
 *    - Sort a String[] array of n extended ASCII strings (R = 256), each of length w.
 *
 *    - Sort an int[] array of n 32-bit integers, treating each integer as
 *      a sequence of w = 4 bytes (R = 256).
 *
 *  Uses extra space proportional to n + R.
 *  all
 *  bad
 *  bed
 *  bug
 *  dad
 *  ...
 *  yes
 *  yet
 *  zoo
 *
 ******************************************************************************/

/**
 * The LSD class provides static methods for sorting an array of w-character strings or 32-bit integers using LSD radix sort.
 */
public class LSD {
    private static final int BITS_PER_BYTE = 8;

    // do not instantiate
    private LSD() {
    }

    public static void sort(String[] array, int keyLength) {
        int n = array.length;
        int Radix = 256;   // extend ASCII alphabet size possible characters
        String[] aux = new String[n];

        for (int d = keyLength - 1; d >= 0; d--) {
            System.out.println("charAt(" + d + ")");
            //keyLength => d= 2,1,0
            // sort by key-indexed counting on dth character

            // compute frequency counts
            int[] count = new int[Radix + 1];
            for (int i = 0; i < n; i++) {
                count[array[i].charAt(d) + 1]++;
            }

            // compute cumulates
            for (int r = 0; r < Radix; r++) {
                count[r + 1] += count[r];
            }

            // move data
            for (int i = 0; i < n; i++) {
                aux[count[array[i].charAt(d)]++] = array[i];
            }

            // copy back
            printArray(aux);
            System.arraycopy(aux, 0, array, 0, n);
        }
    }

    static void printArray(String[] a) {
        for (String value : a) {
            System.out.print(value + " , ");
        }
        System.out.println();
    }

    /**
     * Rearranges the array of 32-bit integers in ascending order.
     * This is about 2-3x faster than Arrays.sort().
     *
     * @param a the array to be sorted
     */
    public static void sort(int[] a) {
        final int BITS = 32;                 // each int is 32 bits
        final int R = 1 << BITS_PER_BYTE;    // each bytes is between 0 and 255
        final int MASK = R - 1;              // 0xFF
        final int w = BITS / BITS_PER_BYTE;  // each int is 4 bytes

        int n = a.length;
        int[] aux = new int[n];

        for (int d = 0; d < w; d++) {

            // compute frequency counts
            int[] count = new int[R + 1];
            for (int i = 0; i < n; i++) {
                int c = (a[i] >> BITS_PER_BYTE * d) & MASK;
                count[c + 1]++;
            }

            // compute cumulates
            for (int r = 0; r < R; r++)
                count[r + 1] += count[r];

            // for most significant byte, 0x80-0xFF comes before 0x00-0x7F
            if (d == w - 1) {
                int shift1 = count[R] - count[R / 2];
                int shift2 = count[R / 2];
                for (int r = 0; r < R / 2; r++)
                    count[r] += shift1;
                for (int r = R / 2; r < R; r++)
                    count[r] -= shift2;
            }

            // move data
            for (int i = 0; i < n; i++) {
                int c = (a[i] >> BITS_PER_BYTE * d) & MASK;
                aux[count[c]++] = a[i];
            }

            // copy back
            for (int i = 0; i < n; i++)
                a[i] = aux[i];
        }
    }

    /**
     * Reads in a sequence of fixed-length strings from standard input;
     * LSD radix sorts them;
     * and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        System.out.println();
        String[] array = {
                "bed", "bug", "dad", "yes", "zoo",
                "now", "for", "tip", "ilk", "dim",
                "tag", "jot", "sob", "nob", "sky",
                "hut", "men", "egg", "few", "jay",
                "owl", "joy", "rap", "gig", "wee",
                "was", "wad", "fee", "tap", "tar",
                "dug", "jam", "all", "bad", "yet",
        };

        int keyLength = checkAllElementsHaveEqualLength(array);


        // sort the strings
        sort(array, keyLength);

        // print results
        for (String s : array) System.out.print(s + " , ");
        System.out.println();
    }

    private static int checkAllElementsHaveEqualLength(String[] a) {
        // check that strings have equal length
        int w = a[0].length();
        for (String value : a) assert value.length() == w : "Strings must have fixed length";
        return w;
    }
}

