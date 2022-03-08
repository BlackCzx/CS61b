package lab9;

//import java.io.ObjectStreamException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */
    private Set<K> keys;
    private V removedValue;
    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        if (key.compareTo(p.key) < 0) {
            return getHelper(key, p.left);
        } else if (key.compareTo(p.key) > 0) {
            return getHelper(key, p.right);
        } else {
            return p.value;
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size++;
            return new Node(key, value);
        }
        if (key.compareTo(p.key) < 0) {
            p.left = putHelper(key, value, p.left);
        } else if (key.compareTo(p.key) > 0) {
            p.right = putHelper(key, value, p.right);
        } else {
            p.value = value;
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    private void keySetHelper(Node p) {
        if (p == null) {
            return;
        }
        keys.add(p.key);
        keySetHelper(p.left);
        keySetHelper(p.right);
    }

    @Override
    public Set<K> keySet() {
        keys = new HashSet<>();
        keySetHelper(root);
        return keys;
    }

    private Node findMaxSuccessor(Node p) {
        //Not find in an empty tree.
        if (p == null) {
            return null;
        }
        if (p.right == null) {
            return p;
        }
        return findMaxSuccessor(p.right);
    }


    private Node removeHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        if (key.compareTo(p.key) < 0) {
            p.left = removeHelper(key, p.left);
        } else if (key.compareTo(p.key) > 0) {
            p.right = removeHelper(key, p.right);
        } else {
            if (p.left != null && p.right != null) {
                removedValue = p.value;
                Node maxSuccessor = findMaxSuccessor(p.left);
                p.key = maxSuccessor.key;
                p.value = maxSuccessor.value;
                p.left = removeHelper(p.key, p.left);
            } else {
                if (p.left == null) {
                    p = p.right;
                } else if (p.right == null) {
                    p = p.left;
                }
            }
        }
        return p;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        root = removeHelper(key, root);
        return removedValue;
    }

    private Node getNode(K key, Node p) {
        if (p == null) {
            return null;
        }
        if (key.compareTo(p.key) < 0) {
            return getNode(key, p.left);
        } else if (key.compareTo(p.key) > 0) {
            return getNode(key, p.right);
        } else {
            return p;
        }
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/

    @Override
    public V remove(K key, V value) {
        Node p = getNode(key, root);
        if (p == null || p.value != value) {
            return null;
        }
        V tmp = p.value;
        p.value = null;
        return tmp;
    }

    public class MyIterator implements Iterator<K> {
        Queue q;

        private class Queue {
            Node[] queue;
            private int front;
            private int end;

            Queue() {
                queue = (Node[]) new Object[size];
                front = 0;
                end = 0;
            }

            public void enQueue(Node item) {
                if (item == null) {
                    return;
                }
                queue[end] = item;
                end++;
            }

            public Node deQueue() {
                Node tmp = queue[front];
                front++;
                return tmp;
            }

            public boolean isEmpty() {
                return front == end;
            }

        }

        public MyIterator() {
            q = new Queue();
            q.enQueue(root);
        }

        public boolean hasNext() {
            return !q.isEmpty();
        }
        public K next() {
            Node tmp = q.deQueue();
            q.enQueue(tmp.left);
            q.enQueue(tmp.right);
            return tmp.key;
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new MyIterator();
    }

}
