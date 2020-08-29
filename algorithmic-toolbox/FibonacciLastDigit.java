import java.util.*;

public class FibonacciLastDigit {
    private static int getFibonacciLastDigitNaive(int n) {
        // By allocating arrays, you just resolve the number of calls problem of a recursive algorithm
        //however, the problem that the biggest java long is still so small comapring to fibonacci mumbers
        // you should think about how can you avoid the sum of two giant numbers
        // you need to look at the problem and see whether there is a way to avoid the adding of two guge numbers?
        if (n <= 1)
            return n;

        int previous = 0;
        int current  = 1;

        for (int i = 0; i < n - 1; ++i) {
            int tmp_previous = previous;
            previous = current;
            current = (tmp_previous + current)%10;
        }

        return current;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int c = getFibonacciLastDigitNaive(n);
        System.out.println(c);
    }
}

