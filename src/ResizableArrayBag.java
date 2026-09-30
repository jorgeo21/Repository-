/**
 * ResizableArrayBag is an array-backed implementation of BagInterface.
 *
 * <p>NOTE: This file is part of the starter code. Do not rewrite the bag ADT.
 * For Project 1 you should ONLY add the required logic inside union/intersection/difference.
 *
 * @param <T> element type stored in this bag
 */
public class ResizableArrayBag<T> implements BagInterface<T> {
    private T[] bag;
    private int numberOfEntries;
    private boolean integrityOK = false;
    private static final int DEFAULT_CAPACITY = 25;
    private static final int MAX_CAPACITY = 10000;

    public ResizableArrayBag() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public ResizableArrayBag(int initialCapacity) {
        if (initialCapacity < DEFAULT_CAPACITY) {
            initialCapacity = DEFAULT_CAPACITY;
        } else {
            checkCapacity(initialCapacity);
        }

        bag = (T[]) new Object[initialCapacity];
        numberOfEntries = 0;
        integrityOK = true;
    }

    @Override
    public int getCurrentSize() {
        return numberOfEntries;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public boolean add(T newEntry) {
        checkIntegrity();
        if (newEntry == null) {
            throw new IllegalArgumentException("Null elements are not allowed");
        }
        if (isArrayFull()) {
            doubleCapacity();
        }

        bag[numberOfEntries] = newEntry;
        numberOfEntries++;
        return true;
    }

    @Override
    public T remove() {
        checkIntegrity();
        if (isEmpty()) {
            return null;
        }
        return removeEntry(numberOfEntries - 1);
    }

    @Override
    public boolean remove(T anEntry) {
        checkIntegrity();
        if (anEntry == null) {
            throw new IllegalArgumentException("Null entries are not allowed");
        }
        int index = getIndexOf(anEntry);
        if (index > -1) {
            removeEntry(index);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        while (!isEmpty()) {
            remove();
        }
    }

    @Override
    public int getFrequencyOf(T anEntry) {
        checkIntegrity();
        if (anEntry == null) {
            throw new IllegalArgumentException("Null entries are not allowed");
        }
        int counter = 0;
        for (int index = 0; index < numberOfEntries; index++) {
            if (anEntry.equals(bag[index])) {
            counter++;
            }
        }
        return counter;
    }

    @Override
    public boolean contains(T anEntry) {
        checkIntegrity();
        if (anEntry == null) {
            throw new IllegalArgumentException("Null entries are not allowed");
        }
        return getIndexOf(anEntry) > -1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        checkIntegrity();
        T[] result = (T[]) new Object[numberOfEntries];
        for (int index = 0; index < numberOfEntries; index++) {
            result[index] = bag[index];
        }
        return result;
    }

    // ===================== Project 1: implement these =====================

    @Override
    public BagInterface<T> union(BagInterface<T> otherBag) {
        if (otherBag == null) {
            throw new IllegalArgumentException("union: otherBag must not be null");
        }
        throw new UnsupportedOperationException("TODO: implement union()");
    }

    @Override
    public BagInterface<T> intersection(BagInterface<T> otherBag) {
        throw new UnsupportedOperationException("TODO: implement intersection()");
    }

    @Override
    public BagInterface<T> difference(BagInterface<T> otherBag) {
        throw new UnsupportedOperationException("TODO: implement difference()");
    }

    // ===================== Private helpers =====================

    private void checkIntegrity() {
        if (!integrityOK) {
            throw new SecurityException("ResizableArrayBag object is corrupt.");
        }
    }

    private boolean isArrayFull() {
        return numberOfEntries >= bag.length;
    }

    private T removeEntry(int index) {
        assert index >= 0 && index < numberOfEntries : "Index out of bounds";
        T result = bag[index];
        bag[index] = bag[numberOfEntries - 1];
        bag[numberOfEntries - 1] = null;
        numberOfEntries--;
        return result;
    }

    private int getIndexOf(T anEntry) {
        assert anEntry != null : "anEntry must not be null";
        for (int index = 0; index < numberOfEntries; index++) {
            if (anEntry.equals(bag[index])) {
                return index;
            }
        }
        return -1;
    }

    private void doubleCapacity() {
        checkIntegrity();
        int newLength = 2 * bag.length;
        checkCapacity(newLength);
        bag = java.util.Arrays.copyOf(bag, newLength);
    }

    private void checkCapacity(int capacity) {
        if (capacity > MAX_CAPACITY) {
            throw new IllegalStateException("Requested capacity exceeds MAX_CAPACITY.");
        }
    }
}