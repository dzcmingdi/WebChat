// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MapFactory.java

package ssm.zmh.webchat.builder;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MapBuilder {
    public MapBuilder() {
    }

    @SafeVarargs
    public static <K, V> Map<K, V> createMap(AbstractMap.SimpleEntry<K, V>... entries) {
        Map<K, V> map = new HashMap<>();

        for (AbstractMap.SimpleEntry<K,V> simpleEntry: entries
        ){
            map.put(simpleEntry.getKey(),simpleEntry.getValue());
        }

        return map;
    }
}