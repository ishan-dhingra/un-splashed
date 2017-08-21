package com.anythingintellect.networklib.cache;

import com.anythingintellect.networklib.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Created by ishan.dhingra on 20/08/17.
 */

public class InMemoryCache implements Cache {

    // In bytes
    private int currentSize;
    private int maxSize;
    // To make get constant: O(1)
    private HashMap<String, CacheEntry> cacheMap;
    // To make eviction constant: O(1)
    private LinkedList<CacheEntry> cacheQueue;

    @Override
    public CacheEntry get(String key) {
        ensureInit();
        CacheEntry cacheEntry = cacheMap.get(key);
        if (cacheEntry == null) {
            Log.d("Cache miss: " + key);
            return null;
        }
        cacheQueue.remove(cacheEntry);
        cacheQueue.addFirst(cacheEntry);
        currentSize += cacheEntry.size();
        Log.d("Cache hit: " + key);
        return cacheEntry;
    }

    private void ensureInit() {
        if(maxSize == 0) {
            throw new IllegalStateException("Cache not initialized");
        }
    }

    @Override
    public void put(String key, CacheEntry cacheEntry) {
        ensureInit();
        int requiredSize = cacheEntry.size();
        if (requiredSize > maxSize) {
            return;
        }
        CacheEntry existingEntry = cacheMap.get(key);
        if (existingEntry != null) {
            requiredSize = cacheEntry.size() - existingEntry.size();
            cacheQueue.remove(existingEntry);
            currentSize -= existingEntry.size();
        }
        requiredSize = (currentSize + requiredSize) - maxSize;
        ensureCapacity(requiredSize);
        cacheEntry.setKey(key);
        cacheQueue.addFirst(cacheEntry);
        currentSize += cacheEntry.size();
        cacheMap.put(key, cacheEntry);
        Log.d("Cached " + key);
        logCacheStats();
    }

    private void logCacheStats() {
        int currentMb = currentSize;
        int maxMb = maxSize;
        int left = maxMb - currentMb;
        Log.d("Cache Stats");
        Log.d("Current " + currentMb);
        Log.d("Max " + maxMb);
        Log.d("Left " + left);
    }

    private void ensureCapacity(int requiredSize) {
        if (requiredSize <= 0 && (currentSize + requiredSize) <= maxSize) {
            return;
        }
        while (requiredSize > 0 && !cacheQueue.isEmpty()) {
            CacheEntry cacheEntry = cacheQueue.removeLast();
            cacheMap.remove(cacheEntry.getKey());
            requiredSize -= cacheEntry.size();
        }

    }

    // Why not constructor initialization?
    // ~ In this we can intentionally doing in-memory cache only, in other cases like Disk based
    // cache require some heavy operations which are not supposed to be done in constructor
    // so separate init() method is exposed by the interface.
    @Override
    public void init(int maxSize) {
        this.maxSize = maxSize;
        this.currentSize = 0;
        this.cacheMap = new HashMap<>();
        this.cacheQueue = new LinkedList<>();
    }

    @Override
    public void clear() {
        cacheMap.clear();
        cacheQueue.clear();
        currentSize = 0;
    }

    @Override
    public void remove(String key) {
        CacheEntry cacheEntry = cacheMap.remove(key);
        cacheQueue.remove(cacheEntry);
    }

}
