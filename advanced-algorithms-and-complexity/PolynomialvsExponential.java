public class PolynomialvsExponential {
    static long fibonacci(long n) {
        if (n <= 2) return 1;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    static long factorial(long n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }

    public static void main(String[] args) {
        System.out.println(fibonacci(45));
        System.out.println(factorial(30));
    }
}
