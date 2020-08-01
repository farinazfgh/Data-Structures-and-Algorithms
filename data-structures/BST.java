import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * The {BST} class represents an ordered symbol table of generic key-value pairs.
 * It supports the usual
 * put, get, contains, delete, size, and is-empty methods.
 * It also provides ordered methods for finding the minimum,maximum, floor, select, ceiling.
 * It also provides a keys method for iterating over all of the keys.
 * A symbol table implements the associative array abstraction:
 * when associating a value with a key that is already in the symbol table,
 * the convention is to replace the old value with the new value.
 * Unlike {java.util.Map}, this class uses the convention that values cannot be {null}—setting the
 * value associated with a key to {null} is equivalent to deleting the key
 * from the symbol table.
 * It requires that the key type implements the {Comparable} interface and calls the
 * {compareTo()} and method to compare two keys. It does not call either {equals()} or {hashCode()}.
 * This implementation uses an (unbalanced) binary search tree.
 * The put, contains, remove, minimum, maximum, ceiling, floor, select, and rank  operations each take O(n) time in the worst
 * case, where n is the number of key-value pairs.
 * The size and is-empty operations take O(1) time.
 * The keys method takes O(n) time in the worst case.
 * Construction takes O(1) time.
 * For alternative implementations of the symbol table API, see { ST}, { BinarySearchST}, { SequentialSearchST}, { RedBlackBST},
 * { SeparateChainingHashST}, and { LinearProbingHashST}.
 */
public class BST<Key extends Comparable<Key>, Value> {
    private Node root;

    private class Node {
        private Key key;
        private Value value;
        private Node left, right;
        private int size;          // number of nodes in subtree rooted at this certain node

        public Node(Key key, Value value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
        }
    }


    public BST() {
    }


    public boolean isEmpty() {
        return size() == 0;
    }


    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }


    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    public Value get(Key key) {
        return get(root, key);
    }

    private Value get(Node x, Key key) {
        if (key == null) throw new IllegalArgumentException("calls get() with a null key");
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else return x.value;
    }

    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("calls put() with a null key");
        if (val == null) {
            delete(key);
            return;
        }
        root = put(root, key, val);
        assert check();
    }

    private Node put(Node x, Key key, Value val) {
        if (x == null) return new Node(key, val, 1);
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = put(x.left, key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else x.value = val;
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }


    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow");
        root = deleteMin(root);
        assert check();
    }

    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }


    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow");
        root = deleteMax(root);
        assert check();
    }

    private Node deleteMax(Node x) {
        if (x.right == null) return x.left;
        x.right = deleteMax(x.right);
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }


    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("calls delete() with a null key");
        root = delete(root, key);
        assert check();
    }

    private Node delete(Node x, Key key) {
        if (x == null) return null;

        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = delete(x.left, key);
        else if (cmp > 0) x.right = delete(x.right, key);
        else {
            if (x.right == null) return x.left;
            if (x.left == null) return x.right;
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }


    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return min(root).key;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        else return min(x.left);
    }


    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return max(root).key;
    }

    private Node max(Node x) {
        if (x.right == null) return x;
        else return max(x.right);
    }


    public Key floor(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to floor() is null");
        if (isEmpty()) throw new NoSuchElementException("calls floor() with empty symbol table");
        Node x = floor(root, key);
        if (x == null) throw new NoSuchElementException("argument to floor() is too small");
        else return x.key;
    }

    private Node floor(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0) return floor(x.left, key);
        Node t = floor(x.right, key);
        if (t != null) return t;//if there is no subtree in the right return the root itself
        else return x;
    }

    public Key floor2(Key key) {
        Key x = floor2(root, key, null);
        if (x == null) throw new NoSuchElementException("argument to floor() is too small");
        else return x;

    }

    private Key floor2(Node x, Key key, Key best) {
        if (x == null) return best;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return floor2(x.left, key, best);
        else if (cmp > 0) return floor2(x.right, key, x.key);
        else return x.key;
    }

    /**
     * Returns the smallest key in the symbol table greater than or equal to {key}.
     * the smallest key in the symbol table greater than or equal to {key}
     */
    public Key ceiling(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to ceiling() is null");
        if (isEmpty()) throw new NoSuchElementException("calls ceiling() with empty symbol table");
        Node x = ceiling(root, key);
        if (x == null) throw new NoSuchElementException("argument to floor() is too large");
        else return x.key;
    }

    private Node ceiling(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0) {
            Node t = ceiling(x.left, key);
            if (t != null) return t;
            else return x;
        }
        return ceiling(x.right, key);
    }

    public Key select(int rank) {
        if (rank < 0 || rank >= size()) {
            throw new IllegalArgumentException("argument to select() is invalid: " + rank);
        }
        return select(root, rank);
    }


    private Key select(Node x, int rank) {
        if (x == null) return null;
        int leftSize = size(x.left);
        if (leftSize > rank) return select(x.left, rank);
        else if (leftSize < rank) return select(x.right, rank - leftSize - 1);
        else return x.key;
    }

    /**
     * Return the number of keys in the symbol table strictly less than {key}.
     */
    public int rank(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to rank() is null");
        return rank(key, root);
    }

    // Number of keys in the subtree less than key.
    private int rank(Key key, Node x) {
        if (x == null) return 0;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return rank(key, x.left);
        else if (cmp > 0) return 1 + size(x.left) + rank(key, x.right);
        else return size(x.left);
    }


    public Iterable<Key> keys() {
        if (isEmpty()) return new ArrayDeque<>();
        return keys(min(), max());
    }


    public Iterable<Key> keys(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to keys() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to keys() is null");

        Queue<Key> queue = new ArrayDeque<Key>();
        keys(root, queue, lo, hi);
        return queue;
    }

    private void keys(Node x, Queue<Key> queue, Key lo, Key hi) {
        if (x == null) return;
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0) keys(x.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.offer(x.key);
        if (cmphi > 0) keys(x.right, queue, lo, hi);
    }

    public int size(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to size() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to size() is null");

        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else return rank(hi) - rank(lo);
    }


    public int height() {
        return height(root);
    }

    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.left), height(x.right));
    }


    public Iterable<Key> levelOrder() {
        Queue<Key> keys = new ArrayDeque<>();
        Queue<Node> queue = new ArrayDeque<Node>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node x = queue.poll();
            if (x == null) continue;
            keys.offer(x.key);
            queue.offer(x.left);
            queue.offer(x.right);
        }
        return keys;
    }


    private boolean check() {
        if (!isBST()) System.out.println("Not in symmetric order");
        if (!isSizeConsistent()) System.out.println("Subtree counts not consistent");
        if (!isRankConsistent()) System.out.println("Ranks not consistent");
        return isBST() && isSizeConsistent() && isRankConsistent();
    }

    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
    private boolean isBST() {
        return isBST(root, null, null);
    }

    // is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    private boolean isBST(Node x, Key min, Key max) {
        if (x == null) return true;
        if (min != null && x.key.compareTo(min) <= 0) return false;
        if (max != null && x.key.compareTo(max) >= 0) return false;
        return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
    }

    private boolean isSizeConsistent() {
        return isSizeConsistent(root);
    }

    private boolean isSizeConsistent(Node x) {
        if (x == null) return true;
        if (x.size != size(x.left) + size(x.right) + 1) return false;
        return isSizeConsistent(x.left) && isSizeConsistent(x.right);
    }

    // check that ranks are consistent
    private boolean isRankConsistent() {
        for (int i = 0; i < size(); i++)
            if (i != rank(select(i))) return false;
        for (Key key : keys())
            if (key.compareTo(select(rank(key))) != 0) return false;
        return true;
    }


    public static void main(String[] args) {
        BST<String, Integer> st = new BST<String, Integer>();
        String[] array = {"S", "E", " A ", "R", " C ", "H ", "E", " X", " A ", "M", " P", " L", " E"};
        for (int i = 0; i < array.length; i++) {
            String key = array[i];
            st.put(key, i);
        }

        for (String s : st.levelOrder())
            System.out.println(s + " " + st.get(s));

        System.out.println();

        for (String s : st.keys())
            System.out.println(s + " " + st.get(s));
    }
}
