package com.mateus.redbot.utils;

public class Triple<K, T, V> {
    private K k;
    private T t;
    private V v;
    public K getValue0() {
        return k;
    }

    public T getValue1() {
        return t;
    }
    public V getValue2() {
        return v;
    }
    public Triple(K k, T t, V v) {
        this.k = k;
        this.t = t;
        this.v = v;
    }
}
