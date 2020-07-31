/******************************************************************************
 *  Sorted symbol table implementation using a java.util.TreeMap.
 *  Does not allow duplicates.
 ******************************************************************************/


import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeMap;

/**
 * The SymbolTable class represents an ordered symbol table of generic key-value pairs.
 * It supports the usual put, get, contains, delete, size, and is-empty methods.
 * It also provides ordered methods for finding the minimum, maximum, floor, and ceiling.
 * It also provides a keys method for iterating over all of the keys.
 * A symbol table implements the associative array abstraction:
 * when associating a value with a key that is already in the symbol table,
 * the convention is to replace the old value with the new value.
 * Unlike Map, this class uses the convention that values cannot be null setting the value associated with a key to null is equivalent to deleting the key from the symbol table.
 * It requires that the key type implements the Comparable interface and calls the compareTo() and method to compare two keys.
 * It does not call either equals() or hashCode().
 * <p>
 * This implementation uses a red-black BST.
 * The put, get, contains, remove, minimum, maximum, ceiling, and floor operations each take Θ(log n) time in the worst case,
 * where n is the number of key-value pairs in the symbol table.
 * The size and is-empty operations take Θ(1) time.
 * Construction takes Θ(1) time.
 */
public class SymbolTable<Key extends Comparable<Key>, Value> implements Iterable<Key> {

    private TreeMap<Key, Value> map;


    public SymbolTable() {
        map = new TreeMap<Key, Value>();
    }

    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("calls get() with null key");
        return map.get(key);
    }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is { null}.
     */
    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("calls put() with null key");
        if (val == null) map.remove(key);
        else map.put(key, val);
    }

    /**
     * Removes the specified key and its associated value from this symbol table
     */
    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("calls delete() with null key");
        map.remove(key);
    }

    public void remove(Key key) {
        if (key == null) throw new IllegalArgumentException("calls remove() with null key");
        map.remove(key);
    }

    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("calls contains() with null key");
        return map.containsKey(key);
    }

    public int size() {
        return map.size();
    }


    public boolean isEmpty() {
        return size() == 0;
    }


    public Iterable<Key> keys() {
        return map.keySet();
    }


    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return map.firstKey();
    }

    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return map.lastKey();
    }

    /**
     * Returns the smallest key in this symbol table greater than or equal to { key}.
     */
    public Key ceiling(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to ceiling() is null");
        Key k = map.ceilingKey(key);
        if (k == null) throw new NoSuchElementException("argument to ceiling() is too large");
        return k;
    }

    /**
     * Returns the largest key in this symbol table less than or equal to {@code key}.
     */
    public Key floor(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to floor() is null");
        Key k = map.floorKey(key);
        if (k == null) throw new NoSuchElementException("argument to floor() is too small");
        return k;
    }
    @Deprecated
    public Iterator<Key> iterator() {
        return map.keySet().iterator();
    }

    public static void main(String[] args) {
        String[] array = {"S", "E", "A", "R", "C", " H", " E", "X", "A", "M", " P", " L", "E"};
        SymbolTable<String, Integer> symbolTable = new SymbolTable<String, Integer>();
        for (int i = 0; i < array.length; i++) {
            String key = array[i];
            symbolTable.put(key, i);
        }
        for (String s : symbolTable.keys())
            System.out.println(s + " " + symbolTable.get(s));
    }
}