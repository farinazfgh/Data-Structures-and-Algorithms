import java.util.Scanner;

public class CarFueling {
    static int computeMinRefills(int dist, int tank, int[] stops) {
        int[] array = new int[stops.length + 2];
        array[0] = 0;
        System.arraycopy(stops, 0, array, 1, stops.length);
        array[stops.length + 1] = dist;
        int numRefills = 0;
        int currentPosition = 0;
        int n = array.length - 1;
        while (currentPosition < n) {
            int lastRefill = currentPosition;
            while ((currentPosition < n) && (array[currentPosition + 1] - array[lastRefill]) <= tank)
                currentPosition++;
            if (currentPosition == lastRefill) return -1;
            if (currentPosition < n)
                numRefills++;
        }
        return numRefills;
    }

    static int computeMinRefills2(int dist, int tank, int[] stops) {
        if (tank >= dist) return 0;

        int[] array = new int[stops.length + 2];
        array[0] = 0;
        System.arraycopy(stops, 0, array, 1, stops.length);
        array[stops.length + 1] = dist;
        int currentPosition = 1;
        int j = 0;
        int start = 0;
        int numRefills = 0;
        while (j < array.length) {
            while (array[currentPosition] - array[start] <= tank) {
                currentPosition++;
                j++;
            }
            j++;
            numRefills++;
            start = currentPosition - 1;
        }

        if (dist - array[start] > tank) return -1;
        return numRefills;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int dist = scanner.nextInt();
        int tank = scanner.nextInt();
        int n = scanner.nextInt();
        int stops[] = new int[n];
        for (int i = 0; i < n; i++) {
            stops[i] = scanner.nextInt();
        }

        System.out.println(computeMinRefills(dist, tank, stops));
    }
}