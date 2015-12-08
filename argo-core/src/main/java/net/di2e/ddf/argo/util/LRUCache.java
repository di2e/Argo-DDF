/**
 * Copyright (C) 2015 Pink Summit, LLC (info@pinksummit.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
