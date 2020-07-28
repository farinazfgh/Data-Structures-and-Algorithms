/**
 * Insertion Sort
 * in-place
 * one at a round
 * stable: if keys are equal it keeps the relative order
 * Adaptive: efficient for data sets that are already substantially sorted
 * Efficient for small data sets, much like other quadratic sorting algorithms
 * ---------------------
 * n= 9
 * 1 , 8 , 3 , 6 , 5 , 4 , 7 , 2 , 9 ,
 * number of compares for partially sorted :22
 * number of swaps for partially sorted :14
 *
 * n= 9
 * number of compares for already sorted :8
 * number of swaps for already sorted :0
 *
 * n= 9
 * number of compares for already desc sorted :36
 * number of swaps for already desc sorted :36
 */
public class InsertionSort {
    static int compare = 0, swap = 0;

    public static void sort(int[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = i; j > 0; j--) {
                if
                (less(array[j], array[j - 1])) {
                    exchange(array, j, j - 1);
                } else break;
            }
        }
    }
    private static boolean isSorted(int[] a, int lo, int hi) {
        for (int i = lo + 1; i < hi; i++)
            if (less(a[i], a[i-1])) return false;
        return true;
    }

    private static boolean less(int v, int w) {
        compare++;
        return (v - w) < 0;
    }

    private static void exchange(int[] a, int i, int j) {
        swap++;
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    public static void main(String[] args) {
        int[] array = {1, 8, 3, 6, 5, 4, 7, 2, 9};
        int[] array1 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] array2 = {9, 8, 7, 6, 5, 4, 3, 2, 1};
        System.out.println("n= " + array.length);
        sort(array);
        System.out.println("number of compares for already sorted :" + compare);
        System.out.println("number of swaps for already sorted :" + swap);
    }

    static void printArray(int[] array) {
        for (int value : array) System.out.print(value + " , ");
        System.out.println();
    }
}
