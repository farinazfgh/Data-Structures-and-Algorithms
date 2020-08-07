import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class HashSubstring {

    private static FastScanner in;
    private static PrintWriter out;
    private static String pattern;
    private static int patternLength;
    private static final long Q = longRandomPrime();
    private final static int R = 256;          // radix

    public static void main(String[] args) throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        printOccurrences(getOccurrences(readInput()));
        out.close();
    }

    private static Data readInput() throws IOException {
        String pattern = in.next();
        String text = in.next();
        return new Data(pattern, text);
    }

    private static void printOccurrences(List<Integer> ans) throws IOException {
        for (Integer cur : ans) {
            out.print(cur);
            out.print(" ");
        }
    }

    private static long hash(String key, int m) {
        long h = 0;
        for (int j = 0; j < m; j++)
            h = (R * h + key.charAt(j)) % Q;
        return h;
    }

    // Las Vegas version: does pat[] match txt[i..i-m+1] ?
    private static boolean check(String txt, int i) {
        for (int j = 0; j < patternLength; j++)
            if (pattern.charAt(j) != txt.charAt(i + j))
                return false;
        return true;
    }

    // a random 31-bit prime
    private static long longRandomPrime() {
        BigInteger prime = BigInteger.probablePrime(31, new Random());
        return prime.longValue();
    }

    private static List<Integer> getOccurrences(Data input) {
        String text = input.text;

        pattern = input.pattern;      // save pattern (needed only for Las Vegas)

        patternLength = pattern.length();
        // precompute R^(m-1) % q for use in removing leading digit
        long RM = 1;
        for (int i = 1; i <= patternLength - 1; i++)
            RM = (R * RM) % Q;
        long patternHashValue = hash(pattern, patternLength);

        List<Integer> results = new ArrayList<>();
        if (text.length() < patternLength) return Collections.emptyList();
        long txtHash = hash(text, patternLength);

        // check for match at offset 0
        if ((patternHashValue == txtHash) && check(text, 0)) {
            results.add(0);
        }

        // check for hash match; if hash match, check for exact match
        for (int i = patternLength; i < text.length(); i++) {
            // Remove leading digit, add trailing digit, check for match.
            txtHash = (txtHash + Q - RM * text.charAt(i - patternLength) % Q) % Q;
            txtHash = (txtHash * R + text.charAt(i)) % Q;

            // match
            int offset = i - patternLength + 1;
            if ((patternHashValue == txtHash) && check(text, offset)) {
                results.add(offset);
            }
        }

        return results;

    }

    static class Data {
        String pattern;
        String text;

        public Data(String pattern, String text) {
            this.pattern = pattern;
            this.text = text;
        }
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}

