package mst;

/******************************************************************************
 *  Generic max priority queue implementation with a binary heap.
 *  Can be used with a comparator instead of the natural order,
 *  but the generic Key type must still be Comparable.
 *
 *  We use a one-based array to simplify parent and child calculations.
 *
 *  Can be optimized by replacing full exchanges with half exchanges
 *  (ala insertion sort).
 *
 ******************************************************************************/

import util.StdIn;
import util.StdOut;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The insert and delete-the-maximum operations take  *  O(log n) amortized time, where n is the number
 * of elements in the priority queue. This is an amortized bound (and not a worst-case bound) because of array resizing operations.
 * The min, size, and is-empty operations take O(1) time in the worst case.
 * Construction takes time proportional to the specified capacity or the number of items used to initialize the data structure.
 */

public class MaxPQ<Key> implements Iterable<Key> {
    private Key[] priorityQueue;
    private int size;
    private Comparator<Key> comparator;

    public MaxPQ(int initCapacity) {
        priorityQueue = (Key[]) new Object[initCapacity + 1];
        size = 0;
    }

    public MaxPQ() {
        this(1);
    }


    public MaxPQ(int initCapacity, Comparator<Key> comparator) {
        this.comparator = comparator;
        priorityQueue = (Key[]) new Object[initCapacity + 1];
        size = 0;
    }

    public MaxPQ(Comparator<Key> comparator) {
        this(1, comparator);
    }

    /**
     * Initializes a priority queue from the array of keys.
     * Takes time proportional to the number of keys, using sink-based heap construction.
     */
    public MaxPQ(Key[] keys) {
        size = keys.length;
        priorityQueue = (Key[]) new Object[keys.length + 1];
        System.arraycopy(keys, 0, priorityQueue, 1, size);
        for (int k = size / 2; k >= 1; k--)
            sink(k);
        assert isMaxHeap();
    }


    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public Key getMax() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return priorityQueue[1];
    }

    private void resize(int capacity) {
        assert capacity > size;
        Key[] temp = (Key[]) new Object[capacity];
        if (size >= 0) System.arraycopy(priorityQueue, 1, temp, 1, size);
        priorityQueue = temp;
    }


    public void insert(Key x) {

        // double size of array if necessary
        if (size == priorityQueue.length - 1) resize(2 * priorityQueue.length);

        // add x, and percolate it up to maintain heap invariant
        priorityQueue[++size] = x;
        swim(size);
        assert isMaxHeap();
    }

    public Key delMax() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        Key max = priorityQueue[1];
        exchange(1, size--);
        sink(1);
        priorityQueue[size + 1] = null;     // to avoid loitering and help with garbage collection
        if ((size > 0) && (size == (priorityQueue.length - 1) / 4)) resize(priorityQueue.length / 2);
        assert isMaxHeap();
        return max;
    }


    /***************************************************************************
     * Helper functions to restore the heap invariant.
     ***************************************************************************/

    private void swim(int k) {
        //k=1 is root
        while (k > 1 && isLessThan(k / 2, k)) {
            exchange(k, k / 2);
            k = k / 2;
        }
    }

    private void sink(int k) {
        while (2 * k <= size) {
            int j = 2 * k;
            if (j < size && isLessThan(j, j + 1)) j++;
            if (!isLessThan(k, j)) break;
            exchange(k, j);
            k = j;
        }
    }

    private boolean isLessThan(int i, int j) {
        if (comparator == null) {
            return ((Comparable<Key>) priorityQueue[i]).compareTo(priorityQueue[j]) < 0;
        } else {
            return comparator.compare(priorityQueue[i], priorityQueue[j]) < 0;
        }
    }

    private void exchange(int i, int j) {
        Key swap = priorityQueue[i];
        priorityQueue[i] = priorityQueue[j];
        priorityQueue[j] = swap;
    }

    private boolean isMaxHeap() {
        for (int i = 1; i <= size; i++) {
            if (priorityQueue[i] == null) return false;
        }
        for (int i = size + 1; i < priorityQueue.length; i++) {
            if (priorityQueue[i] != null) return false;
        }
        if (priorityQueue[0] != null) return false;
        return isMaxHeapOrdered(1);
    }

    // is subtree of pq[1..n] rooted at k a max heap?
    private boolean isMaxHeapOrdered(int k) {
        if (k > size) return true;
        int left = 2 * k;
        int right = 2 * k + 1;
        if (left <= size && isLessThan(k, left)) return false;
        if (right <= size && isLessThan(k, right)) return false;
        return isMaxHeapOrdered(left) && isMaxHeapOrdered(right);
    }


    /***************************************************************************
     * Iterator.
     ***************************************************************************/

    /**
     * Returns an iterator that iterates over the keys on this priority queue
     * in descending order.
     * The iterator doesn't implement {@code remove()} since it's optional.
     */
    public Iterator<Key> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Key> {

        // create a new pq
        private MaxPQ<Key> copy;

        // add all items to copy of heap
        // takes linear time since already in heap order so no keys move
        public HeapIterator() {
            if (comparator == null) copy = new MaxPQ<Key>(size());
            else copy = new MaxPQ<Key>(size(), comparator);
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
            return copy.delMax();
        }
    }


    public static void main(String[] args) {
        MaxPQ<String> priorityQueue = new MaxPQ<>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) {
                priorityQueue.insert(item);
            } else if (!priorityQueue.isEmpty()) {
                StdOut.print(priorityQueue.delMax() + " ");
            }
        }
        StdOut.println("(" + priorityQueue.size() + " left on priorityQueue)");
    }
}

