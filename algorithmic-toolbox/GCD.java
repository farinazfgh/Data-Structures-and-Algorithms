import java.util.Scanner;

public class GCD {
    private static int gcd_naive(int a, int b) {
        int temp = a % b;
        if (temp == 0) return b;

        return gcd_naive(b, temp);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        if ((a - b) > 0) System.out.println(gcd_naive(a, b));
        else
            System.out.println(gcd_naive(b, a));
    }
}
