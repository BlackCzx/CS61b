public class ArrayDeque<T> implements Deque<T> {

    private T[] items;
    private int size;
    private int capacityOfArray;
    private int nextFirst;
    private int nextLast;

    private double sizeUpFactor;
    private double sizeDownFactor;

    public ArrayDeque() {
        capacityOfArray = 8;
        items = (T[]) new Object[capacityOfArray];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
        sizeUpFactor = 2;
        sizeDownFactor = 0.5;
    }

    /** Copy items from first to last in a circular way to newItems from 0 to size-1,
     *  and renew the capacity. */
    private void copyCirArray(int first, int last, double reFactor) {
        if (isEmpty()) {
            return;
        }
        int n = 0;
        int newCapacityOfArray = (int) (capacityOfArray * reFactor);
        T[] newItems = (T[]) new Object[newCapacityOfArray];
        int i = first;
        while (i != last) {
            newItems[n] = items[i];
            i = (i + 1) % capacityOfArray;
            n++;
        }
        newItems[n] = items[i];
        items = newItems;
        capacityOfArray = newCapacityOfArray;
        newItems = null;
    }

    private void resize(double reFactor) {
        int first = (nextFirst + 1) % capacityOfArray;
        int last = (nextLast - 1 + capacityOfArray) % capacityOfArray;
        copyCirArray(first, last, reFactor);
        first = 0;
        last = size - 1;
        nextFirst = (first - 1 + capacityOfArray) % capacityOfArray;
        nextLast = (last + 1) % capacityOfArray;
    }

    @Override
    public void addFirst(T x) {
        items[nextFirst] = x;
        size++;
        nextFirst = (nextFirst - 1 + capacityOfArray) % capacityOfArray;
        if (size == capacityOfArray) {
            resize(sizeUpFactor);
        }
    }

    @Override
    public void addLast(T x) {
        items[nextLast] = x;
        size++;
        nextLast = (nextLast + 1) % capacityOfArray;
        if (size == capacityOfArray) {
            resize(sizeUpFactor);
        }
    }

    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        if (isEmpty()) {
            return;
        }
        int first = (nextFirst + 1) % capacityOfArray;
        int last = (nextLast - 1 + capacityOfArray) % capacityOfArray;
        int i = first;
        while (i != last) {
            System.out.println(items[i] + " ");
            i = (i + 1) % capacityOfArray;
        }
        System.out.println(items[i]);
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        int first = (nextFirst + 1) % capacityOfArray;
        T ret = items[first];
        size--;
        nextFirst = first;
        if ((double) size / (double) capacityOfArray <= 0.25) {
            resize(sizeDownFactor);
        }
        return ret;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        int last = (nextLast - 1 + capacityOfArray) % capacityOfArray;
        T ret = items[last];
        size--;
        nextLast = last;
        if ((double) size / (double) capacityOfArray <= 0.25) {
            resize(sizeDownFactor);
        }
        return ret;
    }

    @Override
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        int first = (nextFirst + 1) % capacityOfArray;
        int realIndex = (first + index) % capacityOfArray;
        return items[realIndex];
    }

}
