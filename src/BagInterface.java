/**
 * BagInterface defines the core operations for the ADT Bag.
 *
 * <p>IMPORTANT (Project 1): You will add and implement the following 3 operations:
 * <ul>
 *   <li>union</li>
 *   <li>intersection</li>
 *   <li>difference</li>
 * </ul>
 *
 * <p>Conventions:
 * <ul>
 *   <li>A "bag" is an unordered collection that can contain duplicates.</li>
 *   <li><b>Null elements are not allowed</b>. Methods should not store {@code null} entries.</li>
 *   <li>All 3 operations must NOT modify either operand bag.</li>
 *   <li>If {@code otherBag} is {@code null}, throw {@link IllegalArgumentException}
 *       with a meaningful message.</li>
 * </ul>
 *
 * @param <T> element type stored in this bag
 */
public interface BagInterface<T> {

    /**
     * Gets the current number of entries in this bag.
     * @return the integer number of entries currently in the bag
     */
    int getCurrentSize();

    /**
     * Checks whether this bag is empty.
     * @return true if the bag is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Adds a new entry to this bag.
     * @param newEntry the object to be added as a new entry
     * @return true if the addition is successful, false otherwise
     */
    boolean add(T newEntry);

    /**
     * Removes one unspecified entry from this bag, if possible.
     * @return either the removed entry, if the removal was successful, or null
     */
    T remove();

    /**
     * Removes one occurrence of a given entry from this bag.
     * @param anEntry the entry to be removed
     * @return true if the removal was successful, false otherwise
     */
    boolean remove(T anEntry);

    /** Removes all entries from this bag. */
    void clear();

    /**
     * Counts the number of times a given entry appears in this bag.
     * @param anEntry the entry to be counted
     * @return the number of times anEntry appears in the bag
     */
    int getFrequencyOf(T anEntry);

    /**
     * Tests whether this bag contains a given entry.
     * @param anEntry the entry to locate
     * @return true if the bag contains anEntry, false otherwise
     */
    boolean contains(T anEntry);

    /**
     * Retrieves all entries that are in this bag.
     * @return a newly allocated array of all entries in the bag
     */
    T[] toArray();

    // ===================== Project 1: NEW OPERATIONS =====================

    /**
     * Returns a new bag containing the union of this bag and {@code otherBag}.
     *
     * <p>For bags, union keeps duplicates: the result contains all entries from this bag
     * plus all entries from {@code otherBag}. For example:
     * <pre>
     *   {1, 2, 2} U {2, 3} = {1, 2, 2, 2, 3}
     * </pre>
     *
     * @param otherBag another bag
     * @return a new bag containing the union result
     * @throws IllegalArgumentException if otherBag is null
     */
    BagInterface<T> union(BagInterface<T> otherBag);

    /**
     * Returns a new bag containing the intersection of this bag and {@code otherBag}.
     *
     * <p>For bags, intersection keeps duplicates up to the minimum frequency in both bags.
     * For example:
     * <pre>
     *   {1, 2, 2} ∩ {2, 2, 3} = {2, 2}
     * </pre>
     *
     * @param otherBag another bag
     * @return a new bag containing the intersection result
     * @throws IllegalArgumentException if otherBag is null
     */
    BagInterface<T> intersection(BagInterface<T> otherBag);

    /**
     * Returns a new bag containing the difference of this bag minus {@code otherBag}.
     *
     * <p>For bags, difference removes duplicates up to the frequency in {@code otherBag}.
     * For example:
     * <pre>
     *   {1, 2, 2, 3} - {2, 3, 3} = {1, 2}
     * </pre>
     *
     * @param otherBag another bag
     * @return a new bag containing the difference result
     * @throws IllegalArgumentException if otherBag is null
     */
    BagInterface<T> difference(BagInterface<T> otherBag);
}