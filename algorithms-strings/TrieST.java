/******************************************************************************
 *  A string symbol table for extended ASCII strings, implemented using a 256-way trie.
 *  % java TrieST < shellsST.txt
 *  by 4
 *  sea 6
 *  sells 1
 *  she 0
 *  shells 3
 *  shore 7
 *  the 5
 *
 ******************************************************************************/


import java.util.ArrayDeque;
import java.util.Queue;

/**
 * The TrieST class represents an symbol table of key-value pairs, with
 * string keys and generic values.
 * It supports the usual put, get, contains, delete, size, and is-empty methods.
 * It also provides character-based methods for finding the string in the symbol table that is the longest prefix of a given prefix,
 * finding all strings in the symbol table that start with a given prefix,
 * and finding all strings in the symbol table that match a given pattern.
 * A symbol table implements the associative array abstraction:
 * when associating a value with a key that is already in the symbol table, the convention is to replace the old value with the new value.
 * Unlike Map, this class uses the convention that values cannot be null setting the value associated with a key to null is equivalent to deleting the key from the symbol table.
 * This implementation uses a 256-way trie.
 * The put, contains, delete, and longest prefix operations take time proportional to the length of the key (in the worst case).
 * Construction takes constant time. The size, and is-empty operations take constant time. Construction takes constant time.
 */
public class TrieST {
    private static final int R = 256;        // extended ASCII


    private Node root;      // root of trie
    private int n;          // number of keys in trie

    // R-way trie node
    private static class Node {
        private Integer val;
        private final Node[] next = new Node[R];
    }


    public TrieST() {
    }


    public Integer get(String key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        Node node = get(root, key, 0);
        if (node == null) return null;
        return (Integer) node.val;
    }

    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    private Node get(Node node, String key, int d) {
        if (node == null) return null;
        if (d == key.length()) return node;
        char c = key.charAt(d);
        return get(node.next[c], key, d + 1);
    }


    public void put(String key, Integer val) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        if (val == null) delete(key);
        else root = put(root, key, val, 0);
    }

    private Node put(Node node, String key, Integer val, int d) {
        if (node == null) node = new Node();
        if (d == key.length()) {
            if (node.val == null) n++;
            node.val = val;
            return node;
        }
        char c = key.charAt(d);
        node.next[c] = put(node.next[c], key, val, d + 1);
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


    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> results = new ArrayDeque<>();
        Node node = get(root, prefix, 0);
        collect(node, new StringBuilder(prefix), results);
        return results;
    }

    private void collect(Node node, StringBuilder prefix, Queue<String> results) {
        if (node == null) return;
        if (node.val != null) results.offer(prefix.toString());
        for (char c = 0; c < R; c++) {
            prefix.append(c);
            collect(node.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /**
     * Returns all of the keys in the symbol table that match {@code pattern},
     * where . symbol is treated as a wildcard character.
     */
    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> results = new ArrayDeque<>();
        collect(root, new StringBuilder(), pattern, results);
        return results;
    }

    private void collect(Node node, StringBuilder prefix, String pattern, Queue<String> results) {
        if (node == null) return;
        int d = prefix.length();
        if (d == pattern.length() && node.val != null)
            results.offer(prefix.toString());
        if (d == pattern.length())
            return;
        char c = pattern.charAt(d);
        if (c == '.') {
            for (char ch = 0; ch < R; ch++) {
                prefix.append(ch);
                collect(node.next[ch], prefix, pattern, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        } else {
            prefix.append(c);
            collect(node.next[c], prefix, pattern, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /**
     * Returns the string in the symbol table that is the longest prefix of {@code query},
     * or {@code null}, if no such string.
     */
    public String longestPrefixOf(String query) {
        if (query == null) throw new IllegalArgumentException("argument to longestPrefixOf() is null");
        int length = longestPrefixOf(root, query, 0, -1);
        if (length == -1) return null;
        else return query.substring(0, length);
    }

    // returns the length of the longest string key in the subtrie
    // rooted at node that is a prefix of the query string,
    // assuming the first d character match and we have already
    // found a prefix match of given length (-1 if no such match)
    private int longestPrefixOf(Node node, String query, int d, int length) {
        if (node == null) return length;
        if (node.val != null) length = d;
        if (d == query.length()) return length;
        char c = query.charAt(d);
        return longestPrefixOf(node.next[c], query, d + 1, length);
    }

    /**
     * Removes the key from the set if the key is present.
     *
     * @param key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void delete(String key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        root = delete(root, key, 0);
    }

    private Node delete(Node node, String key, int d) {
        if (node == null) return null;
        if (d == key.length()) {
            if (node.val != null) n--;
            node.val = null;
        } else {
            char c = key.charAt(d);
            node.next[c] = delete(node.next[c], key, d + 1);
        }

        // remove subtrie rooted at node if it is completely empty
        if (node.val != null) return node;
        for (int c = 0; c < R; c++)
            if (node.next[c] != null)
                return node;
        return null;
    }


    public static void main(String[] args) {
        String[] words = {"she", "sells", "sea", "shells", "by", "the", "sea", "shore"};

        TrieST st = new TrieST();
        for (int i = 0; i < words.length; i++) {
            st.put(words[i], i);
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
}

