/**
 * LinkedBag is a singly-linked implementation of BagInterface.
 *
 * <p>NOTE: This file is part of the starter code. Do not rewrite the bag ADT.
 * For Project 1 you should ONLY add the required logic inside union/intersection/difference.
 *
 * @param <T> element type stored in this bag
 */
public class LinkedBag<T> implements BagInterface<T> {

    private Node firstNode;       // reference to first node
    private int numberOfEntries;

    public LinkedBag() {
        firstNode = null;
        numberOfEntries = 0;
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
        if (newEntry == null) {
            throw new IllegalArgumentException("Null elements are not allowed");
        }
        Node newNode = new Node(newEntry);
        newNode.next = firstNode;
        firstNode = newNode;
        numberOfEntries++;
        return true;
    }

    @Override
    public T remove() {
        if (isEmpty()) {
            return null;
        }

        T result = firstNode.data;
        firstNode = firstNode.next;
        numberOfEntries--;
        return result;
    }

    @Override
    public boolean remove(T anEntry) {
        if (anEntry == null) {
            throw new IllegalArgumentException("Null entries are not allowed");
        }

        if (isEmpty()) {
            return false;
        }

        // Find a node that contains anEntry
        Node nodeN = getReferenceTo(anEntry);
        if (nodeN == null) {
            return false;
        }

        // Overwrite nodeN with firstNode's data, then remove firstNode
        nodeN.data = firstNode.data;
        firstNode = firstNode.next;
        numberOfEntries--;
        return true;
    }

    @Override
    public void clear() {
       firstNode = null;
            numberOfEntries = 0;
    }

    @Override
    public int getFrequencyOf(T anEntry) {
        if (anEntry == null) {
            throw new IllegalArgumentException("Null entries are not allowed");
        }
        
        int frequency = 0;
        Node currentNode = firstNode;
        while (currentNode != null) {
            if (anEntry.equals(currentNode.data)) {
                frequency++;
            }
            currentNode = currentNode.next;
        }
        return frequency;
    }

    @Override
    public boolean contains(T anEntry) {
        if (anEntry == null) {
            throw new IllegalArgumentException("Null entries are not allowed");
        }
        return getReferenceTo(anEntry) != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        T[] result = (T[]) new Object[numberOfEntries];
        int index = 0;
        Node currentNode = firstNode;
        while (currentNode != null && index < numberOfEntries) {
            result[index] = currentNode.data;
            index++;
            currentNode = currentNode.next;
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

    /**
     * Returns a reference to the first node in the chain that contains anEntry,
     * or null if the entry is not found.
     */
    private Node getReferenceTo(T anEntry) {
        assert anEntry != null : "Null entries are not allowed";
        Node currentNode = firstNode;
        while (currentNode != null) {
            if (anEntry == null ? currentNode.data == null : anEntry.equals(currentNode.data)) {
                return currentNode;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    private class Node {
        private T data;
        private Node next;

        private Node(T dataPortion) {
            this(dataPortion, null);
        }

        private Node(T dataPortion, Node nextNode) {
            data = dataPortion;
            next = nextNode;
        }
    }
}
