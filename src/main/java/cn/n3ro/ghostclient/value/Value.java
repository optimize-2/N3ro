/*
 * Decompiled with CFR 0_132.
 */
package cn.n3ro.ghostclient.value;

public abstract class Value<V> {
    private String name;
    private V value;

    public Value(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public V getValue() {
        return this.value;
    }

    public void setValue(V value) {
        this.value = value;
        
    }
}

