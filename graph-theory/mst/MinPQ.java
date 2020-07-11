package mst;

/******************************************************************************
 *  Generic min priority queue implementation with a binary heap.
 *  Can be used with a comparator instead of the natural order.
 *  We use a one-based array to simplify parent and child calculations.
 *  Can be optimized by replacing full exchanges with half exchanges
 *  (ala insertion sort).
 ******************************************************************************/

import util.StdIn;
import util.StdOut;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This implementation uses a binary heap.
 * The insert and delete-the-minimum operations take
 * O(log n) amortized time, where n is the number
 * of elements in the priority queue. This is an amortized bound
 * (and not a worst-case bound) because of array resizing operations.
 * The min, size, and is-empty operations take O(1) time in the worst case.
 * Construction takes time proportional to the specified capacity or the
 * number of items used to initialize the data structure.
 */
public class MinPQ<Key> implements Iterable<Key> {
    private Key[] priorityQueue;                    // store items at indices 1 to n
    private int size;                       // number of items on priority queue
    private Comparator<Key> comparator;  // optional comparator

    public MinPQ(int initCapacity) {
        priorityQueue = (Key[]) new Object[initCapacity + 1];
        size = 0;
    }


    public MinPQ() {
        this(1);
    }

    public MinPQ(int initCapacity, Comparator<Key> comparator) {
        this.comparator = comparator;
        priorityQueue = (Key[]) new Object[initCapacity + 1];
        size = 0;
    }


    public MinPQ(Key[] keys) {
        size = keys.length;
        priorityQueue = (Key[]) new Object[keys.length + 1];
        System.arraycopy(keys, 0, priorityQueue, 1, size);
        for (int k = size / 2; k >= 1; k--)
            sink(k);
        assert isMinHeap();
    }


    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }


    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return priorityQueue[1];
    }

    // helper function to double the size of the heap array
    private void resize(int capacity) {
        assert capacity > size;
        Key[] temp = (Key[]) new Object[capacity];
        if (size >= 0) System.arraycopy(priorityQueue, 1, temp, 1, size);
        priorityQueue = temp;
    }

    public void insert(Key x) {
        if (size == priorityQueue.length - 1) resize(2 * priorityQueue.length);
        priorityQueue[++size] = x;
        swim(size);
        assert isMinHeap();
    }

    public Key delMin() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        Key min = priorityQueue[1];
        exchange(1, size--);
        sink(1);
        priorityQueue[size + 1] = null;     // to avoid loiterig and help with garbage collection
        if ((size > 0) && (size == (priorityQueue.length - 1) / 4)) {
            resize(priorityQueue.length / 2);
        }
        assert isMinHeap();
        return min;
    }


    private void swim(int k) {
        while (k > 1 && greater(k / 2, k)) {
            exchange(k, k / 2);
            k = k / 2;
        }
    }

    private void sink(int k) {
        while (2 * k <= size) {
            int j = 2 * k;
            if (j < size && greater(j, j + 1)) j++;//decide which child is the larger of the two
            if (!greater(k, j)) break;
            exchange(k, j);
            k = j;
        }
    }

    /***************************************************************************
     * Helper functions for compares and swaps.
     ***************************************************************************/
    private boolean greater(int i, int j) {
        if (comparator == null) {
            return ((Comparable<Key>) priorityQueue[i]).compareTo(priorityQueue[j]) > 0;
        } else {
            return comparator.compare(priorityQueue[i], priorityQueue[j]) > 0;
        }
    }

    private void exchange(int i, int j) {
        Key swap = priorityQueue[i];
        priorityQueue[i] = priorityQueue[j];
        priorityQueue[j] = swap;
    }

    // is pq[1..n] a min heap?
    private boolean isMinHeap() {
        for (int i = 1; i <= size; i++) {
            if (priorityQueue[i] == null) return false;
        }
        for (int i = size + 1; i < priorityQueue.length; i++) {
            if (priorityQueue[i] != null) return false;
        }
        if (priorityQueue[0] != null) return false;
        return isMinHeapOrdered(1);
    }

    // is subtree of pq[1..n] rooted at k a min heap?
    private boolean isMinHeapOrdered(int k) {
        if (k > size) return true;
        int left = 2 * k;
        int right = 2 * k + 1;
        if (left <= size && greater(k, left)) return false;
        if (right <= size && greater(k, right)) return false;
        return isMinHeapOrdered(left) && isMinHeapOrdered(right);
    }


    /**
     * Returns an iterator that iterates over the keys on this priority queue
     * in ascending order.
     * <p>
     * The iterator doesn't implement {@code remove()} since it's optional.
     *
     * @return an iterator that iterates over the keys in ascending order
     */
    public Iterator<Key> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Key> {
        // create a new pq
        private MinPQ<Key> copy;

        // add all items to copy of heap
        // takes linear time since already in heap order so no keys move
        public HeapIterator() {
            if (comparator == null) copy = new MinPQ<Key>(size());
            else copy = new MinPQ<Key>(size(), comparator);
            for (int i = 1; i <= size; i++)
                copy.insert(priorityQueue[i]);
        }

        public boolean hasNext() {
            return !copy.isEmpty();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Key next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMin();
        }
    }

    /**
     * Unit tests the {@code MinPQ} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        MinPQ<String> pq = new MinPQ<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) pq.insert(item);
            else if (!pq.isEmpty()) StdOut.print(pq.delMin() + " ");
        }
        StdOut.println("(" + pq.size() + " left on pq)");
    }

}


