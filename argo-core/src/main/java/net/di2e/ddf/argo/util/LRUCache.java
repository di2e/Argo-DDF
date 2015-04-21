package net.di2e.ddf.argo.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = 1L;
    private int cacheSize = 0;

    public LRUCache( int capacity ) {
        super( capacity + 1, 1.1f, true );
        this.cacheSize = capacity;
    }

    @Override
    protected boolean removeEldestEntry( Map.Entry<K, V> eldest ) {
        return size() > cacheSize;
    }

    @Override
    public V put( K k, V v ) {
        if ( cacheSize > 0 ) {
            return super.put( k, v );
        }
        return null;
    }

    public synchronized void updateCacheSize( int size ) {
        this.clear();
        cacheSize = size;
    }

}
