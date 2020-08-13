import java.util.ArrayDeque;
import java.util.Queue;

/**
 * The TrieST class represents an symbol table of key-value pairs, with string keys and generic values.
 * It supports the usual put, get, contains, delete, size, and is-empty methods.
 * It also provides character-based methods for finding the string in the symbol table that is the longest prefix of a given prefix,
 * finding all strings in the symbol table that start with a given prefix, and finding all strings in the symbol table that match a given pattern.
 * A symbol table implements the associative array abstraction:
 * when associating a value with a key that is already in the symbol table, the convention is to replace the old value with the new value.
 * Unlike Map, this class uses the convention that values cannot be null setting the value associated with a key to null is equivalent to deleting the key from the symbol table.
 * This implementation uses a 256-way trie.
 * The put, contains, delete, and longest prefix operations take time proportional to the length of the key (in the worst case).
 * Construction takes constant time.
 * The size, and is-empty operations take constant time.
 * Construction takes constant time.
 */
public class TrieST<Value> {
    private static final int R = 256;        // extended ASCII


    private Node root;      // root of trie
    private int n;          // number of keys in trie

    public TrieST() {
    }

    public static void main(String[] args) {
        String[] array = {"she", "sells", "sea", "shells", "by", "the", "sea", "shore"};
        // build symbol table from standard input
        TrieST<Integer> st = new TrieST<Integer>();
        for (int i = 0; i < array.length; i++) {
            st.put(array[i], i);
        }

        // print results
        if (st.size() < 100) {
            System.out.println("keys(\"\"):");
            for (String key : st.keys()) {
                System.out.println(key + " " + st.get(key));
            }
            System.out.println();
        }

        System.out.println("longestPrefixOf(\"shellsort\"):");
        System.out.println(st.longestPrefixOf("shellsort"));
        System.out.println();

        System.out.println("longestPrefixOf(\"quicksort\"):");
        System.out.println(st.longestPrefixOf("quicksort"));
        System.out.println();

        System.out.println("keysWithPrefix(\"shor\"):");
        for (String s : st.keysWithPrefix("shor"))
            System.out.println(s);
        System.out.println();

        System.out.println("keysThatMatch(\".he.l.\"):");
        for (String s : st.keysThatMatch(".he.l."))
            System.out.println(s);
    }

    public Value get(String key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        Node x = get(root, key, 0);
        if (x == null) return null;
        return (Value) x.value;
    }


    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    private Node get(Node node, String key, int d) {
        if (node == null) return null;
        //we are in the position of the last character of the key so it is found and it is the node itself
        if (d == key.length()) return node;
        char c = key.charAt(d);
        //otherwise find the next array it could be the same it could be the branch?
        
        return get(node.next[c], key, d + 1);
    }

    public void put(String key, Value val) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        if (val == null) delete(key);
            //it is assumed the first key d=0; is of length=1
        else root = put(root, key, val, 0);
    }

    private Node put(Node node, String key, Value value, int d) {
        if (node == null) node = new Node();

        if (d == key.length()) {
            if (node.value == null) n++;
            node.value = value;
            return node;
        }
        char c = key.charAt(d);
        node.next[c] = put(node.next[c], key, value, d + 1);
        return node;
    }


    public int size() {
        return n;
    }


    public boolean isEmpty() {
        return size() == 0;
    }


    public Iterable<String> keys() {
        return keysWithPrefix("");
    }

    /**
     * Returns all of the keys in the set that start with  prefix}.
     *
     * @param prefix the prefix
     * @return all of the keys in the set that start with  prefix},
     * as an iterable
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> results = new ArrayDeque<>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return results;
    }

    private void collect(Node node, StringBuilder prefix, Queue<String> results) {
        if (node == null) return;
        if (node.value != null) results.offer(prefix.toString());
        for (char c = 0; c < R; c++) {
            prefix.append(c);
            collect(node.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }


    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> results = new ArrayDeque<>();
        collect(root, new StringBuilder(), pattern, results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, String pattern, Queue<String> results) {
        if (x == null) return;
        int d = prefix.length();
        if (d == pattern.length() && x.value != null)
            results.offer(prefix.toString());
        if (d == pattern.length())
            return;
        char c = pattern.charAt(d);
        if (c == '.') {
            for (char ch = 0; ch < R; ch++) {
                prefix.append(ch);
                collect(x.next[ch], prefix, pattern, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        } else {
            prefix.append(c);
            collect(x.next[c], prefix, pattern, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }


    public String longestPrefixOf(String query) {
        if (query == null) throw new IllegalArgumentException("argument to longestPrefixOf() is null");
        int length = longestPrefixOf(root, query, 0, -1);
        if (length == -1) return null;
        else return query.substring(0, length);
    }


    private int longestPrefixOf(Node x, String query, int d, int length) {
        if (x == null) return length;
        if (x.value != null) length = d;
        if (d == query.length()) return length;
        char c = query.charAt(d);
        return longestPrefixOf(x.next[c], query, d + 1, length);
    }


    public void delete(String key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        root = delete(root, key, 0);
    }

    private Node delete(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) {
            if (x.value != null) n--;
            x.value = null;
        } else {
            char c = key.charAt(d);
            x.next[c] = delete(x.next[c], key, d + 1);
        }

        // remove subtrie rooted at x if it is completely empty
        if (x.value != null) return x;
        for (int c = 0; c < R; c++)
            if (x.next[c] != null)
                return x;
        return null;
    }

    // R-way trie node
    private static class Node {
        private Object value;
        private final Node[] next = new Node[R];
    }
}

