public class LinkedListDeque<T> {

    private class Node {
        private T item;
        private Node prev;
        private Node next;

        public Node(T i, Node p, Node n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel.prev;
        size = 0;
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public void addFirst(T x) {
        Node newNode = new Node(x, null, null);
        Node tmp = sentinel.next;
        newNode.next = tmp;
        tmp.prev = newNode;
        sentinel.next = newNode;
        newNode.prev = sentinel;
        size++;
    }

    public void addLast(T x) {
        Node newNode = new Node(x, null, null);
        Node tmp = sentinel.prev;
        newNode.prev = tmp;
        tmp.next = newNode;
        sentinel.prev = newNode;
        newNode.next = sentinel;
        size++;
    }

    public T removeFirst() {
        Node first = sentinel.next;
        if (first == sentinel) {
            return null;
        }
        Node tmp = first.next;
        T ret = first.item;
        sentinel.next = tmp;
        tmp.prev = sentinel;
        first = null;
        size--;
        return ret;
    }

    public T removeLast() {
        Node last = sentinel.prev;
        if (last == sentinel) {
            return null;
        }
        Node tmp = last.prev;
        T ret = last.item;
        sentinel.prev = tmp;
        tmp.next = sentinel;
        last = null;
        size--;
        return ret;
    }

    public void printDeque() {
        Node tmp = sentinel.next;
        while (tmp != sentinel) {
            System.out.println(tmp.item+" ");
            tmp = tmp.next;
        }
    }

    public T get(int index) {
        Node tmp = sentinel;
        if (index > size - 1) {
            return null;
        }
        for (int i = index; i >= 0; i--) {
            tmp = tmp.next;
        }
        return tmp.item;
    }

    public T getRecursive(int index) {
        return getRecursiveHelper(sentinel.next, index);
    }

    private T getRecursiveHelper(Node front, int index) {
        if (front == sentinel) {
            return null;
        } else if (index == 0) {
            return front.item;
        } else {
            return getRecursiveHelper(front.next, index - 1);
        }
    }

}
