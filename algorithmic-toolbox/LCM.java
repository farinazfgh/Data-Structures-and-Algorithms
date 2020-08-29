import java.util.Scanner;

public class LCM {
    private static long lcm_naive(long a, long b) {
        long gcd = gcd_naive(a, b);
        return (a * b) / (gcd);
    }

    private static long gcd_naive(long a, long b) {
        int temp = (int) (a % b);
        if (temp == 0) return b;

        return gcd_naive(b, temp);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long a = scanner.nextLong();
        long b = scanner.nextLong();

        System.out.println(lcm_naive(a, b));
    }
}
