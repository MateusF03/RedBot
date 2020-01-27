package com.mateus.redbot.utils;

public class Pair<K, V> {
    private K value0;
    private V value1;
    public K getValue0() {
        return value0;
    }
    public V getValue1() {
        return value1;
    }
    public Pair(K value0, V value1) {
        this.value0 = value0;
        this.value1 = value1;
    }
}
