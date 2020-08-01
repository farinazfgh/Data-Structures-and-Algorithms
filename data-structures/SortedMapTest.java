import java.util.SortedMap;
import java.util.TreeMap;

public class SortedMapTest {
    public static void main(String[] args) {
        SortedMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("John", "(342)113-9878");
        treeMap.put("Richard", "(245)890-9045");
        treeMap.put("Donna", "(205)678-9823");
        treeMap.put("Ken", "(205)678-9823");
        System.out.println("Sorted Map: " + treeMap);
// Get a sub map from Donna (inclusive) to Ken(exclusive)
        SortedMap<String, String> subMap = treeMap.subMap("Donna", "Ken");
        System.out.println("Sorted Submap from Donna to Ken(exclusive): " + subMap);
// Get the first and last keys
        String firstKey = treeMap.firstKey();
        String lastKey = treeMap.lastKey();
        System.out.println("First Key: " + firstKey);
        System.out.println("Last key: " + lastKey);
    }
}