package synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        rb = (T[]) new Object[capacity];
        first = 0;
        last = 0;
        this.fillCount = 0;
        this.capacity = capacity;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        if (isFull()) {
            throw new RuntimeException("Ring Buffer Overflow");
        } else {
            rb[last] = x;
            last++;
            fillCount++;
            if (last == capacity) {
                last = 0;
            }
        }
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        T tmp;
        if (isEmpty()) {
            throw  new RuntimeException("Ring Buffer Underflow");
        } else {
            tmp = rb[first];
            rb[first] = null;
            first++;
            fillCount--;
            if (first == capacity) {
                first = 0;
            }
        }
        return tmp;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        if (isEmpty()) {
            throw  new RuntimeException("Ring Buffer Underflow");
        } else {
            return rb[first];
        }
    }


    private class MyIterator implements Iterator<T> {
        int myFirst;
        int myLast;

        MyIterator() {
            myFirst = first;
            myLast = last;
        }

        public boolean hasNext() {
            return myFirst != myLast;
        }

        public T next() {
            T ret;
            ret = rb[myFirst];
            myFirst++;
            if (myFirst == capacity) {
                myFirst = 0;
            }
            return ret;
        }

    }

    public Iterator<T> iterator() {
        return new MyIterator();
    }
}
