public class CountSort {
    //O(3n) = O(n)

    static int findMax(int[] A) {
        int max = Integer.MIN_VALUE;
        int i;
        for (i = 0; i < A.length; i++) {
            if (A[i] > max)
                max = A[i];
        }
        return max;
    }

    static void countSort(int[] A) {
        int max = findMax(A);
        int[] C = new int[max + 1];

        for (int i = 0; i < A.length; i++) {
            C[A[i]]++;
        }
        for (int i = 0, j = 0; j < C.length; j++) {
            if (C[j] > 0) {
                A[i++] = j;
                C[j]--;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println();
        int[] A = {11, 13, 7, 12, 16, 9, 24, 5, 10, 3};
        int i;
        countSort(A);
        for (i = 0; i < 10; i++)
            System.out.printf("%d ", A[i]);
    }
}
