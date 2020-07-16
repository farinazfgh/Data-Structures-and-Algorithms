package util; /******************************************************************************
 *  Minimum-oriented indexed PQ implementation using a binary heap.
 ******************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * an indexed priority queue of generic keys.
 * It supports the following
 * insert
 * delete-the-minimum
 * delete
 * change-the-priority
 * In order to let the client refer to keys on the priority queue, an integer between  0 and  maxN - 1
 * is associated with each priority the client uses this integer to specify
 * which priority to delete or change.
 * It also supports methods for peeking at the minimum priority,
 * testing if the priority queue is empty, and iterating through
 * the keys.
 * This implementation uses a binary heap along with an array to associate keys with integers in the given range.
 * The insert, delete-the-minimum, delete, change-priority, decrease-priority, and increase-priority
 * operations take O(log n) time in the worst case,
 * where n is the number of elements in the priority queue.
 * Construction takes time proportional to the specified capacity.
 */
public class IndexMinPQ<Key extends Comparable<Key>> implements Iterable<Integer> {
    private final int maxN;        // maximum number of elements on PQ
    private int size;           // number of elements on PQ
    private final int[] priorityQueue;        // binary heap using 1-based indexing
    private final int[] inversePriorityQueue;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
    private final Key[] priorities;      // keys[i] = priority of i

    public IndexMinPQ(int maxN) {
        if (maxN < 0) throw new IllegalArgumentException();
        this.maxN = maxN;
        size = 0;
        priorities = (Key[]) new Comparable[maxN + 1];    // make this of length maxN??
        priorityQueue = new int[maxN + 1];
        inversePriorityQueue = new int[maxN + 1];                   // make this of length maxN??
        for (int i = 0; i <= maxN; i++)
            inversePriorityQueue[i] = -1;
    }

    public static void main(String[] args) {
        // insert a bunch of strings
        String[] strings = {"it", "was", "the", "best", "of", "times", "it", "was", "the", "worst"};

        IndexMinPQ<String> pq = new IndexMinPQ<String>(strings.length);
        for (int i = 0; i < strings.length; i++) {
            pq.insert(i, strings[i]);
        }

        // delete and print each priority
        while (!pq.isEmpty()) {
            int i = pq.delMin();
            StdOut.println(i + " " + strings[i]);
        }
        StdOut.println();

        // reinsert the same strings
        for (int i = 0; i < strings.length; i++) {
            pq.insert(i, strings[i]);
        }

        // print each priority using the iterator
        for (int i : pq) {
            StdOut.println(i + " " + strings[i]);
        }
        while (!pq.isEmpty()) {
            pq.delMin();
        }

    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(int i) {
        validateIndex(i);
        return inversePriorityQueue[i] != -1;
    }

    public int size() {
        return size;
    }

    public void insert(int i, Key priority) {
        validateIndex(i);
        if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");
        size++;
        inversePriorityQueue[i] = size;
        priorityQueue[size] = i;
        priorities[i] = priority;
        swim(size);
    }

    public int minIndex() {
        if (size == 0) throw new NoSuchElementException("Priority queue underflow");
        return priorityQueue[1];
    }

    public Key minKey() {
        if (size == 0) throw new NoSuchElementException("Priority queue underflow");
        return priorities[priorityQueue[1]];
    }

    public int delMin() {
        if (size == 0) throw new NoSuchElementException("Priority queue underflow");
        int min = priorityQueue[1];
        exchange(1, size--);
        sink(1);
        assert min == priorityQueue[size + 1];
        inversePriorityQueue[min] = -1;        // delete
        priorities[min] = null;    // to help with garbage collection
        priorityQueue[size + 1] = -1;        // not needed
        return min;
    }

    public Key keyOf(int i) {
        validateIndex(i);
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        else return priorities[i];
    }

    public void changePriority(int i, Key priority) {
        validateIndex(i);
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        priorities[i] = priority;
        swim(inversePriorityQueue[i]);
        sink(inversePriorityQueue[i]);
    }

    @Deprecated
    public void change(int i, Key priority) {
        changePriority(i, priority);
    }

    public void decreasePriority(int i, Key priority) {
        validateIndex(i);
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        if (priorities[i].compareTo(priority) == 0)
            throw new IllegalArgumentException("Calling decreaseKey() with a priority equal to the priority in the priority queue");
        if (priorities[i].compareTo(priority) < 0)
            throw new IllegalArgumentException("Calling decreaseKey() with a priority strictly greater than the priority in the priority queue");
        priorities[i] = priority;
        swim(inversePriorityQueue[i]);
    }

    public void increasePriorityKey(int i, Key priority) {
        validateIndex(i);
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        if (priorities[i].compareTo(priority) == 0)
            throw new IllegalArgumentException("Calling increaseKey() with a priority equal to the priority in the priority queue");
        if (priorities[i].compareTo(priority) > 0)
            throw new IllegalArgumentException("Calling increaseKey() with a priority strictly less than the priority in the priority queue");
        priorities[i] = priority;
        sink(inversePriorityQueue[i]);
    }

    public void delete(int i) {
        validateIndex(i);
        if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
        int index = inversePriorityQueue[i];
        exchange(index, size--);
        swim(index);
        sink(index);
        priorities[i] = null;
        inversePriorityQueue[i] = -1;
    }

    private void validateIndex(int i) {
        if (i < 0) throw new IllegalArgumentException("index is negative: " + i);
        if (i >= maxN) throw new IllegalArgumentException("index >= capacity: " + i);
    }

    private boolean greater(int i, int j) {
        return priorities[priorityQueue[i]].compareTo(priorities[priorityQueue[j]]) > 0;
    }

    private void exchange(int i, int j) {
        int swap = priorityQueue[i];
        priorityQueue[i] = priorityQueue[j];
        priorityQueue[j] = swap;
        inversePriorityQueue[priorityQueue[i]] = i;
        inversePriorityQueue[priorityQueue[j]] = j;
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
            if (j < size && greater(j, j + 1)) j++;
            if (!greater(k, j)) break;
            exchange(k, j);
            k = j;
        }
    }

    public Iterator<Integer> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Integer> {
        private final IndexMinPQ<Key> copy;

        public HeapIterator() {
            copy = new IndexMinPQ<Key>(priorityQueue.length - 1);
            for (int i = 1; i <= size; i++)
                copy.insert(priorityQueue[i], priorities[priorityQueue[i]]);
        }

        public boolean hasNext() {
            return !copy.isEmpty();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Integer next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMin();
        }
    }
}
