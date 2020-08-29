import java.util.*;

public class FractionalKnapsack {

    private static double getOptimalValue(int capacity, int[] values, int[] weights) {
        double value = 0;

        int n = values.length, a;
        Double[] profitPerWeight = new Double[n];

        for (int i = 0; i < n; i++) {
            profitPerWeight[i] = (values[i] * 1.0) / weights[i];
        }

        Map<Double, Integer[]> m = new HashMap<>();

        for (int i = 0; i < n; i++) {
            Integer[] temp = new Integer[2];
            temp[0] = values[i];
            temp[1] = weights[i];
            m.put(profitPerWeight[i], temp);
        }

        Map<Double, Integer[]> newMap = new TreeMap<>(Collections.reverseOrder());
        newMap.putAll(m);

        for (Double key : newMap.keySet()) {
            if (capacity == 0) {
                return value;
            }
            a = (newMap.get(key)[1] > capacity) ? capacity : newMap.get(key)[1];

            value = value + a * (newMap.get(key)[0] * 1.0 / newMap.get(key)[1]);
            capacity = capacity - a;
        }

        return value;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int capacity = scanner.nextInt();
        int[] values = new int[n];
        int[] weights = new int[n];
        for (int i = 0; i < n; i++) {
            values[i] = scanner.nextInt();
            weights[i] = scanner.nextInt();
        }
        System.out.println(getOptimalValue(capacity, values, weights));
    }
}