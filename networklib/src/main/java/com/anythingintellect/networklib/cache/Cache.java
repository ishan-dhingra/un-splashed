package com.anythingintellect.networklib.cache;

/**
 * Created by ishan.dhingra on 20/08/17.
 */

public interface Cache {

    CacheEntry get(String key);

    void put(String key, CacheEntry cacheEntry);

    void init(int maxSize);

    void clear();

    void remove(String key);


    class CacheEntry {

        private final byte[] data;
        private String key;

        public CacheEntry(byte[] data) {
            this.data = data;
        }

        public byte[] getData() {
            return data;
        }

        public int size() {
            return data.length;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }


}
