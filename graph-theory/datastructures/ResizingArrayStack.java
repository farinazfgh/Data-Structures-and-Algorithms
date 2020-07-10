package datastructures;

/******************************************************************************
 *  Stack implementation with a resizing array.
 *  to be or not to - be - - that - - - is
 ******************************************************************************/

import util.StdIn;
import util.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This implementation uses a resizing array.
 * Grow: Double the underlying array when it is full
 * Shrink: halves the underlying array when it is one-quarter full.
 * The push and pop operations take constant amortized time.
 * The size, peek, and is-empty operations takes constant time in the worst case.
 */
public class ResizingArrayStack<Item> implements Iterable<Item> {
    private Item[] array;
    private int size;


    public ResizingArrayStack() {
        array = (Item[]) new Object[2];
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void resize(int capacity) {
        assert capacity >= size;

        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            copy[i] = array[i];
        }
        array = copy;

        // alternative implementation
        // array = java.util.Arrays.copyOf(array, capacity);
        // System.arraycopy(array, 0, copy, 0);
    }


    public void push(Item item) {
        if (size == array.length) resize(2 * array.length);
        array[size++] = item;
    }

    public Item pop() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = array[size - 1];
        array[size - 1] = null;                              // to avoid loitering
        size--;
        if (size > 0 && size == array.length / 4) resize(array.length / 2);
        return item;
    }


    public Item peek() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        return array[size - 1];
    }

    public Iterator<Item> iterator() {
        return new ReverseArrayIterator();
    }

    private class ReverseArrayIterator implements Iterator<Item> {
        private int i;

        public ReverseArrayIterator() {
            i = size - 1;
        }

        public boolean hasNext() {
            return i >= 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return array[i--];
        }
    }


    public static void main(String[] args) {
        ResizingArrayStack<String> stack = new ResizingArrayStack<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) stack.push(item);
            else if (!stack.isEmpty()) StdOut.print(stack.pop() + " ");
        }
        StdOut.println("(" + stack.size() + " left on stack)");
    }
}


