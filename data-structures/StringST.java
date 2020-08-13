/******************************************************************************
 *  Sorted symbol table implementation using a java.util.TreeMap.
 *  Does not allow duplicates.
 ******************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeMap;

/**
 * The ST class represents an ordered symbol table of generic key-value pairs. It supports the usual put, get, contains, delete, size, and is-empty methods. It also provides ordered methods for finding the minimum, maximum, floor, and ceiling. It also provides a keys method for iterating over all of the keys. A symbol table implements the associative array abstraction: when associating a value with a key that is already in the symbol table, the convention is to replace the old value with the new value. Unlike Map, this class uses the convention that values cannot be nullâ€”setting the value associated with a key to null is equivalent to deleting the key from the symbol table.
 * It requires that the key type implements the Comparable interface and calls the compareTo() and method to compare two keys. It does not call either equals() or hashCode().
 * <p>
 * This implementation uses a red-black BST. The put, get, contains, remove, minimum, maximum, ceiling, and floor operations each take Θ(log n) time in the worst case, where n is the number of key-value pairs in the symbol table. The size and is-empty operations take Θ(1) time. Construction takes Θ(1) time.
 * <p>
 * <p>
 * For additional documentation, see Section 3.5 of Algorithms, 4th Edition by Robert Sedgewick and Kevin Wayne.
 */

public class StringST {

    private TreeMap<String, Integer> st;


    public StringST() {
        st = new TreeMap<>();
    }


    /**
     * returns null if the key is not in this symbol table
     */
    public Integer get(String key) {
        if (key == null) throw new IllegalArgumentException("calls get() with null key");
        return st.get(key);
    }

    public void put(String key, Integer val) {
        if (key == null) throw new IllegalArgumentException("calls put() with null key");
        if (val == null) st.remove(key);
        else st.put(key, val);
    }

    public void remove(String key) {
        if (key == null) throw new IllegalArgumentException("calls remove() with null key");
        st.remove(key);
    }

    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("calls contains() with null key");
        return st.containsKey(key);
    }


    public int size() {
        return st.size();
    }


    public boolean isEmpty() {
        return size() == 0;
    }


    public Iterable<String> keys() {
        return st.keySet();
    }


    @Deprecated
    public Iterator<String> iterator() {
        return st.keySet().iterator();
    }

    /**
     * Returns the smallest key in this symbol table.
     */
    public String min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return st.firstKey();
    }

    /**
     * Returns the largest key in this symbol table.
     */
    public String max() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return st.lastKey();
    }

    /**
     * Returns the smallest key >=key.
     */
    public String ceiling(String key) {
        if (key == null) throw new IllegalArgumentException("argument to ceiling() is null");
        String k = st.ceilingKey(key);
        if (k == null) throw new NoSuchElementException("argument to ceiling() is too large");
        return k;
    }

    /**
     * Returns the largest key <=key
     */
    public String floor(String key) {
        if (key == null) throw new IllegalArgumentException("argument to floor() is null");
        String k = st.floorKey(key);
        if (k == null) throw new NoSuchElementException("argument to floor() is too small");
        return k;
    }

    public static void main(String[] args) {
        String[] a = {"S", "E", " A", " R", " C", " H", " E", " X", " A", " M", " P", " L", "E"};
        StringST st = new StringST();
        for (int i = 0; i < a.length; i++) {
            st.put(a[i], i);
        }
        for (String s : st.keys())
            System.out.println(s + " " + st.get(s));
    }

    public int compareTo(String o) {
        return 0;
    }
}

/******************************************************************************
 *  Copyright 2002-2020, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
