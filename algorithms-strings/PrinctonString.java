/**
 * "The digital information that underlies biochemistry, cell biology, and development can be represented by a simple string of G's, A's, T's and C's. This string is the root data structure of an organism's biology." So, we're not talking just about data structure for information processing but for life it models of life itself.
 */
public class PrinctonString implements Comparable<String> {
    private char[] value; // characters
    private int offset; // index of first char in array
    private int length; // length of string
    private int hash; // cache of hashCode()

    public int length() {
        return length;
    }

    /**
     * So, in Java the standard is that it carries a 16-bit unsigned integer.
     * And one of the big, you know, features of Java's implementation is that you can get that operation done in constant time.
     * And then you can also do what's called concatenation, and that's add a character to the end of another string.
     * That one can't be done in constant time in the standard Java string data type.
     * The private instance variables are an array of characters.
     * And 'offset' that's an index into the first character of the string in the array.
     * So, once the string is built, the hash code computed then when it's time to use the hash code, and a hashing algorithm, it's immediately available.
     * So, the 'length' method simply returns that length.
     * To get the 'i??' character of the string we add 'i' to offset and get that character.
     * And to create a string given 'offset', 'length' and a 'char' array, we just reset those value.
     *
     * @param i
     * @return
     */
    public char charAt(int i) {
        return value[i + offset];
    }

    private PrinctonString(int offset, int length, char[] value) {
        this.offset = offset;
        this.length = length;
        this.value = value;
    }

    /*
     * And then the key thing is the 'substring' method.
     * Since, all it involves is a pointer into the immutable string and length on the index of the first character,
     * we can build a string in constant time just by copying the reference to the character array.
     * But to concatenate, to make a new string that results from adding one character to a string,
     * we have to create a whole new string and make a copy of it because the string itself is immutable.
     *  So it takes time proportional to the number of characters in the string.
     * And it involves making a new string.
     * And if you work out the memory usage for a string of length 'N' then it's '40+2N' bytes.
     * You might consider using char array but then you'll lose a lot of the convenience of being able to pretty substring instantly.
     * And also the language features that support strings.
     * StringBuilder you can get the length in constant time.
     * You can get characters in constant time just by doubling.
     * And you can concatenate out a new character in amortized constant time.
     * Most of the time it's constant.
     * Every once in a while you might have to double what you pay for that double by the number of operations that you did.
     * The thing you lose though is that it takes linear time to extract a substring.
     *  Because to extract a substring you have to make new char array that can be resizing and so forth and can be amenable to concat.
     */
    public String substring(int from, int to) {
        return new String(value, offset + from, to - from);
    }

    @Override
    public int compareTo(String o) {
        return 0;
    }

    /**
     * how do we efficiently reverse a string?
     * So, I could use a string or you could use a StringBuilder.
     * With string you get to declare it almost like a built-in type, and simply initialized with a null string.
     * And then to compute the reverse string we go backwards through the original string and concatenate the characters starting at the back to create a reverse string.
     */
    public static String reverseString(String s) {
        String rev = "";
        for (int i = s.length() - 1; i >= 0; i--)
            rev += s.charAt(i);
        return rev;
    }

    /**
     * + vs. append
     * Or with StringBuilder you use the StringBuilder data type, and so create an object that uses the doubling array and use the append operation.
     * So what do you think which one of these is gonna be most efficient for a long string?
     * And the answer is that it's StringBuilder because using the built-in string every time you do a concatenation you have to make a copy of the whole string.
     * So, if the string is of length 'N' it's gonna take one plus two plus three all the way up 'N', which sums to N², about N²/2.
     * So, it takes quadratic time to do, for this algorithm to run, for a long string and that's gonna preclude using it for huge strings.
     * As we have seen so many times, can't be using a quadratic time algorithm for a lot of data.
     * On the other hand, with StringBuilder it's linear time because the append operations are amortized linear.
     */
    public static String reverseStringBuilder(String s) {
        StringBuilder rev = new StringBuilder();
        for (int i = s.length() - 1; i >= 0; i--)
            rev.append(s.charAt(i));
        return rev.toString();
    }

    /**
     * So, let's look at it with string. We get the length that's gonna be the length of the array.
     * And what we do is for all values of 'i' we set suffixes of 'i' to the substring of x you... 's' you get by starting at 'i' and going all the way to 'N'.
     * And that's our suffix array. And this is the corresponding code for StringBuilder.
     * But now in this case the standard method is gonna be linear.
     * Whereas the StringBuilder, because there's only one string in the substrings or a few pointers into that string, whereas for
     * StringBuilder we have to make a new string for each suffix and there's a quadratic number of characters in the... in all of those strings.
     * So, it takes quadratic time. So you can't use StringBuilder to build a suffix array for a huge string.
     */
    public static String[] suffixesString(String s) {
        int N = s.length();
        String[] suffixes = new String[N];
        for (int i = 0; i < N; i++)
            suffixes[i] = s.substring(i, N);
        return suffixes;
    }

    public static String[] suffixesStringBuilder(String s) {
        int N = s.length();
        StringBuilder sb = new StringBuilder(s);
        String[] suffixes = new String[N];
        for (int i = 0; i < N; i++)
            suffixes[i] = sb.substring(i, N);
        return suffixes;
    }

    /**
     * the longest common prefix. So, here is some, a static method that we will implement.
     * This function takes two strings as arguments. We only need to go as far as the length of the shortest of the two strings.
     * So that's 'N'. And then we just go ahead and start at the beginning and compare. As long as the strings are equal we increment 'i'. And if we get to a point where they're non-equal that's when we return 'i'. In that case that's the length of the longest common prefix is. In this case they're not equal at four. That means they match for four characters. And if we get to the end of one of them then that's the prefix. So we just return 'N'.
     * Although if the prefix is short like if the two strings have a different first character then it's sub-linear, it doesn't have to look at all the data. Just has to look at amount that matches. So, the idea of a sub-linear time algorithm for string processing is a really important one that we're going to be taking advantage of as we move into more complicated algorithms.
     * So, for example you can compare two strings without looking at them all. It depends how.
     * Just have to find the first place that they differ. So you don't look at all the data that's sub-linear time.
     * We are gonna see sorting algorithms that take advantage of that.
     */
    public static int lcp(String s, String t) {
        int N = Math.min(s.length(), t.length());
        for (int i = 0; i < N; i++)
            if (s.charAt(i) != t.charAt(i))
                return i;
        return N;
    }

    public static void main(String[] args) {
        for (int i = 0; i <= 52; i++) {
            System.out.println((char) ('a' + i));
        }
        char[] a = {'d', 'a', 'c', 'f', 'f', 'b', 'd', 'b', 'f', 'b', 'e', 'a'};
        System.out.println();
        char[] R = {'a', 'b', 'c', 'd', 'e', 'f'};
        printArray(a);
        keyIndexCounting(R, a);
        printArray(a);
    }

    private static char[] keyIndexCounting(char[] R, char[] a) {
        System.out.println(a[0] + 1);
        char[] aux = new char[a.length];
        int N = a.length;
        int[] count = new int[R.length + 1];
        for (int i = 0; i < N; i++)
            count[(char) (a[i] + 1)]++;
        for (int r = 0; r < R.length; r++)
            count[r + 1] += count[r];
        for (int i = 0; i < N; i++)
            aux[count[a[i]]++] = a[i];
        for (int i = 0; i < N; i++)
            a[i] = aux[i];
        return a;
    }

    static void printArray(char[] a) {
        for (char value : a) {
            System.out.print(value);
        }
        System.out.println();
    }
}