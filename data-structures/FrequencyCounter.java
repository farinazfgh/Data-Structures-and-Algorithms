import java.util.NoSuchElementException;
import java.util.Scanner;

/******************************************************************************
 *  Read in a list of words from standard input and print out
 *  the most frequently occurring word that has length greater than
 *  a given threshold.
 ******************************************************************************/
public class FrequencyCounter {

    private FrequencyCounter() {
    }

    public static void main(String[] args) {
        int distinct = 0, words = 0;
        int minlen = Integer.parseInt(args[0]);
        SymbolTable<String, Integer> st = new SymbolTable<>();
        Scanner scanner = new Scanner(new java.io.BufferedInputStream(System.in), "UTF-8");


        while (!isEmpty(scanner)) {
            String key = readString(scanner);
            if (key.length() < minlen) continue;
            words++;
            if (st.contains(key)) {
                st.put(key, st.get(key) + 1);
            } else {
                st.put(key, 1);
                distinct++;
            }
        }

        // find a key with the highest frequency count
        String max = "";
        st.put(max, 0);
        for (String word : st.keys()) {
            if (st.get(word) > st.get(max))
                max = word;
        }

        System.out.println(max + " " + st.get(max));
        System.out.println("distinct = " + distinct);
        System.out.println("words    = " + words);
    }

    public static String readString(Scanner scanner) {

        try {
            return scanner.next();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("attempts to read a 'String' value from standard input, "
                    + "but no more tokens are available");
        }
    }

    public static boolean isEmpty(Scanner scanner) {
        return !scanner.hasNext();
    }
}