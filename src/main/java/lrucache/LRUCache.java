package lrucache;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LRUCache<K, V> {

    private final int capacity;
    private final HashMap<K, Node<K, V>> cache;

    private final Node<K, V> head;
    private final Node<K, V> tail;

    private final Lock lock = new ReentrantLock();

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();

        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);

        head.next = tail;
        tail.prev = head;
    }

    // --- Private Operations ---
    private void makeAsHead(Node<K,V> node) {
        removeNode(node);
        addToHead(node);
    }
    private void removeNode(Node<K,V> node) {

        Node<K,V> prev = node.prev;
        Node<K,V> next = node.next;

        prev.next = next;
        next.prev = prev;
    }

    private void addToHead(Node<K,V> node) {

        Node<K,V> currentHead = head.next;

        head.next = node;
        currentHead.prev = node;

        node.prev = head;
        node.next = currentHead;
    }

    // ---- Public Operations ----
    public V get(K key) {

        lock.lock();

        try {
            Node<K, V> node = cache.get(key);

            if (node == null) {
                return null; // Cache Miss
            }

            makeAsHead(node);
            return node.value;
        } finally {
            lock.unlock();
        }
    }

    public void put(K key, V value) {

        lock.lock();

        try {
            Node<K, V> node = cache.get(key);
            // Key in Cache
            if (node != null) {
                node.value = value;
                makeAsHead(node);
                return;
            }
            if (cache.size() == capacity) {
                Node<K, V> lruNode = tail.prev;
                removeNode(lruNode);
                cache.remove(lruNode.key);
            }
            Node<K, V> newNode = new Node<>(key, value);
            addToHead(newNode);
            cache.put(key, newNode);
        } finally {
            lock.unlock();
        }
    }


}
