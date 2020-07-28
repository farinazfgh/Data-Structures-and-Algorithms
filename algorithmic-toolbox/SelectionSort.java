/**
 * n= 9
 * number of compares for already sorted :36
 * number of swaps for already sorted :9
 * n= 9
 * number of compares for partially sorted :36
 * number of swaps for partially sorted :9
 *
 * n= 9
 * number of compares for already desc sorted :36
 * number of swaps for already desc sorted :9
 *
 * Selection sort almost always outperforms bubble sort and gnome sort.
 *
 * Can be useful when memory write is a costly operation.
 *
 * While selection sort is preferable to insertion sort in terms of number of writes (Θ(n) swaps versus Ο(n^2) swaps).
 *
 * It almost always far exceeds the number of writes that cycle sort makes, as cycle sort is theoretically optimal in the number of writes.
 *
 * This can be important if writes are significantly more expensive than reads, such as with EEPROM or Flash memory, where every write lessens the lifespan of the memory.
 */
public class SelectionSort {
    static int compare = 0, swap = 0;

    private SelectionSort() {
    }

    public static void sort(int[] a) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (less(a[j], a[min])) min = j;

            }
            exchange(a, i, min);
            assert isSorted(a, 0, i);
        }
        assert isSorted(a);
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


    private static boolean isSorted(int[] a) {
        return isSorted(a, 0, a.length - 1);
    }


    private static boolean isSorted(int[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++)
            if (less(a[i], a[i - 1])) return false;
        return true;
    }


    private static void printArray(int[] a) {
        for (int value : a) {
            System.out.print(value + " , ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] array = {1, 8, 3, 6, 5, 4, 7, 2, 9};
        int[] array1 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] array2 = {9, 8, 7, 6, 5, 4, 3, 2, 1};
        System.out.println("n= " + array1.length);
        sort(array1);
        System.out.println("number of compares for already sorted :" + compare);
        System.out.println("number of swaps for already sorted :" + swap);
    }
}

