package lrucache;

public class LRUCacheDemo {

    public static void main(String[] args) {

        int capacity = 3;
        LRUCache<Integer, Integer> lruCache = new LRUCache<>(capacity);

        // 1. Check eviction
        lruCache.put(1, 100);
        lruCache.put(2, 200);
        lruCache.put(3, 300);
        lruCache.put(4, 400);
        lruCache.put(5, 500);

        Integer value1 = lruCache.get(1);
        System.out.println(value1);

        // 2. Check recently access node is at the head
        Integer value2 = lruCache.get(5);

        lruCache.put(6, 600);
    }
}
