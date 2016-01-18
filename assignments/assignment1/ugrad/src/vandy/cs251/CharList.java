package vandy.cs251;

import java.lang.IndexOutOfBoundsException;

/**
 * Provides a wrapper facade around primitive char lists, allowing
 * for dynamic resizing.
 */
public class CharList 
             implements Comparable<CharList>, 
                        Cloneable {
    /**
     * The head of the list.
     */
    // TODO - you fill in here
    Node myHead;

    /**
     * The current size of the list.
     */
    // TODO - you fill in here
    int mySize;
    /**
     * Default value for elements in the list.
     */
    // TODO - you fill in here
    char myValue;

    /**
     * Constructs an list of the given size.    (Good)
     *
     * @param size Non-negative integer size of the desired list.
     * @throw IndexOutOfBoundsException If size < 0.
     */
    public CharList(int size) {
        // TODO - you fill in here.  Initialize the List
        this(size, '\0');
    }

    /**
     * Constructs an list of the given size, filled with the provided   (Good)
     * default value.
     *
     * @param size Nonnegative integer size of the desired list.
     * @param defaultValue A default value for the list.
     * @throw IndexOutOfBoundsException If size < 0.
     */
    public CharList(int size, char defaultValue) {
        // TODO - you fill in here
        if (size < 0) {
            throw new IndexOutOfBoundsException("Size cannot be negative!");
        }
        mySize = size;
        myValue = defaultValue;
        Node tmp;
        for (int i = 0; i < size; i++) {
            tmp = new Node();
            tmp.myChar = defaultValue;
            tmp.next = myHead;
            myHead = tmp;
        }
    }

    /**
     * Copy constructor; creates a deep copy of the provided CharList.  (Good)
     *
     * @param s The CharList to be copied.
     */
    public CharList(CharList s) {
        // TODO - you fill in here
        mySize = s.mySize;
        myValue = s.myValue;
        //copy head Node
        if (s.myHead != null) {
            myHead = new Node();
            myHead.myChar = s.myHead.myChar;
            //copy rest of list (if exists)
            Node tracker = s.myHead;
            Node prev = myHead;
            while (tracker.next != null) {
                tracker = tracker.next;
                Node tmp = new Node(tracker.myChar, prev);
                prev = prev.next;
            }
        }
    }

    /**
     * Creates a deep copy of this CharList.  Implements the
     * Prototype pattern.
     */
    @Override
    public Object clone() {
        // TODO - you fill in here (replace return null with right
        // implementation).
    	return (Object) new CharList(this);
    }

    /**
     * @return The current size of the list.    (Good)
     */
    public int size() {
        // TODO - you fill in here (replace return 0 with right
        // implementation).
    	return mySize;
    }

    /**
     * Resizes the list to the requested size.      (Good)
     *
     * Changes the capacity of this list to hold the requested number of elements.
     * Note the following optimizations/implementation details:
     * <ul>
     *   <li> If the requests size is smaller than the current maximum capacity, new memory
     *   is not allocated.
     *   <li> If the list was constructed with a default value, it is used to populate
     *   uninitialized fields in the list.
     * </ul>
     * @param size Nonnegative requested new size.
     */
    public void resize(int size) {
        // TODO - you fill in here
        if (size > mySize) {
            //initialize myHead
            if (mySize == 0) {
                myHead = new Node();
                myHead.myChar = myValue;
            }
            //initialize rest of list w/ default value
            Node prev = myHead;
            for (int i = 1; i < size; i++) {
                Node tmp = new Node(myValue, prev);
                prev = prev.next;
            }
            mySize = size;
        }
    }

    /**
     * @return the element at the requested index.  (Good)
     * @param index Nonnegative index of the requested element.
     * @throws IndexOutOfBoundsException If the requested index is outside the
     * current bounds of the list.
     */
    public char get(int index) {
        // TODO - you fill in here (replace return '\0' with right
        // implementation).
        return seek(index).myChar;
    }

    /**
     * Sets the element at the requested index with a provided value.   (Good)
     * @param index Nonnegative index of the requested element.
     * @param value A provided value.
     * @throws IndexOutOfBoundsException If the requested index is outside the
     * current bounds of the list.
     */
    public void set(int index, char value) {
        // TODO - you fill in here
        seek(index).myChar = value;
    }

    /**
     * Locate and return the @a Node at the @a index location.  (Good)
     */
    private Node seek(int index) {
        // TODO - you fill in here
        rangeCheck(index);
        Node tmp = myHead;
        for (int i = 0; i < index; i++) {
            tmp = tmp.next;
        }
        return tmp;
    }

    /**
     * Compares this list with another list.
     * <p>
     * This is a requirement of the Comparable interface.  It is used to provide
     * an ordering for CharList elements.
     * @return a negative value if the provided list is "greater than" this list,
     * zero if the lists are identical, and a positive value if the
     * provided list is "less than" this list. These lists should be compred
     * lexicographically.
     */
    @Override
    public int compareTo(CharList s) {
        // TODO - you fill in here (replace return 0 with right
        // implementation).
	    Node tmp = myHead;
        Node comparator = s.myHead;
        while (tmp != null && comparator != null) {
            if (tmp.myChar == comparator.myChar) {
                tmp = tmp.next;
                comparator = comparator.next;
            }
            else if (tmp.myChar > comparator.myChar) {
                return 1;
            }
            else {
                return -1;
            }
        }
        if (tmp == null && comparator == null) {
            return 0;
        }
        else if (tmp == null) {
            return -1;
        }
        else {
            return 1;
        }
    }

    /**
     * Throws an exception if the index is out of bound.
     */
    private void rangeCheck(int index) {
        // TODO - you fill in here
        if (index < 0 || index >= mySize) {
            throw new IndexOutOfBoundsException("Index out of bounds!");
        }
    }

    /**
     * A Node in the Linked List.
     */
    private class Node {
        /**
         * Value stored in the Node.
         */
	// TODO - you fill in here
        char myChar;
        /**
         * Reference to the next node in the list.
         */
	// TODO - you fill in here
        Node next;
        /**
         * Default constructor (no op).
         */
        Node() {
        }

        /**
         * Construct a Node from a @a prev Node.
         */
        Node(Node prev) {
            // TODO - you fill in here
            myChar = prev.myChar;
            next = prev.next;
            prev.next = this;
        }

        /**
         * Construct a Node from a @a value and a @a prev Node.
         */
        Node(char value, Node prev) {
            // TODO - you fill in here
            myChar = value;
            next = prev.next;
            prev.next = this;
        }

        /**
         * Ensure all subsequent nodes are properly deallocated.
         */
        void prune() {
            // TODO - you fill in here
            // Leaving the list fully linked could *potentially* cause
            // a pathological performance issue for the garbage
            // collector.
            Node tmp = this;
            Node next = null;
            while (tmp != null) {
                next = tmp.next;
                tmp.next = null;
                tmp = next;
            }

        }
    }
}
