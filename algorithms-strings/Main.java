public class Main {
    public static void main(String[] args) {
        System.out.println(Long.MAX_VALUE);
        long now = System.currentTimeMillis();
        int count = 0;
        for (long i = 0; i < Long.MAX_VALUE; i++) {
            count++;
        }
        System.out.println(System.currentTimeMillis() - now);
    }
}

