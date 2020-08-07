import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The {RabinKarp} class finds the first occurrence of a pattern string
 * in a text string.
 */
public class RabinKarp {
    private final String pattern;      // the pattern  // needed only for Las Vegas
    private final long patternHashValue;    // pattern hash value
    private final int patternLength;           // pattern length
    private final long Q;          // a large prime, small enough to avoid long overflow
    private final int R;           // radix
    private long RM;         // R^(M-1) % Q


    public RabinKarp(String pattern) {
        this.pattern = pattern;      // save pattern (needed only for Las Vegas)
        R = 256;
        patternLength = pattern.length();
        Q = longRandomPrime();

        // precompute R^(m-1) % q for use in removing leading digit
        RM = 1;
        for (int i = 1; i <= patternLength - 1; i++)
            RM = (R * RM) % Q;
        patternHashValue = hash(pattern, patternLength);
    }

    // Compute hash for key[0 .. m-1].
    private long hash(String key, int m) {
        long h = 0;
        for (int j = 0; j < m; j++)
            h = (R * h + key.charAt(j)) % Q;
        return h;
    }

    // Las Vegas version: does pat[] match txt[i..i-m+1] ?
    private boolean check(String txt, int i) {
        for (int j = 0; j < patternLength; j++)
            if (pattern.charAt(j) != txt.charAt(i + j))
                return false;
        return true;
    }


    /**
     * Returns the index of the first occurrence of the pattern string
     * in the text string.
     * in the text string; n if no such match
     */
    public List<Integer> search(String text) {
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


    // a random 31-bit prime
    private static long longRandomPrime() {
        BigInteger prime = BigInteger.probablePrime(31, new Random());
        return prime.longValue();
    }

    /**
     * Takes a pattern string and an input string as command-line arguments;
     * searches for the pattern string in the text string; and prints
     * the first occurrence of the pattern string in the text string.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        /*
         *  pattern: abracadabra
         *  text:    abacadabrabracabracadabrabrabracad
         *
         *  pattern: rab
         *  text:    abacadabrabracabracadabrabrabracad
         *
         *  pattern: bcara
         *  text:         abacadabrabracabracadabrabrabracad
         *
         *  text:    abacadabrabracabracadabrabrabracad
         *  pattern:                        rabrabracad
         *
         *  text:    abacadabrabracabracadabrabrabracad
         *  pattern: abacad
         ******************************************************************************/
        String pattern = "aaaaa";
        String text = "baaaaaaa";


        RabinKarp searcher = new RabinKarp(pattern);
        List<Integer> offsets = searcher.search(text);
        System.out.println(offsets);
    }

    static long polyHash(String S, long p, long x) {
        long hash = 0;
        for (int i = S.length() - 1; i >= 0; i--)
            hash = (hash * x + S.charAt(i)) % p;
        return hash;
    }

    static long[] preComputeHashes(String T, String P, long p, long x) {
        long[] H = new long[(T.length() - P.length()) + 1];
        String S = T.substring(T.length() - P.length(), T.length() - 1);
        H[T.length() - P.length()] = polyHash(S, p, x);
        long y = 1;
        for (int i = 1; i < P.length(); i++)
            y = (y * x) % p;

        for (int i = T.length() - P.length() - 1; i >= 0; i--)
            H[i] = (x * H[i + 1] + T.charAt(i) - y * T.charAt(i + P.length()) % p);
        return H;
    }

    static long getRandomNumber(long p) {
        return (long) (Math.random() * ((p - 1 - 1) + 1)) + 1;
    }

    static List<Integer> rabinKarp(String text, String pattern) {
        long p = longRandomPrime();
        long x = getRandomNumber(p);
        List<Integer> result = new ArrayList<>();
        long pHash = polyHash(pattern, p, x);
        long[] H = preComputeHashes(text, pattern, p, x);
        for (int i = 0; i < (text.length() - pattern.length()); i++) {
            if (pHash != H[i]) continue;
            if (text.substring(i, i + pattern.length() - 1).equals(pattern)) result.add(i);
        }
        return result;
    }
}