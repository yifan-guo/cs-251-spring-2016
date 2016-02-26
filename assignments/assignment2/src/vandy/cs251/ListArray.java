package vandy.cs251;

import java.lang.ArrayIndexOutOfBoundsException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Provides a generic dynamically-(re)sized array abstraction.
 */
public class ListArray<T extends Comparable<T>>
        implements Comparable<ListArray<T>>,
        Iterable<T> {
    /**
     * The underlying list of type T.
     */
    // TODO - you fill in here.
    private Node myHead = new Node();
    /**
     * The current size of the array.
     */
    // TODO - you fill in here.
    private int mySize;
    /**
     * Default value for elements in the array.
     */
    // TODO - you fill in here.
    private T myValue;
    /**
     * Constructs an array of the given size.
     * @param size Nonnegative integer size of the desired array.
     * @throws NegativeArraySizeException if the specified size is
     *         negative.
     */
    @SuppressWarnings("unchecked")
    public ListArray(int size) throws NegativeArraySizeException {
        // TODO - you fill in here.
        // @@ Good job creating a helper method.
        // @@ Helper should probably have a better name:
        // Student Response: changed helper function name to helperConstructor()
        helperConstructor(size, myValue);
    }

    /**
     * Constructs an array of the given size, filled with the provided
     * default value.
     * @param size Nonnegative integer size of the desired array.
     * @param defaultValue A default value for the array.
     * @throws NegativeArraySizeException if the specified size is
     *         negative.
     */
    public ListArray(int size,
                     T defaultValue) throws NegativeArraySizeException {
        // TODO - you fill in here.
        myValue = defaultValue;
        helperConstructor(size, myValue);
    }

    /**
     * Constructs an array of the given size, filled with the provided
     * value.
     * @param size Nonnegative integer size of the desired array.
     * @param value A default value for the array.
     * @throws NegativeArraySizeException if the specified size is
     *         negative.
     */
    private void helperConstructor(int size, T value) throws NegativeArraySizeException {
        sizeCheck(size);
        mySize = size;
        for (int i = 0; i < size; i++) {
            new Node(value, myHead);
        }
    }

    /**
     * Throws an exception if the size is negative.
     */
    private void sizeCheck(int size) {
        if (size < 0) {
            throw new NegativeArraySizeException();
        }
    }

    /**
     * Copy constructor; creates a deep copy of the provided array.
     * @param s The array to be copied.
     */
    public ListArray(ListArray<T> s) {
        // TODO - you fill in here.
        mySize = s.mySize;
        myValue = s.myValue;
        Node prev = myHead;
        for (Node x : s.myHead) {
            new Node(x.item, prev);
            prev = prev.next;
        }
        /*for (Iterator<T> it = s.iterator(); it.hasNext(); ) {
            new Node(it.next(), prev);
            prev = prev.next;
        }*/
    }

    /**
     * @return The current size of the array.
     */
    public int size() {
        // TODO - you fill in here (replace 0 with proper return
        // value).
        return mySize;
    }

    /**
     * Resizes the array to the requested size.
     *
     * Changes the size of this ListArray to hold the requested number of elements.
     * @param size Nonnegative requested new size.
     */
    public void resize(int size) {
        // TODO - you fill in here.
        sizeCheck(size);
        if (size < mySize) {            //shrink
            if (size == 0) {
                myHead.prune();
            }
            else {
                seek(size - 1).prune();
            }
        }
        else if (size > mySize) {       //grow
            Node tmp = myHead;
            if (mySize != 0) {
                tmp = seek(mySize - 1);
            }

            for (int i = mySize; i < size; i++) {
                new Node(myValue, tmp);
                tmp = tmp.next;
            }
        }
        mySize = size;
    }

    /**
     * @return the element at the requested index.
     * @param index Nonnegative index of the requested element.
     * @throws ArrayIndexOutOfBoundsException If the requested index is outside the
     * current bounds of the array.
     */
    public T get(int index) {
        // TODO - you fill in here (replace null with proper return
        // value).
        return seek(index).item;
    }

    /**
     * Sets the element at the requested index with a provided value.
     * @param index Nonnegative index of the requested element.
     * @param value A provided value.
     * @throws ArrayIndexOutOfBoundsException If the requested index is outside the
     * current bounds of the array.
     */
    public void set(int index, T value) {
        // TODO - you fill in here.
        seek(index).item = value;
    }

    private Node seek(int index) {
        // TODO - you fill in here.
        return null;
    }

    /**
     * Removes the element at the specified position in this ListArray.
     * Shifts any subsequent elements to the left (subtracts one from their
     * indices).  Returns the element that was removed from the ListArray.
     *
     * @param index the index of the element to remove
     * @return element that was removed
     * @throws ArrayIndexOutOfBoundsException if the index is out of range.
     */
    public T remove(int index) {
        // TODO - you fill in here (replace null with proper return
        // value).
        rangeCheck(index);
        Iterator<T> it = iterator();    //list iterator
        T tmp = it.next();              //in case index == 0, must call next() before remove()
        for (int i = 0; i < index; i++) {
            tmp = it.next();
        }
        it.remove();
        return tmp;
    }

    /**
     * @throws ArrayIndexOutOfBoundsException If the requested index is outside the
     * current bounds of the array.
     */
    private Node seek(int index) {
        // TODO - you fill in here
        rangeCheck(index);
        Iterator<Node> it = myHead.iterator();
        for (int i = 0; i < index; i++, it.next()) {}
        return it.next();
    }

    /**
     * Compares this array with another array.
     * <p>
     * This is a requirement of the Comparable interface.  It is used to provide
     * an ordering for ListArray elements.
     * @return a negative value if the provided array is "greater than" this array,
     * zero if the arrays are identical, and a positive value if the
     * provided array is "less than" this array.
     */
    @Override
    public int compareTo(ListArray<T> s) {
        // TODO - you fill in here (replace 0 with proper return
        // value).
        Iterator<T> it = iterator();
        Iterator<T> other = s.iterator();
        for (int i = 0; i < Math.min(mySize, s.mySize); i++) {
            // @@ Well done!
            int value = it.next().compareTo(other.next());
            if (value != 0) {
                return value;
            }
        }
        return mySize - s.mySize;
    }

    /**
     * Throws an exception if the index is out of bound.
     */
    private void rangeCheck(int index) {
        // TODO - you fill in here.
        if (index < 0 || index >= mySize) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    /**
     * Factory method that returns an Iterator.
     */
    public Iterator<T> iterator() {
        // TODO - you fill in here (replace null with proper return value).
        return new ListIterator();
    }

    private class Node implements Iterable<Node> {
        // TODO: Fill in any fields you require.

        // SP: -1, prefix with 'm'
        /**
         * Value stored in the Node.
         */
        // TODO - you fill in here
        private T item;
        /**
         * Reference to the next node in the list.
         */
        // TODO - you fill in here
        private Node next;
        /**
         * Default constructor (no op).
         */
        Node() {
            // TODO - you fill in here.
        }

        /**
         * Construct a Node from a @a prev Node.
         */
        Node(Node prev) {
            // TODO - you fill in here.
            this(myValue, prev);
        }

        /**
         * Construct a Node from a @a value and a @a prev Node.
         */
        Node(T value, Node prev) {
            // TODO - you fill in here.
            item = value;
            if (prev != null) {
                next = prev.next;
                prev.next = this;
            }
        }

        /**
         * Ensure all subsequent nodes are properly deallocated.
         */
        void prune() {
            // TODO - you fill in here.
            Node tmp = this;
            while (tmp != null) {
                Node next = tmp.next;
                tmp.next = null;
                tmp = next;
            }
        }

        @Override
        public Iterator<Node> iterator() {
            // TODO - you fill in here.
            return new NodeIterator();
        }
    }

    private class NodeIterator implements Iterator<Node> {
        // TODO: Fill in any fields you require.
        private int currentIndex, lastRemovedIndex = 0; // SP: -3, unnecessary variables
        private Node cur = myHead;
        private Node prev;

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return currentIndex < mySize;
        } // SP: check if cur.nxt == null

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public Node next() {
            if (hasNext()) {
                prev = cur;
                cur = cur.next;
                currentIndex++;
                return cur;
            }
            throw new NoSuchElementException();
        }

        /**
         * Removes from the underlying collection the last element returned
         * by this iterator (optional operation).  This method can be called
         * only once per call to {@link #next}.  The behavior of an iterator
         * is unspecified if the underlying collection is modified while the
         * iteration is in progress in any way other than by calling this
         * method.
         *
         * @throws UnsupportedOperationException if the {@code remove}
         *                                       operation is not supported by this iterator
         * @throws IllegalStateException         if the {@code next} method has not
         *                                       yet been called, or the {@code remove} method has already
         *                                       been called after the last call to the {@code next}
         *                                       method
         * @implSpec The default implementation throws an instance of
         * {@link UnsupportedOperationException} and performs no other action.
         */
        @Override
        public void remove() {
            if (currentIndex == lastRemovedIndex) {
                throw new IllegalStateException();
            }
            cur = cur.next;
            prev.next = cur;
            cur = prev;
            currentIndex--;
            lastRemovedIndex = currentIndex;
            mySize--;
        }
    }

    /**
     * @brief This class implements an iterator for the list.
     */
    private class ListIterator implements Iterator<T> {
        // TODO: Fill in any fields you require.
        private NodeIterator it = new NodeIterator();

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public T next() {
            return it.next().item;
        }

        /**
         * Removes from the underlying collection the last element returned
         * by this iterator (optional operation).  This method can be called
         * only once per call to {@link #next}.  The behavior of an iterator
         * is unspecified if the underlying collection is modified while the
         * iteration is in progress in any way other than by calling this
         * method.
         *
         * @throws UnsupportedOperationException if the {@code remove}
         *                                       operation is not supported by this iterator
         * @throws IllegalStateException         if the {@code next} method has not
         *                                       yet been called, or the {@code remove} method has already
         *                                       been called after the last call to the {@code next}
         *                                       method
         * @implSpec The default implementation throws an instance of
         * {@link UnsupportedOperationException} and performs no other action.
         */
        @Override
        public void remove() {
            it.remove();
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return it.hasNext();
        }
    }
}
