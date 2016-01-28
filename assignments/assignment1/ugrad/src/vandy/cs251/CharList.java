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
    private Node myHead;

    /**
     * The current size of the list.
     */
    // TODO - you fill in here
    private int mySize;
    /**
     * Default value for elements in the list.
     */
    // TODO - you fill in here
    private char myValue;

    /**
     * Constructs an list of the given size.
     *
     * @param size Non-negative integer size of the desired list.
     * @throw IndexOutOfBoundsException If size < 0.
     */
    public CharList(int size) {
        // TODO - you fill in here.  Initialize the List
        this(size, '\0');
    }

    /**
     * Constructs an list of the given size, filled with the provided
     * default value.
     *
     * @param size Nonnegative integer size of the desired list.
     * @param defaultValue A default value for the list.
     * @throw IndexOutOfBoundsException If size < 0.
     */
    public CharList(int size, char defaultValue) {
        // TODO - you fill in here
        sizeCheck(size);
        mySize = size;
        myValue = defaultValue;
        for (int i = 0; i < size; i++) {
            // @@ Use the Node(Node prev) constructor.
            Node tmp = new Node(defaultValue, myHead);
            if (myHead == null) {
                myHead = tmp;
            }
        }
    }

    /**
     * Copy constructor; creates a deep copy of the provided CharList.
     *
     * @param s The CharList to be copied.
     */
    public CharList(CharList s) {
        // TODO - you fill in here
        mySize = s.mySize;
        myValue = s.myValue;
        //copy head Node
        if (s.mySize != 0) {
            myHead = new Node();
            myHead.myChar = s.myHead.myChar;
            //copy rest of list (if exists)
            Node tracker = s.myHead;
            Node prev = myHead;
            for (int i = 1; i < s.mySize; i++) {
                tracker = tracker.next;
                new Node(tracker.myChar, prev);
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
        return new CharList(this);
    }

    /**
     * @return The current size of the list.
     */
    public int size() {
        // TODO - you fill in here (replace return 0 with right
        // implementation).
        return mySize;
    }

    /**
     * Resizes the list to the requested size.
     * @throw IndexOutOfBoundsException If size < 0.    (I ADDED THIS LINE!)
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
        sizeCheck(size);
        if (size > mySize) {
            //initialize myHead
            if (mySize == 0) {
                myHead = new Node();
                myHead.myChar = myValue;
                mySize++;
            }
            //initialize rest of list w/ default value
            Node prev = seek(mySize-1); //get the last Node in the list
            for (int i = mySize; i < size; i++) {
                new Node(myValue, prev);
                prev = prev.next;
            }
        }
        else if (size < mySize) {
            if (size == 0) {
                myHead.prune();
                myHead = null;
            }
            else {
                seek(size - 1).prune();
            }
        }
        else {  //size == mySize
            return;
        }
        mySize = size;
    }

    /**
     * @return the element at the requested index.
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
     * Sets the element at the requested index with a provided value.
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
     * Locate and return the @a Node at the @a index location.
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
        Node cur = myHead;
        Node other = s.myHead;
        for (int i = 0; i < Math.min(mySize, s.mySize); i++, cur = cur.next, other= other.next) {
          // @@ This implementation is inefficient since you need
          // iterating through the list via seek().
            int value = cur.myChar - other.myChar;
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
        // TODO - you fill in here
        if (index < 0 || index >= mySize) {
            throw new IndexOutOfBoundsException("Index out of bounds!");
        }
    }

    //private helper method
    /**
     * Throws an exception if the size parameter is negative.
     */
    private void sizeCheck(int size) {
        // TODO - you fill here
        if (size < 0) {
            throw new IndexOutOfBoundsException("Size cannot be negative!");
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
        private char myChar;
        /**
         * Reference to the next node in the list.
         */
        // TODO - you fill in here
        private Node next;
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
            //myChar is null character by default
            this('\0', prev);
        }

        /**
         * Construct a Node from a @a value and a @a prev Node.
         */
        Node(char value, Node prev) {
            // TODO - you fill in here
            myChar = value;
            if (prev!= null) {
                next = prev.next;
                prev.next = this;
            }
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



